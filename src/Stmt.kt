internal abstract class Stmt {
    internal interface Visitor<R> {
        fun visitBlockStmt(stmt: Block): R
        fun visitClassStmt(stmt: Class): R
        fun visitExpressionStmt(stmt: Expression): R
        fun visitFunctionStmt(stmt: Function): R
        fun visitIfStmt(stmt: If): R
        fun visitPrintStmt(stmt: Print): R
        fun visitReturnStmt(stmt: Return): R
        fun visitVarStmt(stmt: Var): R
        fun visitWhileStmt(stmt: While): R
    }

    // Nested Stmt classes here...
    //> stmt-block
    internal class Block(val statements: List<Stmt>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBlockStmt(this)
        }
    }

    //< stmt-block
    //> stmt-class
    internal class Class(val name: Token, val superclass: Expr?, val methods: List<Function>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitClassStmt(this)
        }
    }

    //< stmt-class
    //> stmt-expression
    internal class Expression(val expression: Expr) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpressionStmt(this)
        }
    }

    //< stmt-expression
    //> stmt-function
    internal class Function(val name: Token, val parameters: List<Token>, val body: List<Stmt>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitFunctionStmt(this)
        }
    }

    //< stmt-function
    //> stmt-if
    internal class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIfStmt(this)
        }
    }

    //< stmt-if
    //> stmt-print
    internal class Print(val expression: Expr) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrintStmt(this)
        }
    }

    //< stmt-print
    //> stmt-return
    internal class Return(val keyword: Token, val value: Expr?) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReturnStmt(this)
        }
    }

    //< stmt-return
    //> stmt-var
    internal class Var(val name: Token, val initializer: Expr?) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStmt(this)
        }
    }

    //< stmt-var
    //> stmt-while
    internal class While(val condition: Expr, val body: Stmt) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitWhileStmt(this)
        }
    }
    //< stmt-while

    internal abstract fun <R> accept(visitor: Visitor<R>): R
}
//< Appendix II stmt
