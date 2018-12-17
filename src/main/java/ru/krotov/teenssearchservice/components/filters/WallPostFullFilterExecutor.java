package ru.krotov.teenssearchservice.components.filters;

import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
public class WallPostFullFilterExecutor implements Register<WallPostFull> {

	private Set<Filter<WallPostFull>> filters = new TreeSet<>();

	public void register(Filter<WallPostFull> filter) {
		filters.add(filter);
	}

	public boolean filter(WallPostFull wallPostFull) {

		return filters.stream().allMatch(filter -> {
			boolean filterResult = filter.filter(wallPostFull);

			if (!filterResult) {
				log.warn(filter.getErrorMessage(wallPostFull));
			}

			return filterResult;
		});
	}
}
