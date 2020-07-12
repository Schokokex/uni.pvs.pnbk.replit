package de.uulm.sp.pvs.pnbk.main.airports;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Route<NodeType> implements Iterable<NodeType> {

	private LinkedList<Node<NodeType>> list = new LinkedList<Node<NodeType>>();

	public boolean contains(NodeType value) {
		for (Node<NodeType> node : list) {
			if (Objects.equals(node.getValue(), value)) {
				return true;
			}
		}
		return false;
	}

	public void add(Node<NodeType> node) {
		list.add(node);
	}

	public boolean contains(Node<NodeType> node) {
		return list.contains(node);
	}
	
	public void removeEntriesAfter(NodeType value) {
		if(!contains(value)) {
			throw new NoSuchElementException();
		} 
		while(!Objects.equals(value, list.getLast().getValue())) {
			list.removeLast();
		}
	}
	
	public boolean intersects(Route<NodeType> otherRoute) {
		for (var object : otherRoute) {
			if(this.contains(object)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Route) {
			final Route<?> otherRoute = (Route<?>) obj;
			return Objects.equals(otherRoute.list, list);
		}
		return super.equals(obj);
	}

	@Override
	public Iterator<NodeType> iterator() {
		return new Iterator<NodeType>() {
			private Iterator<Node<NodeType>> iterator = list.iterator();
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public NodeType next() {
				return iterator.next().getValue();
			}
		};
	}

	public void addFirst(Node<NodeType> from) {
		list.addFirst(from);
	}
}
