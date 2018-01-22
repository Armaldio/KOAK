import Token

abstract class Expr {
       // Nested Expr classes here...

    internal class Assign(val name: Token, val value: Expr) : Expr() {
        override fun toString(): String {
            return "Assign(name=$name, value=$value)"
        }
    }

    internal class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun toString(): String {
            return "Binary(left=$left, operator=$operator, right=$right)"
        }
    }

    internal class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun toString(): String {
            return "Call(callee=$callee, paren=$paren, arguments=$arguments)"
        }
    }

    internal class Get(val `object`: Expr, val name: Token) : Expr() {
        override fun toString(): String {
            return "Get(`object`=$`object`, name=$name)"
        }
    }

    internal class Grouping(val expression: Expr) : Expr() {
        override fun toString(): String {
            return "Grouping(expression=$expression)"
        }
    }

    internal class Literal(val value: Any) : Expr() {
        override fun toString(): String {
            return "Literal(value=$value)"
        }
    }

    internal class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun toString(): String {
            return "Logical(left=$left, operator=$operator, right=$right)"
        }
    }

    internal class Set(val `object`: Expr, val name: Token, val value: Expr) : Expr() {
        override fun toString(): String {
            return "Set(`object`=$`object`, name=$name, value=$value)"
        }
    }

    internal class Super(val keyword: Token, val method: Token) : Expr() {
        override fun toString(): String {
            return "Super(keyword=$keyword, method=$method)"
        }
    }

    internal class This(val keyword: Token) : Expr() {
        override fun toString(): String {
            return "This(keyword=$keyword)"
        }
    }

    internal class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun toString(): String {
            return "Unary(operator=$operator, right=$right)"
        }
    }

    internal class Variable(val name: Token) : Expr() {
        override fun toString(): String {
            return "Variable(name=$name)"
        }
    }

}

