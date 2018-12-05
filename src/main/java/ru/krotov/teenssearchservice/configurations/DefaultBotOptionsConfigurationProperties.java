package ru.krotov.teenssearchservice.configurations;

import lombok.Data;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Data
public class DefaultBotOptionsConfigurationProperties {

	private String proxyHost;
	private Integer proxyPort;
	private DefaultBotOptions.ProxyType proxyType;
	private String proxyUser;
	private String proxyPassword;

}
