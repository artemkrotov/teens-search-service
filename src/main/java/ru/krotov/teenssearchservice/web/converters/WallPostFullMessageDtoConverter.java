package ru.krotov.teenssearchservice.web.converters;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.configurations.VkConfigurationProperties;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.dto.UserDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class WallPostFullMessageDtoConverter implements Converter<WallPostFull, MessageDto> {

	private final UserXtrCountersUserDtoConverter userDtoConverter;
	//TODO: Думаю здесь это лишнее
	private final VkApiClient vkApiClient;
	//TODO: Временно
	private UserActor userActor;
	private final VkConfigurationProperties vkConfigurationProperties;
	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public MessageDto convert(WallPostFull wallPostFull) {
		UserXtrCounters author = getRealAuthor(wallPostFull);
		UserDto userDto = userDtoConverter.convert(author);
		MessageDto messageDto = new MessageDto();
		messageDto.setUserDto(userDto);
		messageDto.setMessage(wallPostFull.getText());
		return messageDto;
	}

	// TODO Унести в сервис
	private UserXtrCounters getRealAuthor(WallPostFull wallPostFull) {

		try {
			// TODO: Запись может быть создана от имеени группы
			Integer fromId = wallPostFull.getFromId(); // TODO: Разобраться и переименовать

			TimeUnit.MILLISECONDS.sleep(100);
			List<UserXtrCounters> userXtrCounters = vkApiClient
					.users()
					.get(userActor).userIds(String.valueOf(fromId)).fields(Arrays.asList(UserField.values()))
					.execute();

			return userXtrCounters.stream()
					.findAny()
					.orElseThrow(null);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
