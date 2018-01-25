abstract class Stmt {

    internal class Block(val statements: List<Stmt>) : Stmt() {
        override fun toString(): String {
            return "Block(statements=$statements)"
        }
    }

    internal class Class(val name: Token, val superclass: Expr?, val methods: List<Function>) : Stmt() {
        override fun toString(): String {
            return "Class(name=$name, superclass=$superclass, methods=$methods)"
        }
    }

    internal class Expression(val expression: Expr) : Stmt() {
        override fun toString(): String {
            return "Expression(expression=$expression)"
        }
    }

    internal class Function(val name: Token, val parameters: List<Parameter>, val body: Expr) : Stmt() {
        override fun toString(): String {
            return "Function(name=$name, parameters=$parameters, body=$body)"
        }
    }

    internal class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {
        override fun toString(): String {
            return "If(condition=$condition, thenBranch=$thenBranch, elseBranch=$elseBranch)"
        }
    }

    internal class Print(val expression: Expr) : Stmt() {
        override fun toString(): String {
            return "Print(expression=$expression)"
        }
    }

    internal class Return(val keyword: Token, val value: Expr?) : Stmt() {
        override fun toString(): String {
            return "Return(keyword=$keyword, value=$value)"
        }
    }

    internal class Var(val type: String, val name: Token, val initializer: Expr?) : Stmt() {
        override fun toString(): String {
            return "Var(type='$type', name=$name, initializer=$initializer)"
        }
    }

    internal class While(val condition: Expr, val body: Stmt) : Stmt() {
        override fun toString(): String {
            return "While(condition=$condition, body=$body)"
        }
    }

    internal class Comment(val comment: Token) : Stmt() {
        override fun toString(): String {
            return "Comment(comment=$comment)"
        }
    }

    internal class Parameter(val name: Token, val type: Token) : Stmt() {
        override fun toString(): String {
            return "Parameter(name=$name, type=$type)"
        }
    }
}
