package ru.krotov.teenssearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.krotov.teenssearchservice.web.configurations.TelegramConfiguration;
import ru.krotov.teenssearchservice.web.configurations.VkConfiguration;

@Import(value = {
		TelegramConfiguration.class, VkConfiguration.class
})
@EnableScheduling
@SpringBootApplication
public class TeensSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeensSearchServiceApplication.class, args);
	}
}
