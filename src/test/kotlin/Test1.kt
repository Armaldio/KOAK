import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class FilesTest {

    fun parse(text: String): AST {
        val scanner = Lexer(text)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)

        val statements = parser.parse()
        val ast = AST()
        statements.filterNotNullTo(ast)

        return ast
    }

    @Test
    fun test() {

        val compiler = Compiler("example/test.koak")
        val ast = compiler.getAST()

        assertEquals(ast.size, 2)
        assertTrue(ast[0] is Stmt.Function)

        //quickly test the output
        assertEquals("Function(name=add, parameters=[Parameter(name=x, type=int), Parameter(name=y, type=int)], body=Binary(left=Variable(name=x), operator=+, right=Variable(name=y)))", ast[0].toString())
    }

    @Test
    fun int_declaration_equals_3() {
        val ast = this.parse("int a = 3")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Var(type='int', name=a, initializer=Literal(value=3))")
    }

    @Test
    fun int_declaration_equals_operation() {
        val ast = this.parse("int a = 3 + 3")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Var(type='int', name=a, initializer=Binary(left=Literal(value=3), operator=+, right=Literal(value=3)))")

    }

    @Test
    fun int_declaration_equals_functioncall() {
        val ast = this.parse("int sum = add(10, 5);")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Var(type='int', name=sum, initializer=Call(callee=Variable(name=add), paren=), arguments=[Literal(value=10), Literal(value=5)]))")
    }

    @Ignore
    @Test
    fun int_declaration_equals_string() {
        val ast = this.parse("int sum = \"hello\"")

        assertTrue(ast.size == 1)
        assertNotEquals(ast[0].toString(), "Var(type='int', name=sum, initializer=Literal(value=hello))")
    }

    @Test
    fun string_declartion_equals_string() {
        val ast = this.parse("string language = \"koak\"")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Var(type='string', name=language, initializer=Literal(value=koak))")
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
        val ast = this.parse("def add(x: int, y: int): x + y;")

        assertTrue(ast.size == 1)
        assertEquals(ast[0].toString(), "Function(name=add, parameters=[Parameter(name=x, type=int), Parameter(name=y, type=int)], body=Binary(left=Variable(name=x), operator=+, right=Variable(name=y)))")
    }

    @Test
    fun function_definition_missing_type() {
        val ast = this.parse("def add(x: int, y): x + y;")

        assertTrue(ast.size == 0)
    }
}