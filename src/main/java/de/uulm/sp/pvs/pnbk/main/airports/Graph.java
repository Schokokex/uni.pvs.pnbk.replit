package de.uulm.sp.pvs.pnbk.main.airports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<NodeType> {

	private HashMap<NodeType, Node<NodeType>> allNodes = new HashMap<NodeType, Node<NodeType>>();

	public Graph(NodeType firstNode) {
		allNodes.put(firstNode, new Node<NodeType>(firstNode));
	}

	private Node<NodeType> getNode(NodeType nodeIdentifier) throws NoSuchElementException {
		final var node = allNodes.get(nodeIdentifier);
		if (null == node) {
			throw new NoSuchElementException();
		}
		return node;
	}

	private Node<NodeType> getOrCreateNode(NodeType nodeIdentifier) {
		try {
			return getNode(nodeIdentifier);
		} catch (NoSuchElementException e) {
			final var newNode = new Node<NodeType>(nodeIdentifier);
			allNodes.put(nodeIdentifier, newNode);
			return newNode;
		}
	}

	public boolean contains(NodeType nodeIdentifier) {
		return allNodes.containsKey(nodeIdentifier);
	}

	public Set<NodeType> getChildrenFrom(NodeType nodeIdentifier) {
		final var node = getNode(nodeIdentifier);
		final var children = node.getChildren();
		final var values = children.stream().map(n -> n.getValue()).collect(Collectors.toSet());
		return values;
	}

	public Set<NodeType> getParentsFrom(NodeType nodeIdentifier) {
		final var node = getNode(nodeIdentifier);
		final var parents = node.getParents();
		final var values = parents.stream().map(n -> n.getValue()).collect(Collectors.toSet());
		return values;
	}

	public void addDirections(NodeType parent, NodeType[] children) {
		final var fromNode = getOrCreateNode(parent);
		for (NodeType toNodeIdentifier : children) {
			final var childNode = getOrCreateNode(toNodeIdentifier);
			fromNode.addChild(childNode);
		}
	}

	private Set<Route<NodeType>> getAllRoutes(Node<NodeType> from, int maxHops) {
		final var out = new HashSet<Route<NodeType>>();
		if (maxHops < 0) {
			return out;
		}
		if (maxHops <= 0) {
			final var route = new Route<NodeType>();
			route.add(from);
			out.add(route);
			return out;
		}
		for (Node<NodeType> child : from.getChildren()) {
			final var suggestions = getAllRoutes(child, maxHops - 1);
			for (Route<NodeType> route : suggestions) {
				if (!route.contains(from)) {
					route.addFirst(from);
					out.add(route);
				}
			}
		}
		return out;
	}

	public Set<Route<NodeType>> getAllRoutes(NodeType from, int maxHops) {
		final var fromNode = getNode(from);
		return getAllRoutes(fromNode, maxHops);
	}

	public Set<Route<NodeType>> getAllRoutes(NodeType from, NodeType to, int maxHops) {
		final var allRoutes = getAllRoutes(from, maxHops);
		final var validRoutes = new HashSet<Route<NodeType>>();
		for (var route : allRoutes) {
			if (route.contains(to)) {
				route.removeEntriesAfter(to);
				validRoutes.add(route);
			}
		}
		return validRoutes;
	}
}
