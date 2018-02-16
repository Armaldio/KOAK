abstract class Stmt {

    internal class Block(val statements: List<Stmt>) : Stmt() {
        override fun getCode(): String {
            return ""
        }

        override fun toString(): String {
            return "Block(statements=$statements)"
        }
    }

    internal class Class(val name: Token, val superclass: Expr?, val methods: List<Function>) : Stmt() {
        override fun toString(): String {
            return "Class(name=$name, superclass=$superclass, methods=$methods)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Expression(val expression: Expr) : Stmt() {
        override fun toString(): String {
            return "Expression(expression=$expression)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Function(val name: Token, val parameters: List<Parameter>, val body: List<Stmt>, val returntype: Token) : Stmt() {
        override fun toString(): String {
            return "Function(name=$name, parameters=$parameters, body=$body, returntype=$returntype)"
        }

        override fun getCode(): String {
            return """
define void @${name.lexeme}(${parameters.joinToString { "i32 %" + it.name }}) {
entry:
  ${body.forEach { it.getCode() }}
  ret void
}
                """
        }
    }

    internal class If(val condition: Expr, val thenBranch: List<Stmt>, val elseBranch: List<Stmt>) : Stmt() {
        override fun toString(): String {
            return "If(condition=$condition, thenBranch=$thenBranch, elseBranch=$elseBranch)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Print(val expression: Expr) : Stmt() {
        override fun toString(): String {
            return "Print(expression=$expression)"
        }

        override fun getCode(): String {
            return """
                ${expression.getCode()}_ = load i32, i32* ${expression.getCode()}, align 4
                call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i32 0, i32 0), i32 ${expression.getCode()}_)
                """
        }
    }

    internal class Return(val keyword: Token, val value: Expr?) : Stmt() {
        override fun toString(): String {
            return "Return(keyword=$keyword, value=$value)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    /**
     * Variable definition
     */
    internal class VariableDefinition(val type: String, val name: Token, val initializer: Expr?) : Stmt() {
        override fun toString(): String {
            return "VariableDefinition(type='$type', name=$name, initializer=$initializer)"
        }

        override fun getCode(): String {
            return """
%${name.lexeme} = alloca i32, align 4
store i32 ${initializer?.getCode()}, i32* %${name.lexeme}, align 4
"""
        }
    }

    internal class While(val condition: Expr, val body: Stmt) : Stmt() {
        override fun toString(): String {
            return "While(condition=$condition, body=$body)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Comment(val comment: Token) : Stmt() {
        override fun toString(): String {
            return "Comment(comment=$comment)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Parameter(val name: Token, val type: Token) : Stmt() {
        override fun toString(): String {
            return "Parameter(name=$name, type=$type)"
        }

        override fun getCode(): String {
            return ""
        }
    }

    abstract fun getCode(): String
    abstract override fun toString(): String
}
