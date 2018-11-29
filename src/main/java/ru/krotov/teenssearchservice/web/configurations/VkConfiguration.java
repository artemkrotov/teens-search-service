package ru.krotov.teenssearchservice.web.configurations;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkConfiguration {

	@Bean
	VkApiClient vkApiClient () {
		return new VkApiClient(new HttpTransportClient());
	}
}
