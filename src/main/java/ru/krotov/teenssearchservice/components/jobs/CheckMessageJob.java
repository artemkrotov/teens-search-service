package ru.krotov.teenssearchservice.components.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.converters.MessageTelegramMessageDtoConverter;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.utils.GroupIndexUtils;
import ru.krotov.teenssearchservice.components.clients.telegram.TelegramClientBot;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.services.VkSearchPostService;

import java.util.List;
import java.util.stream.Collectors;


// TODO: Проанализировать актуальность применения Spring Batch
@Component
@RequiredArgsConstructor
public class CheckMessageJob {

	private final VkSearchPostService vkSearchPostService;
	private final TelegramClientBot telegramClientBot;
	private final MessageTelegramMessageDtoConverter messageTelegramMessageDtoConverter;

	// TODO: Вынести в конфиг
	@Scheduled(fixedDelayString = "${job.checkMessage.delay}")
	public void checkMessages () {
		// todosomething
		List<Message> messages = vkSearchPostService.findMessages(GroupIndexUtils.groupIds);
		List<TelegramMessageDto> telegramMessageDtos = messages.stream().map(messageTelegramMessageDtoConverter::convert).collect(Collectors.toList());
		telegramClientBot.sendMsg(telegramMessageDtos);
	}

}
