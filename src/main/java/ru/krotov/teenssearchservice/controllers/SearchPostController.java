package ru.krotov.teenssearchservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krotov.teenssearchservice.components.clients.telegram.TelegramClientBot;
import ru.krotov.teenssearchservice.utils.GroupIndexUtils;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.services.SearchPostService;
import ru.krotov.teenssearchservice.services.VkSessionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchPostController {

	private final SearchPostService searchPostService;
	private final TelegramClientBot telegramClientBot;
	private final VkSessionService vkSessionService;

	@GetMapping(path = "/get", produces = "application/json")
	public List<TelegramMessageDto> get() {
		List<String> groupIds = GroupIndexUtils.groupIds;
		List<TelegramMessageDto> messages = searchPostService.findMessages(groupIds);
		telegramClientBot.sendMsg("281490960", messages);
		return messages;
	}

	@PostMapping(path = "/send")
	public void get(String message) {
		List<String> groupIds = GroupIndexUtils.groupIds;
		searchPostService.findMessages(groupIds);
	}

	@GetMapping(path = "/session")
	public void session() throws IOException {
		vkSessionService.getToken();
	}

}
