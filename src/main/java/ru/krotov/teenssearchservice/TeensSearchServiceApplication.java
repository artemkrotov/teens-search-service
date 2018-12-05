package ru.krotov.teenssearchservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.krotov.teenssearchservice.configurations.TelegramConfiguration;
import ru.krotov.teenssearchservice.configurations.VkConfiguration;

@Import(value = {
		TelegramConfiguration.class, VkConfiguration.class
})
@EnableScheduling
@SpringBootApplication
public class TeensSearchServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TeensSearchServiceApplication.class).headless(false).run(args);
	}
}
