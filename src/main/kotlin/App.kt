import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("--- Welcome to KOAK compiler ---")
    println("""Detected ${OS.getCurrentOS()} OS""")
    when (args.size) {
        0 -> repl()
        1 -> compile(args[0])
        else -> error("Too much parameters")
    }
    println("---           End           ---")
}

fun repl() {
    val file = createTempFile("repl")
    file.deleteOnExit()

    println("""
KOAK, compiler/interpreter
ready> """)

    var text: String?

    do {

        text = readLine()
        if (text == null)
            exitProcess(0)
        file.appendText(text)

        val compiler = Compiler(file.absolutePath)
        val ast = compiler.getAST(true)

        //ast.forEach { println(it.getCode()) }

        val llfile = compiler.toLLFile(ast)
        val tempCompiledFile = createTempFile("output", ".exe")
        tempCompiledFile.deleteOnExit()
        val compiledFile = compiler.compile(llfile, tempCompiledFile.absolutePath)

        execute(compiledFile.absolutePath)

    } while (text !== null)
}

fun execute(filename: String): String {
    var output = ""
    val proc = Runtime.getRuntime().exec("""cmd /C $filename""")
    Scanner(proc.inputStream).use {
        while (it.hasNextLine()) {
            val line = it.nextLine()
            output += line
            //println(line)
        }
    }
    Scanner(proc.errorStream).use {
        while (it.hasNextLine()) println(it.nextLine())
    }
    return output
}

fun compile(file: String) {
    val compiler = Compiler(file)
    val ast = compiler.getAST()

    val llfile = compiler.toLLFile(ast)
    val out: String = if (OS.getCurrentOS() == OS.OS.LINUX || OS.getCurrentOS() == OS.OS.MAC) "./a.bc" else "./a.exe"
    val exefile = compiler.compile(llfile, out)
    llfile.delete()
}

