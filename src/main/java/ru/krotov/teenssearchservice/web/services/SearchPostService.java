package ru.krotov.teenssearchservice.web.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.dto.UserDto;

import java.util.List;

public interface SearchPostService {


	List<MessageDto> findMessages(List<String> groupDomains);
	List<MessageDto> findMessages(String groupDomain) throws ClientException, ApiException;

}
