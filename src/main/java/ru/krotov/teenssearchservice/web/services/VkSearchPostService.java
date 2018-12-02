package ru.krotov.teenssearchservice.web.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.web.clients.CircleLinkedListWithLimit;
import ru.krotov.teenssearchservice.web.clients.WomenNames;
import ru.krotov.teenssearchservice.web.configurations.VkConfigurationProperties;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.dto.UserDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VkSearchPostService implements SearchPostService {

	//TODO: Временно
	private UserActor userActor;

	//TODO: Написать анализатор который решает как частво запускать Scheduled
	@Autowired
	private VkApiClient vkApiClient;
	@Autowired
	private VkConfigurationProperties vkConfigurationProperties;

	private CircleLinkedListWithLimit <Integer> circleLinkedListWithLimit = new CircleLinkedListWithLimit<>(1000); // TODO: Подумать как посчитать оптимальный размер


	@PostConstruct
	void init () {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public List<MessageDto> findMessages(List<String> groupDomains) {
		return groupDomains.stream()
				.map(this::findMessages)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private int waitTime = 50;


	private synchronized void waitTime (int waitTime) {
		try {
			this.wait(waitTime);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}


	// TODO: Между запросами должна быть задержка, иначе API VK даст микробан
	@Override
	public List<MessageDto> findMessages(String groupDomain) {
		try {
			waitTime(waitTime);
			List<WallPostFull> wallPostFulls = vkApiClient.wall().get(userActor).count(6).domain(groupDomain).execute().getItems();
			return wallPostFulls.stream()
					.filter(message -> {
						Integer id = message.getId();

						if (circleLinkedListWithLimit.contains(id)) {
							return false;
						}

						circleLinkedListWithLimit.add(id);
						return true;

					})
					.map(this::makeMessageDto)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (ApiException | ClientException e) {
			throw new RuntimeException(e);
		}
	}


	private MessageDto makeMessageDto (WallPostFull wallPostFull) {

		UserXtrCounters author = this.getRealAuthor(wallPostFull);

		//TODO: Костыль
		if (author == null) {
			log.error(wallPostFull.toString());
			return null;
		}

		if (!WomenNames.isWoman(author.getFirstName())) { // TODO: Сделать Бин
	//		return null;
		}

		UserDto userDto = new UserDto();
		userDto.setFullName(author.getFirstName() + " " + author.getLastName());
		userDto.setInstagram(author.getInstagram());
		userDto.setVk("https://vk.com/" + author.getDomain());
		BaseObject city = author.getCity();
		if (city != null) {
			userDto.setCity(city.getTitle());
		}
		userDto.setAgeAndTodayBirthDay(author.getBdate());
		userDto.setPhotoUrl(author.getPhoto400Orig());

		MessageDto messageDto = new MessageDto();
		messageDto.setUserDto(userDto);
		messageDto.setMessage(wallPostFull.getText());

		return messageDto;
	}

	private UserXtrCounters getRealAuthor(WallPostFull wallPostFull) {

		try {
			// TODO: Запись может быть создана от имеени группы
			Integer fromId = wallPostFull.getFromId(); // TODO: Разобраться и переименовать

			waitTime(waitTime);
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
