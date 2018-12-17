package ru.krotov.teenssearchservice.components.filters.wall;

import com.vk.api.sdk.objects.wall.WallPostFull;
import ru.krotov.teenssearchservice.components.filters.Register;
import ru.krotov.teenssearchservice.components.filters.Registrable;
import ru.krotov.teenssearchservice.components.filters.WallPostFullFilterExecutor;

public abstract class AbstractWallPostFullFilter implements WallPostFullFilter {

	public AbstractWallPostFullFilter(WallPostFullFilterExecutor wallPostFullFilterExecutor) {
		register(wallPostFullFilterExecutor);
	}

	@Override
	public void register(Register<WallPostFull> register) {
		register.register(this);
	}

	@Override
	public int compareTo(Registrable o) {
		return this.getOrder() - o.getOrder();
	}
}
