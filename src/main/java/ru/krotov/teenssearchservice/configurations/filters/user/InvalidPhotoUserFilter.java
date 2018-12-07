package ru.krotov.teenssearchservice.configurations.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.configurations.filters.Filter;

@Component
public class InvalidPhotoUserFilter extends AbstractUserFilter {

	private static final String DEFAULT_PHOTO = "camera_400";
	private static final String DELETED_PHOTO = "deactivated";

	public InvalidPhotoUserFilter(@Qualifier("userFilterExecutor") Filter<UserXtrCounters> filter) {
		super(filter);
	}

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {
		String photoMaxOrig = userXtrCounters.getPhotoMaxOrig();
		return !photoMaxOrig.contains(DEFAULT_PHOTO) && !photoMaxOrig.contains(DELETED_PHOTO);
	}
}
