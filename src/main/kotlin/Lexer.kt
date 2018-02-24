import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

class Lexer(private val source: String) {
    private val tokens = mutableListOf<Token>()

    private var start = 0
    private var current = 0
    private var column = 0
    private var line = 1

    private var keywords: MutableMap<String, TokenType> = mutableMapOf(
            "and" to TokenType.AND,
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "false" to TokenType.FALSE,
            "for" to TokenType.FOR,
            "str" to TokenType.STRING_TYPE,
            "void" to TokenType.VOID_TYPE,
            "int" to TokenType.INT_TYPE,
            "char" to TokenType.CHAR_TYPE,
            "double" to TokenType.DOUBLE_TYPE,
            "def" to TokenType.DEF,
            "if" to TokenType.IF,
            "then" to TokenType.THEN,
            "null" to TokenType.NULL,
            "or" to TokenType.OR,
            "print" to TokenType.PRINT,
            "return" to TokenType.RETURN,
            "super" to TokenType.SUPER,
            "this" to TokenType.THIS,
            "true" to TokenType.TRUE,
            "while" to TokenType.WHILE,
            "do" to TokenType.DO,
            "extern" to TokenType.EXTERN,
            "in" to TokenType.IN
    )

    fun scanTokens(): MutableList<Token> {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line, column))
        return tokens
    }

    private fun report(line: Int, column: Int, where: String, message: String) {
        System.err.println("[line $line] Error$where: $message")
        System.err.println(source.split("\n")[line - 1])
        for (i in 1..(column - 1)) System.err.print(" ")
        System.err.println("^")
        throw Exception(message)
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun advance(): Char {
        current++
        column++
        return source[(current - 1)]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)

        val token = Token(type, text, literal, line, column)

        tokens.add(token)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[(current)] != expected) return false

        current++
        return true
    }

    private fun peek(): Char {
        return if (isAtEnd()) '\u0000' else source[(current)]
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        // Unterminated string.
        if (isAtEnd()) {
            this.report(line, 0, "", "Unterminated string.")
            return
        }

        // The closing ".
        advance()

        // Trim the surrounding quotes.
        val value = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        var isDouble: Boolean = false
        while (isDigit(peek())) advance()

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            isDouble = true
            // Consume the "."
            advance()

            while (isDigit(peek())) advance()
        }

        when (isDouble) {
            true -> addToken(TokenType.NUMBER, parseDouble(source.substring(start, current)))
            false -> addToken(TokenType.NUMBER, parseInt(source.substring(start, current)))
        }
    }

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) '\u0000' else source[(current + 1)]
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val text = source.substring(start, current)

        var type = keywords[text]
        if (type == null) type = TokenType.IDENTIFIER
        addToken(type)
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' ||
                c in 'A'..'Z' ||
                c == '_'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

    private fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            '[' -> addToken(TokenType.LEFT_BRACKET)
            ']' -> addToken(TokenType.RIGHT_BRACKET)
            ',' -> addToken(TokenType.COMMA)
            ':' -> addToken(TokenType.COLON)
            ';' -> addToken(TokenType.SEMICOLON)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            '*' -> addToken(TokenType.STAR)
            '#' -> {
                while (peek() != '\n' && !isAtEnd()) {
                    advance()
                }
                if (peek() == '\n')
                    advance()
            }
            '|' -> {
                when {
                    this.match('|') -> addToken(TokenType.OR)
                    this.match('=') -> addToken(TokenType.OR_ASSIGN)
                    else -> {
                        addToken(TokenType.PIPE)
                    }
                }
            }
            '&' -> {
                when {
                    this.match('&') -> addToken(TokenType.AND)
                    this.match('=') -> addToken(TokenType.AND_ASSIGN)
                    else -> addToken(TokenType.BINARY_AND)
                }
            }
            '/' -> if (match('/')) while (peek() != '\n' && !isAtEnd()) advance() else addToken(TokenType.SLASH)
            '!' -> addToken(if (this.match('=')) TokenType.NOT_EQUAL else TokenType.NOT)
            '=' -> addToken(if (this.match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (this.match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (this.match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            ' ', '\t' -> Unit

            '\'' -> Unit

            '\n' -> {
                //addToken(TokenType.EOL)
                line++
                column = 0
            }

            '"' -> string()

            else -> when {
                isDigit(c) -> number()
                isAlpha(c) -> identifier()
                else -> this.report(line, column, "", "Unexpected character.")
            }

        }
    }

}