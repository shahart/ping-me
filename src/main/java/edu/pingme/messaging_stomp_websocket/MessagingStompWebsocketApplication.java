package edu.pingme.messaging_stomp_websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class MessagingStompWebsocketApplication {

	private static final Logger log = LoggerFactory.getLogger(MessagingStompWebsocketApplication.class);

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MessagingStompWebsocketApplication.class);
		app.addListeners(appEvent -> {
			if (appEvent instanceof ApplicationPreparedEvent) {
				System.out.println("Config is 100% ?! Proceed ? [Y/n]");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					String response = br.readLine(); // "Y";
					if (! "Y".equals(response)) {
						System.exit(0);
					}
				} catch (/*IO*/Exception e) {
					e.printStackTrace();
				}
			}
		});
		context = app.run(args);
	}

	@Scheduled(fixedDelay = 60_000L)
	public void appTimeout() {
		long lastUsage = context.getBean(Controller.class).getLastUsage();
		if (System.currentTimeMillis() - lastUsage > TimeUnit.HOURS.toMillis(6)) {
			log.info("Timeout period reached and no messages have been received");
			context.close();
		}
	}

}
