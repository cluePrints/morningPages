package com.mpages.trees;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class BinaryTreeMap<K, V> // implements Map<K, V>
{
	private Entry<K, V> root;
	private Comparator<K> comparator;
	private int size;

	public BinaryTreeMap(Comparator<K> comparator) {
		super();
		this.comparator = comparator;
	}

	public V put(K key, V value) {
		if (root == null) {
			root = entry(key, value);
			size++;
			return null;
		} else {
			Entry<K, V> e = findPlaceToInsert(key, root);
			if (eq(e.key, key)) {
				V old = e.value;
				e.value = value;
				return old;
			} else {
				Entry<K, V> newEntry = entry(key, value);
				newEntry.parent = e;
				if (le(key, e.key)) {
					e.left = newEntry;
				} else {
					e.right = newEntry;
				}

				size++;
				return null;
			}
		}
	}

	public V get(Object key) {
		Entry<K, V> e = getEntry((K) key, root);
		return e == null ? null : e.value;
	}

	public void clear() {
		root = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean containsKey(Object key) {
		return getEntry((K) key, root) != null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public V remove(Object key) {
		Entry<K, V> removed = getEntry((K) key, root);
		if (removed == null) {
			return null;
		} else {
			Entry<K, V> parent = removed.parent;
			if (removed.hasNoChildren()) {
				if (removed == root) {
					root = null;
					size--;
					return removed.value;
				}
				if (comparator.compare(removed.key, parent.key) < 0) {
					parent.left = null;
				} else {
					parent.right = null;
				}
			}
			size--;
			return removed.value;
		}
	}

	private Entry<K, V> findPlaceToInsert(K key, Entry<K, V> curr) {
		if (eq(key, curr.key)) {
			return curr;

		} else if (le(key, curr.key) && curr.left != null) {
			return findPlaceToInsert(key, curr.left);

		} else if (gt(key, curr.key) && curr.right != null) {
			return findPlaceToInsert(key, curr.right);

		} else {
			return curr;
		}
	}

	private Entry<K, V> getEntry(K key, Entry<K, V> curr) {
		while (curr != null) {
			int c = comparator.compare(key, curr.key);
			if (c < 0) {
				curr = curr.left;
			} else if (c > 0) {
				curr = curr.right;
			} else {
				return curr;
			}
		}
		return curr;
	}

	private boolean le(K k1, K k2) {
		return comparator.compare(k1, k2) <= 0;
	}

	private boolean gt(K k1, K k2) {
		return comparator.compare(k1, k2) > 0;
	}

	private boolean eq(K k1, K k2) {
		return comparator.compare(k1, k2) == 0;
	}

	private Entry<K, V> entry(K key, V value) {
		Entry<K, V> e = new Entry<K, V>();
		e.key = key;
		e.value = value;
		return e;
	};

	static class Entry<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;
		private Entry<K, V> parent;
		private Entry<K, V> left;
		private Entry<K, V> right;

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}

		public Entry<K, V> getParent() {
			return parent;
		}

		public void setParent(Entry<K, V> parent) {
			this.parent = parent;
		}

		public Entry<K, V> getLeft() {
			return left;
		}

		public void setLeft(Entry<K, V> left) {
			this.left = left;
		}

		public Entry<K, V> getRight() {
			return right;
		}

		public void setRight(Entry<K, V> right) {
			this.right = right;
		}

		public boolean hasNoChildren() {
			return left == null && right == null;
		}

		@Override
		public String toString() {
			return "Entry [key=" + key + ", value=" + value + "]";
		}
	}
}
