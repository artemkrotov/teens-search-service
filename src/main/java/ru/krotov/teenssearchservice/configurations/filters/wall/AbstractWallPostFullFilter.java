package ru.krotov.teenssearchservice.configurations.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import ru.krotov.teenssearchservice.configurations.filters.Filter;
import ru.krotov.teenssearchservice.configurations.filters.WallPostFullFilter;

public abstract class AbstractWallPostFullFilter implements WallPostFullFilter {

	@Override
	public void register(Filter<WallPostFull> filter) {
		filter.register(this);
	}

	public AbstractWallPostFullFilter(Filter<WallPostFull> filter) {
		register(filter);
	}
}
