package ru.krotov.teenssearchservice.configurations.properties;

import lombok.Data;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Data
public class DefaultBotOptionsConfigurationProperties {

	private String name;
	private String token;

	private String proxyHost;
	private Integer proxyPort;

	private DefaultBotOptions.ProxyType proxyType;

	private String proxyUser;
	private String proxyPassword;

}
