import java.io.File
import java.util.Scanner

class Compiler(file: String) {
    private var _file: String = ""
    var hadError = false


    init {
        this._file = file
    }

    fun getAST(): AST {
        println("Compiling ${this._file}...")

        val source: String = File(this._file).readLines().joinToString(separator = "\n")
        val lexer = Lexer(source)
        val tokens = lexer.scanTokens()

        val parser = Parser(tokens)

        val statements = parser.parse()

        val ast = AST()
        statements.filterNotNullTo(ast)

        return ast
    }

    fun toLLFile(ast: AST, filename: String) {
        var outString = ""

        // declarations
        outString += "declare i32 @printf(i8*, ...)"

        // globals
        outString += "@.str = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1"
        outString += ""

        // functions
        outString += ""


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


        File("$filename.ll").printWriter().use { out -> out.println(outString) }

        val clangPath: String? = System.getenv("LLVM_HOME")
        println("Clang path: $clangPath")
        val proc = Runtime.getRuntime().exec("cmd /C $clangPath/bin/clang.exe $filename.ll")
        Scanner(proc.inputStream).use {
            while (it.hasNextLine()) println(it.nextLine())
        }
        Scanner(proc.errorStream).use {
            while (it.hasNextLine()) println(it.nextLine())
        }
    }
}