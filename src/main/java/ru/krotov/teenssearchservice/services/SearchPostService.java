package ru.krotov.teenssearchservice.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.krotov.teenssearchservice.model.Message;

import java.util.List;

public interface SearchPostService {


	List<Message> findMessages(List<String> groupDomains);
	List<Message> findMessages(String groupDomain) throws ClientException, ApiException;

}
