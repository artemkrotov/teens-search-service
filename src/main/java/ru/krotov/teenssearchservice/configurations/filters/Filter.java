package ru.krotov.teenssearchservice.configurations.filters;


public interface Filter <T> {

	void register(Filter<T> filter);
	boolean filter (T t);
}
