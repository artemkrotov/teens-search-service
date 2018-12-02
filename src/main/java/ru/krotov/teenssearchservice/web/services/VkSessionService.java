package ru.krotov.teenssearchservice.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.krotov.teenssearchservice.web.clients.VkSessionClient;
import ru.krotov.teenssearchservice.web.configurations.VkConfigurationProperties;

import java.io.IOException;

public class VkSessionService {

	@Autowired
	private VkSessionClient vkSessionClient;

	@Autowired
	private VkConfigurationProperties vkConfigurationProperties;

	public void getToken() throws IOException {
		vkSessionClient.getToken(vkConfigurationProperties.getAppId());
	}
}
