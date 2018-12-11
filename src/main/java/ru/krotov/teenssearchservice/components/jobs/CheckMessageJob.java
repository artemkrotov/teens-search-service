package ru.krotov.teenssearchservice.components.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.utils.GroupIndexUtils;
import ru.krotov.teenssearchservice.components.clients.telegram.TelegramClientBot;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.services.VkSearchPostService;

import java.util.List;


// TODO: Проанализировать актуальность применения Spring Batch
@Component
@RequiredArgsConstructor
public class CheckMessageJob {

	private final VkSearchPostService vkSearchPostService;
	private TelegramClientBot telegramClientBot;

	// TODO: Вынести в конфиг
	@Scheduled(fixedDelayString = "${job.checkMessage.delay}")
	public void checkMessages () {
		// todosomething
		List<TelegramMessageDto> telegramMessageDtos = vkSearchPostService.findMessages(GroupIndexUtils.groupIds);
		telegramClientBot.sendMsg(telegramMessageDtos);
	}

}
