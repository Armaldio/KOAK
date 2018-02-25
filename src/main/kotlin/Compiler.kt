import java.io.File
import java.util.Scanner
import kotlin.system.exitProcess

class Compiler(file: String) {
    private var _file: String = ""
    var hadError = false

    init {
        this._file = file
    }

    fun getAST(silence: Boolean = false): AST {
        if (!silence)
            println("Compiling ${this._file}...")

        val source: String = File(this._file).readLines().joinToString(separator = "\n")
        val lexer = Lexer(source)
        val tokens = lexer.scanTokens()

        val parser = Parser(tokens, source, this._file)

        val statements = parser.parse()

        val ast = AST()
        statements.filterNotNullTo(ast)

        return ast
    }

    fun toLLFile(_ast: AST): File {
        var ast = _ast

        var outString = ""

        // declarations
        outString += "declare i32 @printf(i8*, ...)"

        // globals
        outString += "@.str = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1"
        outString += ""

        // functions
        val newAst = AST()
        ast.forEach {
            when (it) {
                is Stmt.Function -> {
                    val code = it.getCode()
                    outString += code
                }
                else -> newAst.add(it)
            }
        }
        ast = newAst

        // rest
        outString += """
define void @main() #0 {
entry:
        """
        ast.forEach {
            outString += it.getCode()
        }

        outString +=
                """
    ret void
}
"""

        val llfile = createTempFile("output", ".ll")
        llfile.deleteOnExit()
        llfile.printWriter().use { out -> out.println(outString) }

        // Debug
        //val f = File(".llfile.ll").printWriter().use { out -> out.println(outString) }

        return llfile
    }


    fun compile(llfile: File, path: String): File {
        val exefile = File(path)

        when (OS.getCurrentOS()) {
            OS.OS.WINDOWS -> {
                val clangPath: String? = System.getenv("LLVM_HOME")
                if (clangPath == "") {
                    println("You must define LLVM_HOME environment variable!\nPlease refer to the documentation https://google.com")
                    exitProcess(2)
                }
                val proc = Runtime.getRuntime().exec("""cmd /C $clangPath/bin/clang.exe ${llfile.absolutePath} -o ${exefile.absoluteFile}""")
                Scanner(proc.inputStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                Scanner(proc.errorStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
            }
            OS.OS.LINUX, OS.OS.MAC -> {
                var proc = Runtime.getRuntime().exec("""llvm-as ${llfile.absolutePath} -o ${exefile.absolutePath}""")
                Scanner(proc.inputStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                Scanner(proc.errorStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                proc = Runtime.getRuntime().exec("""llc -march=x86-64 -filetype=obj a.bc -o a.o""")
                Scanner(proc.inputStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                Scanner(proc.errorStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                proc = Runtime.getRuntime().exec("""clang a.o -o a.out""")
                Scanner(proc.inputStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                Scanner(proc.errorStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                proc = Runtime.getRuntime().exec("""rm a.bc a.o""")
                Scanner(proc.inputStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
                Scanner(proc.errorStream).use {
                    while (it.hasNextLine()) println(it.nextLine())
                }
            }
            else -> println("OS is not supported, compilation disabled.")
        }
        return exefile
    }
}