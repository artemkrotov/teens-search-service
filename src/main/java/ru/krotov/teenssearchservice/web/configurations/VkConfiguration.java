package ru.krotov.teenssearchservice.web.configurations;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.krotov.teenssearchservice.web.clients.VkSessionClient;
import ru.krotov.teenssearchservice.web.services.VkSessionService;

@Configuration
public class VkConfiguration {

	@Bean
	VkApiClient vkApiClient () {
		return new VkApiClient(new HttpTransportClient());
	}

	@Bean
	@ConfigurationProperties(prefix = "vk")
	VkConfigurationProperties vkConfigurationProperties () {
		return new VkConfigurationProperties();
	}

	@Bean
	VkSessionClient vkSessionClient () {
		return new VkSessionClient();
	}

	@Bean
	VkSessionService vkSessionService() {
		return new VkSessionService();
	}
}
