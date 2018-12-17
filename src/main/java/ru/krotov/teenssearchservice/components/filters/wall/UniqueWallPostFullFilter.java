package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.repository.MessageRepository;

import java.time.LocalDateTime;

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
		Message lastMessageByUserId = messageRepository.findLastMessageByUserId(wallPostFull.getOwnerId());// TODO: Проблема с записью от имени группы
		return lastMessageByUserId == null || lastMessageByUserId.getCreated().isBefore(LocalDateTime.now().minusHours(2L)); // TODO: Хардкод, постоянная инициализация
	}

	@Override
	public String getErrorMessage(WallPostFull wallPostFull) {
		return String.format("Message with id = %d is not unique", wallPostFull.getId());
	}

	@Override
	public int getOrder() {
		return 100;
	}
}
