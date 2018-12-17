package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.components.filters.UserFilterExecutor;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LastPhotoDateFilter extends AbstractUserFilter {

	private static final int DAYS_AFTER_FIRST_PHOTO = 30 * 6;
	private VkApiClient vkApiClient;

	private UserActor userActor;

	public LastPhotoDateFilter(UserFilterExecutor filter) {
		super(filter);
	}

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {
		try {

			if (userXtrCounters.isBlacklisted()) {
				return true;
			}

			Integer fromId = userXtrCounters.getId();

			TimeUnit.MILLISECONDS.sleep(200);

			GetResponse response = vkApiClient.photos()
					.get(userActor).albumId("profile").rev(false).ownerId(fromId)
					.execute();

			Photo profilePhoto = response.getItems().stream()
					.findFirst()
					.orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d have not profile photos!", fromId)));

			//TODO: DateConverterUtil
			LocalDate firstPhotoDate = new Date(profilePhoto.getDate() * 1000L)
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate limitDay = new Date()
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate()
					.minusDays(DAYS_AFTER_FIRST_PHOTO);

			return firstPhotoDate.isBefore(limitDay);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	@Override
	public String getErrorMessage(UserXtrCounters userXtrCounters) {
		return String.format("User's with id = %d last photo is too fresh!", userXtrCounters.getId());
	}

	@Autowired
	public void setVkApiClient(VkApiClient vkApiClient) {
		this.vkApiClient = vkApiClient;
	}

	@Autowired
	public void setUserActor(UserActor userActor) {
		this.userActor = userActor;
	}

	@Override
	public int getOrder() {
		return 400;
	}
}
