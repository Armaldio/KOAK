import java.io.File

class Compiler(file: String) {
    private var _file: String = ""
    var hadError = false


    init {
        this._file = file
    }

    fun compile() {
        println("Compiling ${this._file}...")

        val source: String = File(this._file).readLines().joinToString(separator = "\n")
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()

        // For now, just print the tokens.
        for (token in tokens) {
            System.out.println(token)
        }
    }
}