import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FilesTest {

    @Test
    fun testsWork() {
        val compiler = Compiler("example/test.koak")
        val ast = compiler.compile()

        assertEquals(ast.size, 2)
        assertTrue(ast[0] is Stmt.Function)

        //quickly test the output
        assertEquals("Function(name=add, parameters=[Parameter(name=x, type=int), Parameter(name=y, type=int)], body=Binary(left=Variable(name=x), operator=+, right=Variable(name=y)))", ast[0].toString())
    }
}