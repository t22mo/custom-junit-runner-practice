import com.test.custom.annotations.*;
import com.test.custom.runner.CustomRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CustomRunner.class)
@RunGroup("A")
@TestOrder(TestOrder.OrderType.ASCEND)
public class TestCustomRunner {

    @Test
    @Repeat(10)
    @Group("A")
    public void test1() {
        int randValue;
        System.out.println("test1");

        randValue = (int) (Math.random() * 2);
        Assert.assertEquals(1, randValue);
    }

    @Test
    @Repeat(2)
    @Group("A")
    public void test2() {
        System.out.println("test2");
    }

    @Test
    @EstimateTime(9)
    @PrintTime
    @Group("B")
    public void test3() {
        for (int i = 0; i < 100000000; i++) {

        }
    }
}