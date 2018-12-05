package ru.krotov.teenssearchservice.web.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.core.utils.GroupIndexUtils;
import ru.krotov.teenssearchservice.web.clients.telegram.TelegramClientBot;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.services.VkSearchPostService;

import java.util.List;


// TODO: Проанализировать актуальность применения Spring Batch
@Component
@RequiredArgsConstructor
public class CheckMessageJob {

	private final VkSearchPostService vkSearchPostService;
	private final TelegramClientBot telegramClientBot;

	// TODO: Вынести в конфиг
	@Scheduled(fixedDelayString = "${job.checkMessage.delay}")
	public void checkMessages () {
		// todosomething
		List<MessageDto> messageDtos = vkSearchPostService.findMessages(GroupIndexUtils.groupIds);
		telegramClientBot.sendMsg("281490960", messageDtos);
	}

}
