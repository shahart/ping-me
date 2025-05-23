package edu.pingme.messaging_stomp_websocket;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

    @Value("${recipientElementId}")
    String recipientElementId;

    @Value("${password}")
    String password;

    long lastUsage = System.currentTimeMillis();

    ChromeDriver driver;
//  FirefoxDriver firefoxDriver;

    Map<String, Integer> success = new ConcurrentHashMap<>();
    Map<String, Integer> error = new ConcurrentHashMap<>();

    public long getLastUsage() {
        return lastUsage;
    }

    @PostConstruct
    public void logConfig() {
        log.info("Password: " + password);
        log.info("recipients: from '" + recipientSource + "' to default '" + recipientTarget);
        log.info("Checking for configs... Wait, this might take 2 minutes"); // minimum is 2 seconds
        driver = getChromeInstance(); // timeout is 60 sec
        // firefoxDriver = getFirefoxInstance();
        pingMe("", "sanityCheck", null);
        log.info("Browser is up.");
    }

    @PostMapping("ping/{recipient}")
    public ResponseEntity<Void> pingMe(
            @PathVariable("recipient") String recipient,
            @RequestBody String text,
            HttpServletRequest request) {

        // another quick and dirty - assuming there's no contact name = Null
        if (recipient.equalsIgnoreCase("null")) {
            recipient = "";
        }

        switch (text) {
            case "^0$" -> text = message0;
            case "^1$" -> text = message1;
            case "^2$" -> text = message2;
            case "^3$" -> text = "Like";
            case "^4$" -> text = "Please repeat, message lost.";
            default -> {
                if (! text.contains("sanityCheck")) {
                    log.info("Encrypted text: " + text);
                    text = new String(Base64.getDecoder().decode(text));
                    text = Utils.deEnCrypt(text, password);
                }
            }
        }

        String browser = request == null ? "AppSanityCheck" : Utils.getBrowser(request.getHeader("User-Agent"));

        log.info("Ping using browser '{}' to '{}' with text '{}' " +
                        "from {}",
                browser, (recipient.isEmpty() ? recipientTarget : recipient), text,
                (request == null ? "unknown" : (request.getRemoteHost() + "/ " + request.getHeader("x-forwarded-for"))));

        String prefix = browser + " message (low connectivity): "; // can prefix on new browser, but Keep It Simple
        if (! text.contains("sanityCheck")) {
            if (System.currentTimeMillis() - lastUsage < TimeUnit.MINUTES.toMillis(10) || ! recipient.isEmpty()) {
                prefix = "";
            }
            lastUsage = System.currentTimeMillis();
        }

        // sendViaFirefox(recipient, prefix + text); // TODO?
        if (! sendViaSelenium(recipient, prefix + text)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // sendViaAHK(prefix + text);
        log.info("Done sending.");

        if (! text.contains("sanityCheck")) {
            int prev = success.getOrDefault(recipient, 0);
            success.put(recipient, prev + 1);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendViaAHK(String recipient, String text) throws Exception {
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

    private boolean sendViaSelenium(String recipient, String text) {
        try {
            WebElement webElement = driver.findElement(By.xpath("//*[contains(text(), '" +
                    (! text.contains("sanityCheck") ? (recipient.isEmpty() ? recipientTarget : recipient) : recipientSource) + "')]"));
            webElement.click();

            // webElement = driver.findElement(By.xpath("//*[contains(text(), 'Type a message')]"));
            // this is the class id of the 1st element inside 'Type a message':
            webElement = driver.findElements(By.cssSelector(".selectable-text.copyable-text." + recipientElementId)).get(1); // By.className throws "Compound class names not permitted
            webElement.click();
            webElement.sendKeys(text);

            // instead of webElement.submit(); as it's not a form
            webElement = driver.findElement(By.cssSelector(".x1c4vz4f.x2lah0s.xdl72j9.xfect85.x1iy03kw.x1lfpgzf"));
            webElement.click();

            // back to source so response won't be marked as Read
            webElement = driver.findElement(By.xpath("//*[contains(text(), '" + recipientSource + "')]"));
            webElement.click();
            return true;
        }
        catch (NoSuchElementException e2) {
            log.error("NoSuchElementException");
            if (! text.contains("sanityCheck")) {
                int prev = error.getOrDefault(recipient, 0);
                error.put(recipient, prev + 1);
            }
            return false;
        }
        catch (Exception e) { // maybe NoSuchElementException ?!
            if (! text.contains("sanityCheck")) {
                int prev = error.getOrDefault(recipient, 0);
                error.put(recipient, prev + 1);
            }
            throw e;
        }
    }

//    public FirefoxDriver getFirefoxInstance() {
//        return null; // driver;
//    }

//    private void sendViaFirefox(String recipient, String text) {
//    }

    @PreDestroy
    public void dumpStats() {
        log.info("successes: " + success);
        if (! error.isEmpty()) {
            log.info("errors: " + error);
        }
    }

}