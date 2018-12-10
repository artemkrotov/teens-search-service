package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import ru.krotov.teenssearchservice.components.filters.Filter;

public abstract class AbstractWallPostFullFilter implements WallPostFullFilter {

	@Override
	public void register(Filter<WallPostFull> filter) {
		filter.register(this);
	}

	public AbstractWallPostFullFilter(Filter<WallPostFull> filter) {
		register(filter);
	}
}
