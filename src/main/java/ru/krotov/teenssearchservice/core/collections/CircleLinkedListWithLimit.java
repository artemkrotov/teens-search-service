package ru.krotov.teenssearchservice.core.collections;

import lombok.EqualsAndHashCode;

import java.util.LinkedList;

@EqualsAndHashCode(callSuper = true, exclude = "limit")
public class CircleLinkedListWithLimit<T> extends LinkedList<T> {

	private final int limit;

	public CircleLinkedListWithLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public synchronized void addFirst (T element) {

		if (super.size() >= limit) {
			super.removeLast();
		}

		super.addFirst(element);
	}


}
