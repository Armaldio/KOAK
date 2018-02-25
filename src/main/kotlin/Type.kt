abstract class Type {
    internal class Int : Type() {
        override fun toString(): String {
            return ("Int()")
        }

        override fun getCode(): String {
            return "i32"
        }

    }

    internal class Double : Type() {
        override fun toString(): String {
            return ("Double()")
        }

        override fun getCode(): String {
            return "double"
        }

    }

    internal class Str : Type() {
        override fun toString(): String {
            return ("Str()")
        }

        override fun getCode(): String {
            return ""
        }
    }

    internal class Char : Type() {
        override fun toString(): String {
            return ("Char()")
        }

        override fun getCode(): String {
            return "i8"
        }
    }

    internal class Void : Type() {
        override fun toString(): String {
            return ("Void()")
        }

        override fun getCode(): String {
            return ""
        }
    }

    abstract fun getCode(): String
    abstract override fun toString(): String
}

