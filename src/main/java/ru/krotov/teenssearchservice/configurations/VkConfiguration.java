package ru.krotov.teenssearchservice.configurations;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.components.clients.vk.VkSessionClient;
import ru.krotov.teenssearchservice.services.VkSessionService;

@Configuration
public class VkConfiguration {

	@Bean
	VkApiClient vkApiClient () {
		return new VkApiClient(new HttpTransportClient());
	}

	@Bean
	UserActor getUserActor() {
		return new UserActor(vkConfigurationProperties().getAppId(), vkConfigurationProperties().getToken());
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
