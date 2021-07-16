import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtil {

    private static final Logger logger = LoggerFactory.getLogger("Selenium Testing");
    public static void test(String name){
        logger.info ("TEST : "+name);
    }
    public static void error(String log){
       logger.error (log);
    }

    public static void info(String log){
        logger.info ( log);
    }

    public static void pass(){
        logger.trace ("TEST PASSED");
    }

    public static void fail(){
        logger.error ("TEST FAILED");
        Assertions.fail();
    }
}
