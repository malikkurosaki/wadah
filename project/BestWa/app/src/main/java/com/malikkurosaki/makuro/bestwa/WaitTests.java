package com.malikkurosaki.makuro.bestwa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WaitTests {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {

        // set up appium and tell from where it can install the apk file from
        // computer to device
        File appDir = new File("E:\\AppiumDemo-master\\AppiumDemo-master\\apps");
        File app = new File(appDir, "com.whatsapp.apk");
        // Very important properties you need for Appium to work, change as per
        // SDK and device name
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device", "Android");
        capabilities.setCapability("deviceName", "Huawei MediaPad T1");
        capabilities.setCapability("platformName", "Android");
        // You need to have this sdk installed for Appium to work
        capabilities.setCapability("platformVersion", "4.3.1");
        capabilities.setCapability("app", app.getAbsolutePath());
        // The URL where the hub will start
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),
                capabilities);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void whatsAppinstall() {
        // Find and click the accept button

    }

}

