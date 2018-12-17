package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.clients.UserClient;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.model.User;
import ru.krotov.teenssearchservice.repository.MessageRepository;
import ru.krotov.teenssearchservice.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Slf4j
@Component
@RequiredArgsConstructor
public class WallPostFullMessageConverter implements Converter<WallPostFull, Message> {

	private final UserXtrCountersUserConverter userXtrCountersUserConverter;
	private final UserClient userClient;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;

	@Override
	public Message convert(WallPostFull wallPostFull) {

		Message message = null;
		try {
			Integer userVkId = getVkId(wallPostFull);

			User user = userRepository.findByVkId(userVkId);

			if (user == null) {
				UserXtrCounters userXtrCounters = userClient.getUser(userVkId);
				user = userXtrCountersUserConverter.convert(userXtrCounters);
			}

			if (user == null) {
				throw new RuntimeException("User hadn't be null!");
			}

			message = new Message();
			message.setUser(user);
			message.setCreated(LocalDateTime.ofInstant(Instant.ofEpochMilli((long) wallPostFull.getDate() * 1000), ZoneId.systemDefault()));
			user.getMessages().add(message);
			message.setText(wallPostFull.getText());

			messageRepository.save(message);

			return message;
		} catch (UserNotFoundException e) {
			log.error(e.getMessage());
		} catch (InvalidUserException e) {
			log.warn(e.getMessage());
		} catch (Exception e) {
			log.error("Message: {}, Error {}", message, e.getMessage());
		}

		return null;
	}


	// TODO: Запись может быть создана от имеени группы (разобраться)
	private Integer getVkId(WallPostFull wallPostFull) {
		return wallPostFull.getFromId();
	}


}
