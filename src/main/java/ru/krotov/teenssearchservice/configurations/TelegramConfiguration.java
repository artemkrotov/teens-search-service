package ru.krotov.teenssearchservice.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.krotov.teenssearchservice.components.clients.telegram.TelegramClientBot;
import ru.krotov.teenssearchservice.configurations.properties.DefaultBotOptionsConfigurationProperties;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
public class TelegramConfiguration {

	@Bean
	TelegramBotsApi telegramBotsApi() {
		ApiContextInitializer.init();
		return new TelegramBotsApi();
	}

	@Bean
	@ConfigurationProperties(prefix = "telegram.default-bot-options")
	DefaultBotOptionsConfigurationProperties defaultBotOptionsConfigurationProperties() {
		return new DefaultBotOptionsConfigurationProperties();
	}

	@Bean
	@Profile("proxy")
	public DefaultBotOptions defaultBotOptionsWithProxy(DefaultBotOptionsConfigurationProperties defaultBotOptionsConfigurationProperties) {

		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(
						defaultBotOptionsConfigurationProperties.getProxyUser(),
						defaultBotOptionsConfigurationProperties.getProxyPassword().toCharArray());
			}
		});

		DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
		defaultBotOptions.setProxyHost(defaultBotOptionsConfigurationProperties.getProxyHost());
		defaultBotOptions.setProxyPort(defaultBotOptionsConfigurationProperties.getProxyPort());
		defaultBotOptions.setProxyType(defaultBotOptionsConfigurationProperties.getProxyType());
		return defaultBotOptions;
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultBotOptions defaultBotOptions() {
		return ApiContext.getInstance(DefaultBotOptions.class);
	}

	@Bean
	public TelegramClientBot bot(TelegramBotsApi telegramBotsApi, DefaultBotOptions defaultBotOptions,
								 DefaultBotOptionsConfigurationProperties defaultBotOptionsConfigurationProperties) {
		try {
			TelegramClientBot telegramClientBot = new TelegramClientBot(defaultBotOptions, defaultBotOptionsConfigurationProperties);
			telegramBotsApi.registerBot(telegramClientBot);
			return telegramClientBot;
		} catch (TelegramApiRequestException e) {
			throw new RuntimeException(e);
		}
	}
}
