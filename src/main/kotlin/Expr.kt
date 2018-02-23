import Token

abstract class Expr {

    internal class Assign(val name: Token, val value: Expr) : Expr() {
        override fun toString(): String {
            return "Assign(name=$name, value=$value)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun toString(): String {
            return "Binary(left=$left, operator=$operator, right=$right)"
        }
        override fun getCode(): String {
            return when (operator.lexeme) {
                "+" -> {
                    "add nsw i32 ${left.getCode()}, ${right.getCode()}"
                }
                else -> ""
            }
        }
    }

    internal class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun toString(): String {
            return "Call(callee=$callee, paren=$paren, arguments=$arguments)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Get(val `object`: Expr, val name: Token) : Expr() {
        override fun toString(): String {
            return "Get(`object`=$`object`, name=$name)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Grouping(val expression: Expr) : Expr() {
        override fun toString(): String {
            return "Grouping(expression=$expression)"
        }
        override fun getCode(): String {
            return expression.getCode()
        }
    }

    internal class Literal(val value: Any) : Expr() {
        override fun toString(): String {
            return "Literal(value=$value)"
        }
        override fun getCode(): String {
            return "$value"
        }
    }

    internal class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun toString(): String {
            return "Logical(left=$left, operator=$operator, right=$right)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Set(val `object`: Expr, val name: Token, val value: Expr) : Expr() {
        override fun toString(): String {
            return "Set(`object`=$`object`, name=$name, value=$value)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Super(val keyword: Token, val method: Token) : Expr() {
        override fun toString(): String {
            return "Super(keyword=$keyword, method=$method)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class This(val keyword: Token) : Expr() {
        override fun toString(): String {
            return "This(keyword=$keyword)"
        }
        override fun getCode(): String {
            return ""
        }
    }

    internal class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun getCode(): String {
            return ""
        }

        override fun toString(): String {
            return "Unary(operator=$operator, right=$right)"
        }
    }

    internal class Variable(val name: Token) : Expr() {
        override fun getCode(): String {
            return "%${name.lexeme}"
        }

        override fun toString(): String {
            return "Variable(name=$name)"
        }
    }

    internal class ReturnValue(val value: Stmt, val type: Type) : Expr() {
        override fun getCode(): String {
            return "${type.getCode()} (${value.getCode()})"
        }

        override fun toString(): String {
            return "ReturnValue(value=$value, type=$type)"
        }
    }

    abstract fun getCode(): String
    abstract override fun toString(): String
}

