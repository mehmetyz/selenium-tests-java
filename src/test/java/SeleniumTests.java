import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.opentest4j.AssertionFailedError;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.LogManager;

import org.slf4j.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTests {
    private String url = "https://laughing-mclean-4e5975.netlify.app/";
    private JavascriptExecutor js;
    private WebDriver driver = null;

    @BeforeEach
    void refreshWebSite(){

        try {

            TestUtil.info("Connect to driver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--window-size=1420,108");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");

            driver = new ChromeDriver(options);

            js = (JavascriptExecutor) driver;
        }
        catch (Exception e){
            TestUtil.error("Connection to driver error : \n\t" + e.getMessage());
            TestUtil.fail();
        }

        TestUtil.info("Connect to Website");
        driver.navigate().to(url);

    }

    private List<WebElement> todoList (){

        WebElement textField = null, addingButton = null;
        List<WebElement> todoList = null;

        try {
            TestUtil.info("Adding new TODO");

            textField = driver.findElement(By.id("addText"));
            addingButton = driver.findElement(By.id("addBtn"));
            textField.sendKeys("TestDemo1");
            addingButton.click();

            todoList = driver.findElements(By.xpath(".//*[contains(@id, 'todo')]"));
            assertTrue( todoList.size() > 0);
            assertEquals("TestDemo1", todoList.get(0).getText());

        }
        catch (Exception | AssertionFailedError e ){
            TestUtil.error("Cannot adding new TODO into list.");
            TestUtil.fail();
        }
        return todoList;
    }

    @Test
    @DisplayName("Testing connect to web site")
    @Order(1)
    public void testReachWebSite() {

        TestUtil.test("Test to Connect Website");
        TestUtil.info("Check Title ...");
        try {
                assertEquals("ToDo App", driver.getTitle());
            TestUtil.pass();
        }
        catch (AssertionFailedError e){
            TestUtil.error("Checking title error : \n\t" + e.getMessage());
            TestUtil.fail();
        }
    }


    @Test
    @DisplayName("Testing adding new TODO")
    @Order(2)
    public void testAddNewTODO(){
        TestUtil.test("Test to Add New TODO");

        TestUtil.info("Check text area for TODO");
        WebElement textField = null, addingButton = null;

        try {
            textField = driver.findElement(new By.ById("addText"));
            assertNotNull(textField);
            TestUtil.info("Text Area found");
        }
        catch (Exception ex){
            TestUtil.error("Text area not found: \n\t" +ex.getMessage() );
            TestUtil.fail();
        }

        TestUtil.info("Type the TODO");
        textField.sendKeys("TestDemo1");

        TestUtil.info("Add TODO into list");

        try {
            addingButton = driver.findElement(new By.ById("addBtn"));
            assertNotNull(addingButton);
        }
        catch (Exception | AssertionFailedError ex)
        {
            TestUtil.error("Add button did not found!");
            TestUtil.fail();
        }

        addingButton.click();

        List<WebElement> todoList = driver.findElements(By.xpath(".//*[contains(@id, 'todo')]"));

        try {

            assertTrue( todoList.size() > 0);
            assertEquals("TestDemo1", todoList.get(0).getText());
        }
        catch (AssertionFailedError e){
            TestUtil.error("The TODO did not added to list.");
            TestUtil.fail();
        }

        TestUtil.pass();
    }

    @Test
    @DisplayName("Testing set the TODO status is completed.")
    @Order(3)
    public void testCheckCompletedTODO(){
        TestUtil.test("Test to Change TODO status to COMPLETED!");
        List<WebElement> todoList = this.todoList();

        if (todoList.isEmpty())
            TestUtil.fail();

        TestUtil.info("Set status is COMPLETED!");
        WebElement todo = todoList.get(0);
        todo.click();

        try {

            assertEquals("app-block checked", todo.getAttribute("class"));
        }
        catch (AssertionFailedError e){
            TestUtil.error("The TODO status is not COMPLETED.");
            TestUtil.fail();
        }
        TestUtil.pass();
    }

    @Test
    @DisplayName("Testing remove TODO")
    @Order(4)
    public void testRemoveTODO(){

        List<WebElement> todoList = this.todoList();

        if (todoList.isEmpty())
            TestUtil.fail();

        todoList.get(0).findElement(By.className("remove")).click();

        todoList = driver.findElements(By.xpath(".//*[contains(@id, 'todo')]"));
        assertEquals(0, todoList.size());

        TestUtil.pass();
    }

    @Test
    @DisplayName("Testing using Local Storage")
    @Order(5)
    public void testLocalStorage(){

        TestUtil.test("Test to Check Local Storage");
        List<WebElement> todoList = this.todoList();

        if (todoList.isEmpty())
            TestUtil.fail();

        driver.navigate().refresh();

        String localStorageData = (String) js.executeScript(String.format(
                "return window.localStorage.getItem('%s');", "todos"));

        assertFalse(localStorageData.isEmpty());

        TestUtil.pass();

    }


    @AfterEach
    void end(){
        driver.quit();
    }
}
