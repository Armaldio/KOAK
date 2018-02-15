fun main(args: Array<String>) {
    println("--- Welcome to KOAK compiler ---")
    when (args.size) {
        0 -> repl()
        1 -> getAST(args[0])
        else -> error("Too much parameters")
    }
    println("---           End           ---")
}

fun repl() {
    println("""
KOAK, compiler/interpreter
ready> """)

    var text: String?

    do {

        text = readLine()
        println("Found [$text]")

    } while (text !== null)
}

fun getAST(file: String) {
    val compiler = Compiler(file)
    val ast = compiler.getAST()

    compiler.toLLFile(ast, "output")

    for ((index, statement) in ast.withIndex()) {
        System.out.println("$index: $statement")
    }
}

