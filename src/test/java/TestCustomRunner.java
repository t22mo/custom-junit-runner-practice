import com.test.custom.annotations.Repeat;
import com.test.custom.runner.CustomRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CustomRunner.class)
public class TestCustomRunner {

    @Test
    @Repeat(10)
    public void test1() {
        int randValue;
        System.out.println("test1");

        randValue = (int) (Math.random() * 2);
        Assert.assertEquals(1, randValue);
    }

    @Test
    @Repeat(2)
    public void test2() {
        System.out.println("test2");
    }
}