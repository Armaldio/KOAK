import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var debug = false
    println("--- Welcome to KOAK compiler ---")
    println("""Detected ${OS.getCurrentOS()} OS""")
    var ac = args.size
    var av  = mutableListOf<String>()
    args.forEach {
        if (it == "-d") {
            ac--
            debug = true
        }
        av.add(it)
    }

    when (ac) {
        0 -> repl(debug)
        1 -> compile(av[0], debug)
        else -> error("Too much parameters")
    }
    println("---           End           ---")
}

fun repl(debug: Boolean) {
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

        if (debug)
            ast.forEach { println(it.getCode()) }

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

fun compile(file: String, debug: Boolean) {
    val compiler = Compiler(file)
    val ast = compiler.getAST()

    if (debug)
    {
        ast.forEach { println(it) }
    }

    val llfile = compiler.toLLFile(ast)
    val out: String = if (OS.getCurrentOS() == OS.OS.LINUX || OS.getCurrentOS() == OS.OS.MAC) "./a.bc" else "./a.exe"
    val exefile = compiler.compile(llfile, out)
    llfile.delete()
}

