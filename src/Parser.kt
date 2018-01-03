//> Parsing Expressions parser

//> Statements and State parser-imports

import java.util.ArrayList
import java.util.Arrays

//> Control Flow import-arrays
//< Control Flow import-arrays

internal class Parser(private val tokens: List<Token>) {
    private var current = 0
    //< advance
    //> utils
    private val isAtEnd: Boolean
        get() = peek().type === TokenType.EOF

    //> parse-error
    private class ParseError : RuntimeException()

    /* Parsing Expressions parse < Statements and State parse
  Expr parse() {
    try {
      return expression();
    } catch (ParseError error) {
      return null;
    }
  }
*/
    //> Statements and State parse
    fun parse(): List<Stmt?> {
        val statements = ArrayList<Stmt?>()
        while (!isAtEnd) {
            /* Statements and State parse < Statements and State parse-declaration
      statements.add(statement());
*/
            //> parse-declaration
            statements.add(declaration())
            //< parse-declaration
        }

        return statements
    }

    //< Statements and State parse
    //> expression
    private fun expression(): Expr {
        /* Parsing Expressions expression < Statements and State expression
    return equality();
*/
        //> Statements and State expression
        return assignment()
        //< Statements and State expression
    }

    //< expression
    //> Statements and State declaration
    private fun declaration(): Stmt? {
        try {
            //> Classes match-class
            if (match(TokenType.CLASS)) return classDeclaration()
            //< Classes match-class
            //> Functions match-fun
            if (match(TokenType.DEF)) return function("function")
            //< Functions match-fun
            return if (match(TokenType.VAR)) varDeclaration() else statement()

        } catch (error: ParseError) {
            synchronize()
            return null
        }

    }
    //< Statements and State declaration
    //> Classes parse-class-declaration

    private fun classDeclaration(): Stmt {
        val name = consume(TokenType.IDENTIFIER, "Expect class name.")
        //> Inheritance parse-superclass

        var superclass: Expr? = null
        if (match(TokenType.LESS)) {
            consume(TokenType.IDENTIFIER, "Expect superclass name.")
            superclass = Expr.Variable(previous())
        }

        //< Inheritance parse-superclass
        consume(TokenType.LEFT_BRACE, "Expect '{' before class body.")

        val methods = ArrayList<Stmt.Function>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd) {
            methods.add(function("method"))
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after class body.")

        /* Classes parse-class-declaration < Inheritance construct-class-ast
    return new Stmt.Class(name, methods);
*/
        //> Inheritance construct-class-ast
        return Stmt.Class(name, superclass, methods)
        //< Inheritance construct-class-ast
    }

    //< Classes parse-class-declaration
    //> Statements and State parse-statement
    private fun statement(): Stmt {
        //> Control Flow match-for
        if (match(TokenType.FOR)) return forStatement()
        //< Control Flow match-for
        //> Control Flow match-if
        if (match(TokenType.IF)) return ifStatement()
        //< Control Flow match-if
        if (match(TokenType.PRINT)) return printStatement()
        //> Functions match-return
        if (match(TokenType.RETURN)) return returnStatement()
        //< Functions match-return
        //> Control Flow match-while
        if (match(TokenType.WHILE)) return whileStatement()
        //< Control Flow match-while
        //> parse-block
        return if (match(TokenType.LEFT_BRACE)) Stmt.Block(block()) else expressionStatement()
        //< parse-block

    }

    //< Statements and State parse-statement
    //> Control Flow for-statement
    private fun forStatement(): Stmt {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'for'.")

        /* Control Flow for-statement < Control Flow for-initializer
    // More here...
*/
        //> for-initializer
        val initializer: Stmt?
        if (match(TokenType.SEMICOLON)) {
            initializer = null
        } else if (match(TokenType.VAR)) {
            initializer = varDeclaration()
        } else {
            initializer = expressionStatement()
        }
        //< for-initializer
        //> for-condition

        var condition: Expr? = null
        if (!check(TokenType.SEMICOLON)) {
            condition = expression()
        }
        consume(TokenType.SEMICOLON, "Expect ';' after loop condition.")
        //< for-condition
        //> for-increment

        var increment: Expr? = null
        if (!check(TokenType.RIGHT_PAREN)) {
            increment = expression()
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after for clauses.")
        //< for-increment
        //> for-body
        var body = statement()

        //> for-desugar-increment
        if (increment != null) {
            body = Stmt.Block(Arrays.asList(
                    body,
                    Stmt.Expression(increment)))
        }

        //< for-desugar-increment
        //> for-desugar-condition
        if (condition == null) condition = Expr.Literal(true)
        body = Stmt.While(condition, body)

        //< for-desugar-condition
        //> for-desugar-initializer
        if (initializer != null) {
            body = Stmt.Block(Arrays.asList(initializer, body))
        }

        //< for-desugar-initializer
        return body
        //< for-body
    }

    //< Control Flow for-statement
    //> Control Flow if-statement
    private fun ifStatement(): Stmt {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.")
        val condition = expression()
        consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition.") // [parens]

        val thenBranch = statement()
        var elseBranch: Stmt? = null
        if (match(TokenType.ELSE)) {
            elseBranch = statement()
        }

        return Stmt.If(condition, thenBranch, elseBranch)
    }

    //< Control Flow if-statement
    //> Statements and State parse-print-statement
    private fun printStatement(): Stmt {
        val value = expression()
        consume(TokenType.SEMICOLON, "Expect ';' after value.")
        return Stmt.Print(value)
    }

    //< Statements and State parse-print-statement
    //> Functions parse-return-statement
    private fun returnStatement(): Stmt {
        val keyword = previous()
        var value: Expr? = null
        if (!check(TokenType.SEMICOLON)) {
            value = expression()
        }

        consume(TokenType.SEMICOLON, "Expect ';' after return value.")
        return Stmt.Return(keyword, value)
    }

    //< Functions parse-return-statement
    //> Statements and State parse-var-declaration
    private fun varDeclaration(): Stmt {
        val name = consume(TokenType.IDENTIFIER, "Expect variable name.")

        var initializer: Expr? = null
        if (match(TokenType.EQUAL)) {
            initializer = expression()
        }

        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.")
        return Stmt.Var(name, initializer)
    }

    //< Statements and State parse-var-declaration
    //> Control Flow while-statement
    private fun whileStatement(): Stmt {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'while'.")
        val condition = expression()
        consume(TokenType.RIGHT_PAREN, "Expect ')' after condition.")
        val body = statement()

        return Stmt.While(condition, body)
    }

    //< Control Flow while-statement
    //> Statements and State parse-expression-statement
    private fun expressionStatement(): Stmt {
        val expr = expression()
        consume(TokenType.SEMICOLON, "Expect ';' after expression.")
        return Stmt.Expression(expr)
    }

    //< Statements and State parse-expression-statement
    //> Functions parse-function
    private fun function(kind: String): Stmt.Function {
        val name = consume(TokenType.IDENTIFIER, "Expect $kind name.")
        //> parse-parameters
        consume(TokenType.LEFT_PAREN, "Expect '(' after $kind name.")
        val parameters = ArrayList<Token>()
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (parameters.size >= 8) {
                    error(peek(), "Cannot have more than 8 parameters.")
                }

                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."))
            } while (match(TokenType.COMMA))
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.")
        //< parse-parameters
        //> parse-body

        consume(TokenType.LEFT_BRACE, "Expect '{' before $kind body.")
        val body = block()
        return Stmt.Function(name, parameters, body)
        //< parse-body
    }

    //< Functions parse-function
    //> Statements and State block
    private fun block(): List<Stmt> {
        val statements = ArrayList<Stmt>()

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd) {
            statements.add(this.declaration()!!)
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.")
        return statements
    }

    //< Statements and State block
    //> Statements and State parse-assignment
    private fun assignment(): Expr {
        /* Statements and State parse-assignment < Control Flow or-in-assignment
    Expr expr = equality();
*/
        //> Control Flow or-in-assignment
        val expr = or()
        //< Control Flow or-in-assignment

        if (match(TokenType.EQUAL)) {
            val equals = previous()
            val value = assignment()

            if (expr is Expr.Variable) {
                val name = expr.name
                return Expr.Assign(name, value)
                //> Classes assign-set
            } else if (expr is Expr.Get) {
                return Expr.Set(expr.`object`, expr.name, value)
                //< Classes assign-set
            }

            error(equals, "Invalid assignment target.")
        }

        return expr
    }

    //< Statements and State parse-assignment
    //> Control Flow or
    private fun or(): Expr {
        var expr = and()

        while (match(TokenType.OR)) {
            val operator = previous()
            val right = and()
            expr = Expr.Logical(expr, operator, right)
        }

        return expr
    }

    //< Control Flow or
    //> Control Flow and
    private fun and(): Expr {
        var expr = equality()

        while (match(TokenType.AND)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Logical(expr, operator, right)
        }

        return expr
    }

    //< Control Flow and
    //> equality
    private fun equality(): Expr {
        var expr = comparison()

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    //< equality
    //> comparison
    private fun comparison(): Expr {
        var expr = addition()

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = addition()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    //< comparison
    //> addition-and-multiplication
    private fun addition(): Expr {
        var expr = multiplication()

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = multiplication()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun multiplication(): Expr {
        var expr = unary()

        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    //< addition-and-multiplication
    //> unary
    private fun unary(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        /* Parsing Expressions unary < Functions unary-call
    return primary();
*/
        //> Functions unary-call
        return call()
        //< Functions unary-call
    }

    //< unary
    //> Functions finish-call
    private fun finishCall(callee: Expr): Expr {
        val arguments = ArrayList<Expr>()
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                //> check-max-arity
                if (arguments.size >= 8) {
                    error(peek(), "Cannot have more than 8 arguments.")
                }
                //< check-max-arity
                arguments.add(expression())
            } while (match(TokenType.COMMA))
        }

        val paren = consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.")

        return Expr.Call(callee, paren, arguments)
    }

    //< Functions finish-call
    //> Functions call
    private fun call(): Expr {
        var expr = primary()

        while (true) {
            if (match(TokenType.LEFT_PAREN)) {
                expr = finishCall(expr)
                //> Classes parse-property
            } else if (match(TokenType.DOT)) {
                val name = consume(TokenType.IDENTIFIER,
                        "Expect property name after '.'.")
                expr = Expr.Get(expr, name)
                //< Classes parse-property
            } else {
                break
            }
        }

        return expr
    }
    //< Functions call
    //> primary

    private fun primary(): Expr {
        if (match(TokenType.FALSE)) return Expr.Literal(false)
        if (match(TokenType.TRUE)) return Expr.Literal(true)
        if (match(TokenType.NULL)) return Expr.Literal(null!!)

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Expr.Literal(previous().literal!!)
        }
        //> Inheritance parse-super

        if (match(TokenType.SUPER)) {
            val keyword = previous()
            consume(TokenType.DOT, "Expect '.' after 'super'.")
            val method = consume(TokenType.IDENTIFIER,
                    "Expect superclass method name.")
            return Expr.Super(keyword, method)
        }
        //< Inheritance parse-super
        //> Classes parse-this

        if (match(TokenType.THIS)) return Expr.This(previous())
        //< Classes parse-this
        //> Statements and State parse-identifier

        if (match(TokenType.IDENTIFIER)) {
            return Expr.Variable(previous())
        }
        //< Statements and State parse-identifier

        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }
        //> primary-error

        throw error(peek(), "Expect expression.")
        //< primary-error
    }

    //< primary
    //> match
    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }

        return false
    }

    //< match
    //> consume
    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()

        throw error(peek(), message)
    }

    //< consume
    //> check
    private fun check(tokenType: TokenType): Boolean {
        return if (isAtEnd) false else peek().type === tokenType
    }

    //< check
    //> advance
    private fun advance(): Token {
        if (!isAtEnd) current++
        return previous()
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    //< utils
    //> error
    private fun error(token: Token, message: String): ParseError {
        println("Error: $message -> $token")
        return ParseError()
    }

    //< error
    //> synchronize
    private fun synchronize() {
        advance()

        while (!isAtEnd) {
            if (previous().type === TokenType.SEMICOLON) return

            when (peek().type) {
                TokenType.CLASS, TokenType.DEF, TokenType.VAR, TokenType.FOR, TokenType.IF, TokenType.WHILE, TokenType.PRINT, TokenType.RETURN -> return
            }

            advance()
        }
    }
    //< synchronize
}
