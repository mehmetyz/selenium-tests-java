import org.junit.jupiter.api.Assertions;

public class TestUtil {


    public static void test(String name){
        System.out.println("TEST: " + name);
    }
    public static void error(String log){
        System.out.println("Error: " + log);
    }

    public static void info(String log){
        System.out.println("Info: " + log);
    }

    public static void pass(){
        System.out.println("TEST PASSED");
    }

    public static void fail(){
        System.out.println("TEST FAILED");
        Assertions.fail();
    }
}
