package edu.pingme.messaging_stomp_websocket;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Value("${message.0}")
    String message0;

    @Value("${message.1}")
    String message1;

    @Value("${message.2}")
    String message2;

    @Value("${recipient.source}")
    String recipientSource;

    @Value("${recipient.target}")
    String recipientTarget;

    long lastUsage = 0;

    ChromeDriver driver;

    @PostConstruct
    public void logConfig() {
        log.info("recipients: from '" + recipientSource + "' to '" + recipientTarget);
        driver = getChromeInstance(); // timeout is 60 sec
        log.info("Chrome is up.");
    }

    @GetMapping("ping/{text}")
    public ResponseEntity<Void> pingMe(@PathVariable("text") String text, HttpServletRequest request) {

        switch (text) {
            case "0" -> text = message0;
            case "1" -> text = message1;
            case "2" -> text = message2;
        }

        String device = getDevice(request.getHeader("User-Agent"));
        log.info("ping/" + device + "/" + text);

        String prefix = device + " message (low connectivity): "; // can prefix on new device, but Keep It Simple
        if (! text.contains("sanityCheck")) {
            if (System.currentTimeMillis() - lastUsage < 60_000) {
                prefix = "";
            }
            lastUsage = System.currentTimeMillis();
        }

        sendViaSelenium(prefix + text);
        // sendViaAHK(prefix + text);
        log.info("Done sending.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendViaAHK(String text) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "C:\\Program Files\\AutoHotkey\\AutoHotkey.exe",
                "C:\\repos\\ping-me\\script.ahk");
        Process process = processBuilder.start();
        int exitCode = process.waitFor(); // 0
        log.info("Process exited with code: " + exitCode);
    }

    public ChromeDriver getChromeInstance() {
        System.setProperty("webdriver.chrome.driver", "c:\\repos\\selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        ChromeDriver driver = new ChromeDriver(options);
        if (! Objects.requireNonNull(driver.getTitle()).contains("WhatsApp")) {
            throw new RuntimeException("Invalid Chrome's window's title: " + driver.getTitle());
        }
        return driver;
    }

    private void sendViaSelenium(String text) {
        WebElement webElement = driver.findElement(By.xpath("//*[contains(text(), '" +
                (!text.contains("sanityCheck") ? recipientTarget : recipientSource) + "')]"));
        webElement.click();

        // webElement = driver.findElement(By.xpath("//*[contains(text(), 'Type a message')]"));
        // this is the class id of the 1st element inside 'Type a message':
        webElement = driver.findElements(By.cssSelector(".selectable-text.copyable-text.x15bjb6t.x1n2onr6")).get(1); // By.className throws "Compound class names not permitted
        webElement.click();
        webElement.sendKeys(text);

        // instead of webElement.submit(); as it's not a form
        webElement = driver.findElement(By.cssSelector(".x1c4vz4f.x2lah0s.xdl72j9.xfect85.x1iy03kw.x1lfpgzf"));
        webElement.click();

        // back to source so response won't be marked as Read
        webElement = driver.findElement(By.xpath("//*[contains(text(), '" + recipientSource + "')]"));
        webElement.click();
    }

    private String getDevice(String userAgent) {
        if (userAgent.contains("iPhone")) {
            return userAgent.contains("CriOS") ? "iPhone" : "iWatch";
        }
        else if (userAgent.contains("Postman")) {
            return "Postman";
        }
        else if (userAgent.contains("Windows")) {
            return "Windows";
        }
        else if (userAgent.contains("Samsung")) {
            return "Samsung Android";
        }
        else if (userAgent.contains("Edg")) {
            return "Edge";
        }
        return "Some device";
    }
}