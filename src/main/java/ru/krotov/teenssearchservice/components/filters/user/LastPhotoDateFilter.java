package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LastPhotoDateFilter extends AbstractUserFilter {

	public LastPhotoDateFilter(@Qualifier("userFilterExecutor") Filter<UserXtrCounters> filter) {
		super(filter);
	}

	private VkApiClient vkApiClient;

	private UserActor userActor;

	private static final int daysAfterFirstPhoto= 30*6;

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {
		try {
			Integer fromId = userXtrCounters.getId();

			TimeUnit.MILLISECONDS.sleep(200);

			GetResponse response = vkApiClient.photos()
					.get(userActor).albumId("profile").rev(false).ownerId(fromId)
					.execute();

			Photo profilePhoto = response.getItems().stream()
					.findFirst()
					.orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d have not profile photos!", fromId)));

			//TODO: DateConverterUtil
			LocalDate firstPhotoDate = new Date(profilePhoto.getDate()*1000L)
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate limitDay = new Date()
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate()
					.minusDays(daysAfterFirstPhoto);

			return firstPhotoDate.isBefore(limitDay);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Autowired
	public void setVkApiClient(VkApiClient vkApiClient) {
		this.vkApiClient = vkApiClient;
	}

	@Autowired
	public void setUserActor(UserActor userActor) {
		this.userActor = userActor;
	}
}
