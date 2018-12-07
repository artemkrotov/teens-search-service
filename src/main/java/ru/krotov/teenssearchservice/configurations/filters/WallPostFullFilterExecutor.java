package ru.krotov.teenssearchservice.configurations.filters;

import com.vk.api.sdk.objects.wall.WallPostFull;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class WallPostFullFilterExecutor implements WallPostFullFilter {

	private List<Filter<WallPostFull>> filters = new LinkedList<>();

	@Override
	public void register(Filter<WallPostFull> filter) {
		filters.add(filter);
	}

	@Override
	public boolean filter(WallPostFull wallPostFull) {
		return filters.stream().allMatch(filter -> filter.filter(wallPostFull));
	}
}
