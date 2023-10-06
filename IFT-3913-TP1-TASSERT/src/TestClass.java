import org.junit.jupiter.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestClass {

    @Test
    public void testAddition() {
        int result = 2 + 3;
        Assert.assertEquals(5, result);
    }

    @Test
    public void testSubtraction() {
        int result = 5 - 3;
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testDivision() {
        int result = 10 / 2;
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testNotNull() {
        String text = "Hello, World!";
        Assert.assertNotNull(text);
    }

    @Test
    public void testDivideByZero() {
        try {
            int result = 10/0;

            Assert.fail("Expected ArithmeticException, but no exception was thrown");
        } catch (ArithmeticException e) {

            // Expected exception; the test passes
        }
    }
}
