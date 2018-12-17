package ru.krotov.teenssearchservice.components.filters;

public interface Registrable<T> {
	int getOrder();
	void register(Register<T> register);
}
