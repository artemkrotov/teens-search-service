package ru.krotov.teenssearchservice.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.utils.collections.CircleLinkedListWithLimit;
import ru.krotov.teenssearchservice.utils.GroupIndexUtils;
import ru.krotov.teenssearchservice.components.converters.WallPostFullMessageDtoConverter;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkSearchPostService implements SearchPostService {

	@Qualifier("wallPostFullFilterExecutor")
	private final Filter<WallPostFull> wallPostFullFilterExecutor;

	//TODO: Написать анализатор который решает как частво запускать Scheduled
	private final VkApiClient vkApiClient;
	private final VkConfigurationProperties vkConfigurationProperties;
	private final WallPostFullMessageDtoConverter wallPostFullMessageDtoConverter;
	//TODO: Временно
	private UserActor userActor;
	private CircleLinkedListWithLimit<String> circleLinkedListWithLimit =
			new CircleLinkedListWithLimit<>(GroupIndexUtils.groupIds.size() * 100); // TODO: Подумать как посчитать оптимальный размер

	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public List<TelegramMessageDto> findMessages(List<String> groupDomains) {
		return groupDomains.stream()
				.map(this::findMessages)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Override
	public List<TelegramMessageDto> findMessages(String groupDomain) {
		try {

			// TODO: Между запросами должна быть задержка, иначе API VK даст микробан
			TimeUnit.SECONDS.sleep(1);

			List<WallPostFull> wallPostFulls = vkApiClient.wall().get(userActor).count(100).domain(groupDomain).execute().getItems();
			return wallPostFulls.stream()
					.filter(this::isMessageUnique)
					.filter(wallPostFullFilterExecutor::filter)
					.map(wallPostFullMessageDtoConverter::convert)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (ApiException | ClientException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: Вынести в отдельный сервис и сделать сложную проверку
	private boolean isMessageUnique(WallPostFull message) {

		String text = message.getText();

		if (circleLinkedListWithLimit.contains(text)) {
			return false;
		}

		circleLinkedListWithLimit.add(text);
		return true;
	}
}
