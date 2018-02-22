import Token

abstract class Types {
    internal class Int(val value: Int) : Types() {
        override fun toString(): String {
            return ("Int(value=$value)")
        }

        override fun getCode(): String {
            return ""
        }

    }

    internal class Double(val value: Double) : Types() {
        override fun toString(): String {
            return ("Double(value=$value)")
        }

        override fun getCode(): String {
            return ""
        }

    }

    internal class Str(val value: Str) : Types() {
        override fun toString(): String {
            return ("Str(value=$value)")
        }

        override fun getCode(): String {
            return ""
        }
    }

    abstract fun getCode(): String
    abstract override fun toString(): String
}

