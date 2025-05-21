import com.lambdatest.tunnel.Tunnel;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class WindowsChrome {
    private RemoteWebDriver driver;

    @BeforeTest
    public void setup() throws Exception {
        // LambdaTest credentials
        String username = "belalahmad";
        String authkey = "LT_7VDCei73IDbSY28Yxz9fbocgnw9Ja4ryhwGP4HccHy967Rc";
//        String username = System.getenv("LT_USERNAME");
//        String authkey = System.getenv("LT_ACCESS_KEY");
        String hub = "hub.lambdatest.com/wd/hub";
//        Tunnel t;

        // Set ChromeOptions with platform and browser version
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");

        // Set LambdaTest specific options
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("build", "github action");
        ltOptions.put("project", "Belal");
        ltOptions.put("w3c", true); // Ensure W3C compliance
//        ltOptions.put("tunnel", true); // Enable the tunnel
//        ltOptions.put("tunnelName", "belal"); // Specify the tunnel name
        ltOptions.put("console", true); // Enable console logs
        ltOptions.put("network", true); // Enable network logs
        ltOptions.put("debug", true); // Enable debugging if needed

//        // Start the LambdaTest tunnel
//        t = new Tunnel();
//        HashMap<String, String> options = new HashMap<>();
//        options.put("user", username);
//        options.put("key", authkey);
//        options.put("tunnelName","belal");
//        t.start(options); // Starting the tunnel

        // Add ltOptions to ChromeOptions
        browserOptions.setCapability("LT:Options", ltOptions);

        // Construct the URL for LambdaTest remote WebDriver
        String remoteUrl = "https://" + username + ":" + authkey + "@" + hub;

        // Initialize RemoteWebDriver with ChromeOptions
        driver = new RemoteWebDriver(new URL(remoteUrl), browserOptions);
    }

    @Test
    public void testFormSubmission() throws InterruptedException, IOException {
        // Navigate to the URL for testing
        driver.get("https://the-internet.herokuapp.com/geolocation");
        takeScreenshot("first scrrenshot");

        // Validate the page title
        String pageTitle = driver.getTitle();
        System.out.println("Page Title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("The Internet"));


        // Click the "Where am I?" button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement whereAmIButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"content\"]/div/button")));
        whereAmIButton.click();
        System.out.println("clicked");


        // Wait for the geolocation result
        Thread.sleep(10000); // Adjust the sleep time as needed
    }

    public void takeScreenshot(String fileName) throws IOException {

        File srcFile = driver.getScreenshotAs(OutputType.FILE);

        FileHandler.copy(srcFile, new File("./" + fileName + ".png"));

    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser after tests
        }

        // Stop the LambdaTest tunnel
//        Tunnel.stop(); // Make sure to stop the tunnel after the test

    }
}
