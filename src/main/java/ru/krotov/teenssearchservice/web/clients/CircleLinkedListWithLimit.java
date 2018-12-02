package ru.krotov.teenssearchservice.web.clients;

import java.util.LinkedList;

public class CircleLinkedListWithLimit<T> extends LinkedList<T> {

	private final int limit;

	public CircleLinkedListWithLimit(int limit) {
		this.limit = limit;
	}


	public synchronized boolean add (T element) {

		if (super.size() >= limit) {
			super.removeFirst();
		}

		return super.add(element);
	}


}
