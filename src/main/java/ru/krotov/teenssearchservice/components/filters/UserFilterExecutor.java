package ru.krotov.teenssearchservice.components.filters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.user.UserFilter;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class UserFilterExecutor implements UserFilter {

	private List<Filter<UserXtrCounters>> filters = new LinkedList<>();

	@Override
	public void register(Filter<UserXtrCounters> filter) {
		filters.add(filter);
	}

	@Override
	public boolean filter(UserXtrCounters userXtrCounters) {

		return filters.stream().allMatch(filter -> {
			boolean filterResult = filter.filter(userXtrCounters);

			if (!filterResult) {
				log.error("Invalid user: ({}). Reason: {}", userXtrCounters, filter.getClass().getName());
			}

			return filterResult;
		});
	}
}
