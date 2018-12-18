package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.repository.MessageRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class UniqueWallPostFullFilter extends AbstractWallPostFullFilter {

	private final MessageRepository messageRepository;

	public UniqueWallPostFullFilter(WallPostFullFilterExecutor filter, MessageRepository messageRepository) {
		super(filter);
		this.messageRepository = messageRepository;
	}

	// TODO: Может работать долго
	@Override
	public boolean filter(WallPostFull wallPostFull) {
		Integer useId = wallPostFull.getFromId(); // TODO Зависи от имени группы
		Message lastMessageByUserId = messageRepository.findLastMessageByUserId(useId);// TODO: Проблема с записью от имени группы

		if (lastMessageByUserId == null) {
			return true;
		}

		boolean isNotEqualMessage = !lastMessageByUserId.getId().equals(wallPostFull.getId());
		boolean isUserNotTimeOuted = lastMessageByUserId.getCreated().isBefore(LocalDateTime.now().minusMinutes(30)); // TODO: Хардкод, постоянная инициализация
		return isNotEqualMessage && isUserNotTimeOuted;
	}

	@Override
	public String getErrorMessage(WallPostFull wallPostFull) {
		return String.format("Message with id = %d is not unique", wallPostFull.getId());
	}

	@Override
	public int getOrder() {
		return 1000;
	}
}
