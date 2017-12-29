fun main(args: Array<String>) {
    when (args.size) {
        0 -> repl()
        1 -> compile(args[0])
        else -> error("Too much parameters")
    }

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

fun compile(file: String) {
    val compiler = Compiler(file)
    compiler.compile()
}
