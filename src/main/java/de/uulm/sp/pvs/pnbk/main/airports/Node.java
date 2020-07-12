package de.uulm.sp.pvs.pnbk.main.airports;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node<NodeType> {
	final public NodeType value;
	final private Set<Node<NodeType>> childrenList = new HashSet<Node<NodeType>>();
	final private Set<Node<NodeType>> parentList = new HashSet<Node<NodeType>>();
	
	public Node(NodeType value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "{value: %s}".formatted(value);
	}
 
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			final var node = (Node<?>) obj;
			return Objects.equals(value, node.value);
		}
		return super.equals(obj);
	}
	
	public NodeType getValue() {
		return value;
	}

	public Set<Node<NodeType>> getChildren(){
		return new HashSet<Node<NodeType>>(childrenList);
	}
	
	public Set<Node<NodeType>> getParents(){
		return new HashSet<Node<NodeType>>(parentList);
	}
	
	public void addChild(Node<NodeType> child) {
		final var parent = this;
		if(childrenList.contains(child)) {
			return;
		} else {
			childrenList.add(child);
			child.addParent(parent);
		}
	}
	
	public void addParent(Node<NodeType> parent) {
		final var child = this;
		if(parentList.contains(parent)) {
			return;
		} else {
			parentList.add(parent);
			parent.addChild(child);
		}
	}

	public void removeChild(Node<NodeType> child) {
		childrenList.remove(child);
		child.removeParent(this);
	}
	
	public void removeParent(Node<NodeType> parent) {
		parentList.remove(parent);
		parent.removeChild(this);
	}
	
}
