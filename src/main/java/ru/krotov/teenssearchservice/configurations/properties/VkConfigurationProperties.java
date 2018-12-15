package ru.krotov.teenssearchservice.configurations.properties;

import lombok.Data;

@Data
public class VkConfigurationProperties {

	private Integer appId;
	private String token;
	private Integer messageBucketSize;

}
