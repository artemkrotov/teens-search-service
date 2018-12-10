package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramUserDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class WallPostFullMessageDtoConverter implements Converter<WallPostFull, TelegramMessageDto> {

	private final UserXtrCountersUserDtoConverter userDtoConverter;
	//TODO: Думаю здесь это лишнее
	private final VkApiClient vkApiClient;
	private final VkConfigurationProperties vkConfigurationProperties;
	//TODO: Временно
	private UserActor userActor;

	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public TelegramMessageDto convert(WallPostFull wallPostFull) {

		try {
			UserXtrCounters realUser = getRealUser(wallPostFull);
			TelegramUserDto telegramUserDto = userDtoConverter.convert(realUser);

			TelegramMessageDto telegramMessageDto = new TelegramMessageDto();
			telegramMessageDto.setTelegramUserDto(telegramUserDto);
			telegramMessageDto.setMessage(wallPostFull.getText());
			return telegramMessageDto;
		} catch (UserNotFoundException e) {
			log.error(e.getMessage());
			return null;
		} catch (InvalidUserException e) {
			return null;
		}
	}

	// TODO Унести в сервис
	private UserXtrCounters getRealUser(WallPostFull wallPostFull) {

		try {
			// TODO: Запись может быть создана от имеени группы
			Integer fromId = wallPostFull.getFromId(); // TODO: Разобраться и переименовать

			TimeUnit.MILLISECONDS.sleep(200);

			List<UserXtrCounters> userXtrCounters = vkApiClient
					.users()
					.get(userActor).userIds(String.valueOf(fromId)).fields(Arrays.asList(UserField.values()))
					.execute();

			return userXtrCounters.stream()
					.findAny()
					.orElseThrow(() -> new UserNotFoundException(String.format("User from wallPost with id = %d wasn't founded!", wallPostFull.getId())));
		} catch (Exception e) {
			throw new UserNotFoundException(String.format("User wasn't founded! Reason: %s", e.getMessage()));
		}
	}
}
