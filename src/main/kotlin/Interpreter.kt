internal class Interpreter(private val stmts: AST, val source: String) {
    private var current: Int = 0
    private class InterpreterError : RuntimeException()

    fun interpret(): Boolean {
        while (this.current < stmts.count()) {
            when (stmts[this.current]) {
                is Stmt.VariableDefinition -> this.varDefinition()
            }
            this.current++
        }
        return true
    }

    private fun varDefinition() {
        val variable = stmts[this.current] as Stmt.VariableDefinition

        when (variable.type) {
            is Type.Str -> this.checkStr(variable)
            is Type.Double -> this.checkDouble(variable)
            is Type.Int -> this.checkInt(variable)
            else -> throw Exception("Type not found")
        }
    }

    private fun checkStr(variable: Stmt.VariableDefinition) {
        val assign = variable.initializer as Expr.Literal
        if (assign.value !is String) {
            this.error(variable.name, "Variable is not string")
        }
    }

    private fun checkDouble(variable: Stmt.VariableDefinition) {
        val assign = variable.initializer as Expr.Literal
        if (assign.value !is Double) {
            this.error(variable.name, "Variable is not double")
        }
    }

    private fun checkInt(variable: Stmt.VariableDefinition) {
        val assign = variable.initializer as Expr.Literal
        if (assign.value !is Int) {
            this.error(variable.name, "Variable is not int")
        }
    }

    private fun error(token: Token, message: String): InterpreterError {
        report(token.line, token.column, "$message instead got ${token.type} $token")
        return InterpreterError()
    }

    private fun report(line: Int, column: Int, message: String) {
        System.err.println("[line $line] Error: $message")
        System.err.println(source.split("\n")[line])
        for (i in 1..(column - 1)) System.err.print(" ")
        System.err.println("^")
        throw Exception(message)
    }
}