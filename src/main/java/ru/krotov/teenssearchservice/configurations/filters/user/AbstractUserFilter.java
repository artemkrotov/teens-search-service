package ru.krotov.teenssearchservice.configurations.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import ru.krotov.teenssearchservice.configurations.filters.Filter;
import ru.krotov.teenssearchservice.configurations.filters.UserFilter;

public abstract class AbstractUserFilter implements UserFilter {

	@Override
	public void register(Filter<UserXtrCounters> filter) {
		filter.register(this);
	}

	public AbstractUserFilter(Filter<UserXtrCounters> filter) {
		register(filter);
	}
}
