package ru.krotov.teenssearchservice.components.filters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class UserFilterExecutor implements Register<UserXtrCounters> {

	private Set<Filter<UserXtrCounters>> filters = new TreeSet<>();

	public void register(Filter<UserXtrCounters> filter) {
		filters.add(filter);
	}

	public boolean filter(UserXtrCounters userXtrCounters) {

		return filters.stream().allMatch(filter -> {
			boolean filterResult = filter.filter(userXtrCounters);

			if (!filterResult) {
				throw new InvalidUserException(filter.getErrorMessage(userXtrCounters));
			}

			return filterResult;
		});
	}
}
