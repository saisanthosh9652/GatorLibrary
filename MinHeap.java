import java.util.ArrayList;
import java.util.List;

class MinHeap<T extends Comparable<T>> {
	private ArrayList<T> minHeap;

	// default constructor
	public MinHeap() {
		this.minHeap = new ArrayList<>();
	}

	// toString method
	public String toString() {
		String tempString = "";
		for (int i = 0; i < minHeap.size(); i++) {
			T temp = minHeap.get(i);
			tempString = tempString + temp.toString() + ", ";
		}
		return tempString;
	}

	// checks whether the heap is empty or not
	public boolean isEmpty() {
		return minHeap.isEmpty();
	}

	// getting all the elemnts of a heap
	public List<T> getAllElements() {
		return this.minHeap;
	}

	// inserting the node into the heap
	public void insert(T node) {
		minHeap.add(node);
		int currentIndex = minHeap.size() - 1;
		while (currentIndex > 0) {
			int parentIndex = (currentIndex - 1) / 2;
			T parent = minHeap.get(parentIndex);
			if (node.compareTo(parent) < 0) {
				minHeap.set(currentIndex, parent);
				minHeap.set(parentIndex, node);
				currentIndex = parentIndex;
			} else {
				break;
			}
		}
	}

	// getting the minimum element from the heap
	public T getMin() {
		if (minHeap.size() == 0) {
			return null;
		}
		return minHeap.get(0);
	}

	// removing the minimum element from the heap
	public T removeMin() {
		if (minHeap.size() == 0) {
			return null;
		}
		T minNode = this.getMin();
		T lastNode = minHeap.remove(minHeap.size() - 1);
		if (minHeap.size() != 0) {

			minHeap.set(0, lastNode);

			int currentIndex = 0;
			while (true) {
				int leftIndex = 2 * currentIndex + 1;
				int rightIndex = 2 * currentIndex + 2;

				if (leftIndex >= minHeap.size()) {
					break;
				}
				int minChildIndex = leftIndex;

				if (rightIndex < minHeap.size()) {
					T left = minHeap.get(leftIndex);
					T right = minHeap.get(rightIndex);

					if (left.compareTo(right) > 0) {
						minChildIndex = rightIndex;
					}
				}

				T minChild = minHeap.get(minChildIndex);

				if (lastNode.compareTo(minChild) > 0) {
					minHeap.set(currentIndex, minChild);
					minHeap.set(minChildIndex, lastNode);
					currentIndex = minChildIndex;
				} else {
					break;
				}

			}
		}

		return minNode;
	}

	// deleting the node from the heap
	public void delete(T node) {
		if (minHeap.size() == 0) {
			return;
		}

		int index = minHeap.indexOf(node);

		if (index == -1) {
			return;
		}

		T lastNode = minHeap.remove(minHeap.size() - 1);

		if (index != minHeap.size()) {
			minHeap.set(index, lastNode);
			int currentIndex = index;

			while (true) {
				int leftIndex = 2 * currentIndex + 1;
				int rightIndex = 2 * currentIndex + 2;

				if (leftIndex >= minHeap.size()) {
					break;
				}

				int minChildIdx = leftIndex;

				if (rightIndex < minHeap.size()) {
					T left = minHeap.get(leftIndex);
					T right = minHeap.get(rightIndex);
					if (left.compareTo(right) > 0) {
						minChildIdx = rightIndex;
					}
				}

				T minChild = minHeap.get(minChildIdx);

				if (lastNode.compareTo(minChild) > 0) {
					minHeap.set(currentIndex, minChild);
					minHeap.set(minChildIdx, lastNode);
					currentIndex = minChildIdx;
				} else {
					break;
				}
			}
			int parentIndex = (currentIndex - 1) / 2;

			T parent = minHeap.get(parentIndex);

			if (currentIndex > 0 && lastNode.compareTo(parent) < 0) {
				while (currentIndex > 0) {
					parentIndex = (currentIndex - 1) / 2;
					parent = minHeap.get(parentIndex);

					if (lastNode.compareTo(parent) < 0) {
						minHeap.set(currentIndex, parent);
						minHeap.set(parentIndex, lastNode);
						currentIndex = parentIndex;
					} else {
						break;
					}
				}
			}
		}
	}
}
