package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import ru.krotov.teenssearchservice.components.filters.Filter;

public abstract class AbstractUserFilter implements UserFilter {

	@Override
	public void register(Filter<UserXtrCounters> filter) {
		filter.register(this);
	}

	public AbstractUserFilter(Filter<UserXtrCounters> filter) {
		register(filter);
	}
}
