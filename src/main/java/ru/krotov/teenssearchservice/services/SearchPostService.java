package ru.krotov.teenssearchservice.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;

import java.util.List;

public interface SearchPostService {


	List<TelegramMessageDto> findMessages(List<String> groupDomains);
	List<TelegramMessageDto> findMessages(String groupDomain) throws ClientException, ApiException;

}
