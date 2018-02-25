import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test
import java.io.File

class FilesTest {

    fun parse(text: String): AST {
        val scanner = Lexer(text)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens, text, "test.koak")

        val statements = parser.parse()
        val ast = AST()
        statements.filterNotNullTo(ast)

        return ast
    }

    @Test
    fun test_all_test_files() {
        val files = mutableListOf<String>()
        val results = mutableListOf<String>()

        File("src/test/kotlin/").walkTopDown().forEach {
            if (it.extension == "koak") {
                files.add(it.absolutePath)
                results.add(File(it.absolutePath.replace("koak", "result")).absolutePath)
            }
        }

        var i = 0
        files.forEach {
            val compiler = Compiler(it)
            val ast = compiler.getAST()

            val result = File(results[i]).readLines()
            var lines = 0
            File(results[i]).forEachLine { lines++ }

            assertEquals(ast.size, lines)


            var j = 0
            ast.forEach {
                assertEquals(it.toString(), result[j])
                j++
            }

            i++
        }

    }

    @Test
    fun int_declaration_equals_3() {
        val ast = this.parse("int a = 3")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "VariableDefinition(type='Int()', name=a, initializer=Literal(value=3))")
    }

    @Test
    fun int_declaration_equals_operation() {
        val ast = this.parse("int a = 3 + 3")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "VariableDefinition(type='Int()', name=a, initializer=Binary(left=Literal(value=3), operator=+, right=Literal(value=3)))")
    }

    @Test
    fun int_declaration_equals_functioncall() {
        val ast = this.parse("int sum = add(10, 5)")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "VariableDefinition(type='Int()', name=sum, initializer=Call(callee=Variable(name=add), paren=), arguments=[Literal(value=10), Literal(value=5)]))")
    }

    @Ignore
    @Test
    fun int_declaration_equals_string() {
        val ast = this.parse("int sum = \"hello\"")

        assertTrue(ast.size == 1)
        assertNotEquals(ast[0].toString(), "VariableDefinition(type='Int()', name=sum, initializer=Literal(value=hello))")
    }

    @Test
    fun string_declartion_equals_string() {
        val ast = this.parse("str language = \"koak\"")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "VariableDefinition(type='Str()', name=language, initializer=Literal(value=koak))")
    }

    @Test
    fun print_variable() {
        val ast = this.parse("print(koak)")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Print(expression=Grouping(expression=Variable(name=koak)))")
    }

    @Test
    fun print_literral() {
        val ast = this.parse("print(\"hello!\")")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Print(expression=Grouping(expression=Literal(value=hello!)))")
    }

    @Test
    fun function_definition() {
        val ast = this.parse("def test(x : int) : int 1 + 2 + x;")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Function(name=test, parameters=[Parameter(name=x, type=int)], body=[], returntype=ReturnValue(value=Expression(expression=Binary(left=Binary(left=Literal(value=1), operator=+, right=Literal(value=2)), operator=+, right=Variable(name=x))), type=Int()))")
    }

    @Test
    fun function_definition_multiple_statements_inside() {
        val ast = this.parse("""
            def test2(x : int) : int
                int x = 1 + 2 + x:
                x
            ;
            """)

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Function(name=test2, parameters=[Parameter(name=x, type=int)], body=[VariableDefinition(type='Int()', name=x, initializer=Binary(left=Binary(left=Literal(value=1), operator=+, right=Literal(value=2)), operator=+, right=Variable(name=x)))], returntype=ReturnValue(value=Expression(expression=Variable(name=x)), type=Int()))")
    }

    @Test
    fun function_definition_if_statement_inside() {
        val ast = this.parse("""
            def test3(x: int): int
                if x >= 3 then
                    3
            ;
            """)

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Function(name=test3, parameters=[Parameter(name=x, type=int)], body=[], returntype=ReturnValue(value=If(condition=Binary(left=Variable(name=x), operator=>=, right=Literal(value=3)), thenBranch=[Expression(expression=Literal(value=3))], elseBranch=[]), type=Int()))")
    }

    @Test(expected = Exception::class)
    fun function_definition_missing_type() {
        val ast = this.parse("def add(x: int, y): x + y;")
    }

    @Ignore
    @Test
    @Ignore
    fun function_compiles_and_output_15() {
        val ast = this.parse("""
            int x = 15
            print(x)
            """)

        assertTrue(ast.size == 2)
        assertEquals(ast[0].toString(), "VariableDefinition(type='Int()', name=x, initializer=Literal(value=15))")
        assertEquals(ast[1].toString(), "Print(expression=Grouping(expression=Variable(name=x)))")

        val compiler = Compiler("./tmp")

        val llfile = compiler.toLLFile(ast)
        val tempCompiledFile = createTempFile("output", ".exe")
        tempCompiledFile.deleteOnExit()
        val compiledFile = compiler.compile(llfile, tempCompiledFile.absolutePath)

        val out = execute(compiledFile.absolutePath)

        assertEquals(out, "15")

    }
}