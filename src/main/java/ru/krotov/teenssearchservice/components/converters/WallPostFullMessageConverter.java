package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.model.User;
import ru.krotov.teenssearchservice.repository.MessageRepository;
import ru.krotov.teenssearchservice.services.UserService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Slf4j
@Component
@RequiredArgsConstructor
public class WallPostFullMessageConverter implements Converter<WallPostFull, Message> {

	private final UserService userService;
	private final MessageRepository messageRepository;

	@Override
	@Transactional
	public Message convert(WallPostFull wallPostFull) {

		Message message = null;
		try {
			Integer userVkId = getVkId(wallPostFull);
			User user = userService.getUser(userVkId);
			message = new Message();
			message.setId(wallPostFull.getId());
			message.setUser(user);
			message.setCreated(LocalDateTime.ofInstant(Instant.ofEpochMilli((long) wallPostFull.getDate() * 1000), ZoneId.systemDefault()));
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
