package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.UserFilterExecutor;

@Component
public class InvalidPhotoUserFilter extends AbstractUserFilter {

	private static final String DEFAULT_PHOTO = "camera_400";
	private static final String DELETED_PHOTO = "deactivated";

	public InvalidPhotoUserFilter(UserFilterExecutor filter) {
		super(filter);
	}

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {
		String photoMaxOrig = userXtrCounters.getPhotoMaxOrig();
		return !photoMaxOrig.contains(DEFAULT_PHOTO) && !photoMaxOrig.contains(DELETED_PHOTO);
	}

	@Override
	public String getErrorMessage(UserXtrCounters userXtrCounters) {
		return String.format("User with id = %d has invalid photo", userXtrCounters.getId());
	}

	@Override
	public int getOrder() {
		return 300;
	}
}
