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

    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    private fun report(line: Int, where: String, message: String) {
        System.err.println(
                "[line $line] Error$where: $message")
        hadError = true
    }
}