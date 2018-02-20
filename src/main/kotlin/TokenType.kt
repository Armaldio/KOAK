enum class TokenType {
    // Single-character tokens.
    LEFT_PAREN,
    RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, COMMA, DOT, MINUS, PLUS, SLASH, STAR, COLON, SEMICOLON, PIPE,
    BINARY_AND,
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
    VOID_TYPE,
    CHAR_TYPE,

    // Assignement
    OR_ASSIGN, AND_ASSIGN,
    // Literals.
    IDENTIFIER,
    STRING, NUMBER,

    // Keywords.
    AND,
    CLASS, ELSE, FALSE, DEF, FOR, IF, NULL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, WHILE, THEN,
    DO,
    EXTERN,
    IN,
    EOF
}