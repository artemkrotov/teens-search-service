package ru.krotov.teenssearchservice.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.krotov.teenssearchservice.web.clients.Bot;
import ru.krotov.teenssearchservice.web.clients.GroupsHelper;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.services.SearchPostService;
import ru.krotov.teenssearchservice.web.services.VkSessionService;

import java.io.IOException;
import java.util.List;

@RestController
public class SearchPostController {

	@Autowired
	private SearchPostService searchPostService;
	@Autowired
	private Bot bot;
	@Autowired
	private VkSessionService vkSessionService;

	@GetMapping(path = "/get", produces = "application/json")
	public List<MessageDto> get() {
		List<String> groupIds = GroupsHelper.groupIds;
		List<MessageDto> messages = searchPostService.findMessages(groupIds);
		bot.sendMsg("281490960", messages);
		return messages;
	}

	@PostMapping(path = "/send")
	public void get(String message) {
		List<String> groupIds = GroupsHelper.groupIds;
		searchPostService.findMessages(groupIds);
	}

	@GetMapping(path = "/session")
	public void session() throws IOException {
		vkSessionService.getToken();
	}

}
