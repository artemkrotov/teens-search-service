package ru.krotov.teenssearchservice.components.filters.user;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import ru.krotov.teenssearchservice.components.filters.Register;
import ru.krotov.teenssearchservice.components.filters.Registrable;
import ru.krotov.teenssearchservice.components.filters.UserFilterExecutor;

public abstract class AbstractUserFilter implements UserFilter {

	public AbstractUserFilter(UserFilterExecutor userFilterExecutor) {
		register(userFilterExecutor);
	}

	@Override
	public void register(Register<UserXtrCounters> register) {
		register.register(this);
	}

	@Override
	public int compareTo(Registrable o) {
		return this.getOrder() - o.getOrder();
	}
}
