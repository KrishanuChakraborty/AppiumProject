package org.example.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public class FirstAppiumTest {

    private AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(System.getProperty("deviceName", "Pixel_10_Pro"))
                .setApp(Paths.get("src/test/resources/apps/android-demo.apk")
                        .toAbsolutePath().toString())
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAdbExecTimeout(Duration.ofSeconds(60));

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        dismissCompatibilityDialogIfPresent();
    }

    private void dismissCompatibilityDialogIfPresent() {
        try {
            WebElement dontShowAgain = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("android:id/button1")));
            dontShowAgain.click();
        } catch (TimeoutException e) {
            // dialog didn't appear this run — fine
        }
    }

    @Test
    public void openLoginScreen() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Open the drawer
        driver.findElement(AppiumBy.accessibilityId("open menu")).click();

        // Click "Log In" inside the drawer
        WebElement loginMenuItem = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("menu item log in")));
        loginMenuItem.click();

        // Dump the login screen's source so we can grab the real field locators next
        System.out.println(driver.getPageSource());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}