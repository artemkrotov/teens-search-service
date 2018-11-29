package ru.krotov.teenssearchservice.web.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.web.clients.WomenNames;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.dto.UserDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VkSearchPostService implements SearchPostService {

	//TODO: Временно
	private UserActor userActor = new UserActor(6764102, "a2255484130c74ee2cc0e6b192fb8f0b397f79d5fba2bceb092bec7a76fb8675bb3ff5f3ec89f5300e1d8");

	@Autowired
	private VkApiClient vkApiClient;

	@Override
	public List<MessageDto> findMessages(List<String> groupDomains) {
		return groupDomains.stream()
				.map(this::findMessages)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	// TODO: Между запросами должна быть задержка, иначе API VK даст микробан
	@Override
	public List<MessageDto> findMessages(String groupDomain) {
		try {
			List<WallPostFull> wallPostFulls = vkApiClient.wall().get(userActor).domain(groupDomain).execute().getItems();
			return wallPostFulls.stream()
					.map(this::makeMessageDto)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (ApiException | ClientException e) {
			throw new RuntimeException(e);
		}
	}

	private MessageDto makeMessageDto (WallPostFull wallPostFull) {

		UserXtrCounters author = this.getRealAuthor(wallPostFull);

		if (!WomenNames.isWoman(author.getFirstName())) { // TODO: Сделать Бин
			return null;
		}

		UserDto userDto = new UserDto();
		userDto.setFullName(author.getFirstName() + " " + author.getLastName());
		userDto.setInstagram(author.getInstagram());
		userDto.setVk("https://vk.com/" + author.getId());

		MessageDto messageDto = new MessageDto();
		messageDto.setUserDto(userDto);
		messageDto.setMessage(wallPostFull.getText());

		return messageDto;
	}

	private UserXtrCounters getRealAuthor(WallPostFull wallPostFull) {

		try {
			// TODO: Запись может быть создана от имеени группы
			Integer fromId = wallPostFull.getFromId(); // TODO: Разобраться и переименовать

			List<UserXtrCounters> userXtrCounters = vkApiClient
					.users()
					.get(userActor).userIds(String.valueOf(fromId)).fields(Arrays.asList(UserField.values()))
					.execute();

			return userXtrCounters.stream()
					.findAny()
					.orElseThrow(null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
}
