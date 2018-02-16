class Token(val type: TokenType, var lexeme: String, val literal: Any?, val line: Int, val column: Int) {
    override fun toString(): String {
        return lexeme
    }
}