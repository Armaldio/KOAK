enum class TokenType {
    // Single-character tokens.
    LEFT_PAREN,
    RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, SLASH, STAR, COLON, SEMICOLON,

    // One or two character tokens.
    NOT,
    NOT_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    COMMENT,
    EOL,

    //types
    STRING_TYPE,
    INT_TYPE,

    // Literals.
    IDENTIFIER,
    STRING, NUMBER,

    // Keywords.
    AND,
    CLASS, ELSE, FALSE, DEF, FOR, IF, NULL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, WHILE,

    EOF
}