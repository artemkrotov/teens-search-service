package ru.krotov.teenssearchservice.components.filters;

import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.wall.WallPostFullFilter;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class WallPostFullFilterExecutor implements WallPostFullFilter {

	private List<Filter<WallPostFull>> filters = new LinkedList<>();

	@Override
	public void register(Filter<WallPostFull> filter) {
		filters.add(filter);
	}

	@Override
	public boolean filter(WallPostFull wallPostFull) {
		return filters.stream().allMatch(filter -> {
			boolean filterResult = filter.filter(wallPostFull);

			if (!filterResult) {
				log.error("Invalid message: ({}). Reason: {}", wallPostFull, filter.getClass().getName());
			}

			return filterResult;
		});
	}
}
