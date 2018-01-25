import java.io.File

class Compiler(file: String) {
    private var _file: String = ""
    var hadError = false


    init {
        this._file = file
    }

    fun compile(): List<Stmt?> {
        println("Compiling ${this._file}...")

        val source: String = File(this._file).readLines().joinToString(separator = "\n")
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)

        val statements = parser.parse()

        val ast = AST()
        statements.filterNotNullTo(ast)

        return ast
    }
}