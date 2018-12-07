package ru.krotov.teenssearchservice.configurations.filters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class UserFilterExecutor implements UserFilter {

	private List<Filter<UserXtrCounters>> filters = new LinkedList<>();

	@Override
	public void register(Filter<UserXtrCounters> filter) {
		filters.add(filter);
	}

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {
		return filters.stream().allMatch(filter -> filter.filter(userXtrCounters));
	}
}
