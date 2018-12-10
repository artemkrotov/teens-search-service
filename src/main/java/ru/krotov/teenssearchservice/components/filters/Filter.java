package ru.krotov.teenssearchservice.components.filters;


public interface Filter <T> {

	void register(Filter<T> filter);
	boolean filter (T t);
}
