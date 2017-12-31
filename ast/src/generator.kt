import java.util.*
import java.io.PrintWriter
import java.io.IOException
import java.io.File


fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate_ast <output directory>")
        System.exit(1)
    }
    val outputDir = args[0]

    defineAst(outputDir, "Expr", Arrays.asList(
            "Binary   = val left: Expr?, val operator: Token?, val right: Expr?",
            "Grouping = val expression: Expr?",
            "Literal  = val value: Object?",
            "Unary    = val operator: Token?, val right: Expr?"
    ))
}

@Throws(IOException::class)
private fun defineAst(
        outputDir: String, baseName: String, types: List<String>) {
    val path = "${System.getProperty("user.dir")}/ast/src/$outputDir/$baseName.kt"

    val file = File(path)
    file.parentFile.mkdirs()
    file.createNewFile()

    val writer = PrintWriter(path, "UTF-8")

    writer.println("import java.util.List;")
    writer.println("")
    writer.println("abstract class $baseName {")

    for (type in types) {
        val className = type.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
        val fields = type.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
        defineType(writer, baseName, className, fields)
    }

    writer.println("}")
    writer.close()
}

private fun defineType(writer: PrintWriter, baseName: String,
                       className: String, fieldList: String) {
    writer.println("\tinternal class $className($fieldList) : $baseName()")
}