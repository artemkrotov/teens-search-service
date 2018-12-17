package ru.krotov.teenssearchservice.components.filters;


public interface Filter <T> extends Registrable<T>, Comparable<Registrable> {

	boolean filter (T t);
	String getErrorMessage(T t);
}
