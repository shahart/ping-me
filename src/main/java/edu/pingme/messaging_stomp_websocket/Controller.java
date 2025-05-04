package edu.pingme.messaging_stomp_websocket;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.util.HtmlUtils;

//import java.io.IOException;
//import java.util.concurrent.TimeUnit;

@RestController
//@RequestMapping("api")
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Value("${message.0}")
    String message0;

    @Value("${message.1}")
    String message1;

    @Value("${message.2}")
    String message2;

    @Value("${message.3}")
    String message3;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
    @GetMapping("ping/{text}")
    public ResponseEntity<Long> pingMe(@PathVariable("text") String text, HttpServletRequest request) throws Exception { //HelloMessage message) throws Exception {

        // IWATCH: 18.4   Mozilla/5.0 (iPhone; CPU iPhone OS 18_4   like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.4 Mobile/15E148 Safari/604.1
        // IPHONE: 18.4.1 Mozilla/5.0 (iPhone; CPU iPhone OS 18_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/136.0.7103.56 Mobile/15E148 Safari/604.1
        // PostMan: PostmanRuntime/7.43.0
        // EDGE: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Mobile Safari/537.36 Edg/135.0.0.0
        // FF: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:137.0) Gecko/20100101 Firefox/137.0
        // CHROME: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36
        // ANDROID GALAXY INTERNET:

        // if (text.length() == 1 && Character.isDigit(text.charAt(0))) {

        switch (text) {
            case "0" -> text = message0;
            case "1" -> text = message1;
            case "2" -> text = message2;
            case "3" -> text = message3;
        }

        log.info("ping/" + text + "; User-Agent: " + request.getHeader("User-Agent"));

        // silly protection. Can do also rate-limit-exception, ..
        if (request.getHeader("User-Agent").contains("18.4 ")) {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:\\Program Files\\AutoHotkey\\AutoHotkey.exe",
                    "C:\\repos\\ping-me\\script.ahk");
            processBuilder.inheritIO(); // to display output in the console
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // 0
            log.info("Process exited with code: " + exitCode);

            return new ResponseEntity<>( // success ? 1L : 0L,
                    (long) exitCode, HttpStatus.OK); // new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
        }

        log.warn("Invalid device");
        return new ResponseEntity<>(0L, HttpStatus.BAD_REQUEST);
    }
}