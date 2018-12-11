package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.repository.MessageRepository;

import java.time.LocalDateTime;

@Component
public class UniqueWallPostFullFilter extends AbstractWallPostFullFilter {

	private final MessageRepository messageRepository;

	public UniqueWallPostFullFilter(@Qualifier("wallPostFullFilterExecutor") Filter<WallPostFull> filter, MessageRepository messageRepository) {
		super(filter);
		this.messageRepository = messageRepository;
	}

	// TODO: Может работать долго
	@Override
	public boolean filter(WallPostFull wallPostFull) {
		Message lastMessageByUserId = messageRepository.findLastMessageByUserId(wallPostFull.getOwnerId());// TODO: Проблема с записью от имени группы
		return lastMessageByUserId == null || lastMessageByUserId.getCreated().isBefore(LocalDateTime.now().minusHours(2L)); // TODO: Хардкод, постоянная инициализация
	}
}
