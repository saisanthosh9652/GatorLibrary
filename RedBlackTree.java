import java.util.ArrayList;
import java.util.Comparator;

class RedBlackTree {
	Node root;
	Node nillNode;
	boolean RED = true;
	boolean BLACK = false;
	int flipCount;

	// default constructor
	RedBlackTree() {
		this.nillNode = new Node(-1, "", "", "");
		this.nillNode.left = null;
		this.nillNode.right = null;
		this.nillNode.color = BLACK;
		this.root = this.nillNode;
		this.flipCount = 0;
	}

	// used to rotate left in the tree
	public void rotateLeft(Node node) {
		if (node == null || node == nillNode) {
			return;
		}
		Node rightNode = node.right;

		node.right = rightNode.left;
		if (rightNode.left != nillNode) {
			rightNode.left.parent = node;
		}

		rightNode.parent = node.parent;
		if (node.parent == null) {
			this.root = rightNode;
		} else if (node.parent.left == node) {
			node.parent.left = rightNode;
		} else {
			node.parent.right = rightNode;
		}
		rightNode.left = node;
		node.parent = rightNode;
	}

//used  to rightrotate in the redblack tree
	public void rotateRight(Node node) {
		if (node == null || node == nillNode) {
			return;
		}
		Node leftNode = node.left;

		node.left = leftNode.right;
		if (leftNode.right != null && leftNode.right != nillNode) {
			leftNode.right.parent = node;
		}

		leftNode.parent = node.parent;
		if (node.parent == null) {
			this.root = leftNode;
		}

		else if (node.parent.left == node) {
			node.parent.left = leftNode;
		} else {
			node.parent.right = leftNode;
		}

		leftNode.right = node;
		node.parent = leftNode;

	}

//inserting the node into the red black tree
	public boolean insert(Node node) {

		node.parent = null;
		node.left = nillNode;
		node.right = nillNode;
		node.color = RED;

		Node sn = this.root;
		Node snParent = null;
		while (sn != nillNode) {
			snParent = sn;
			if (sn.key > node.key) {
				sn = sn.left;
			} else if (sn.key < node.key) {
				sn = sn.right;
			} else {
				return false;
			}
		}
		node.parent = snParent;
		if (snParent == null) {
			root = node;
		} else if (snParent.key > node.key) {
			snParent.left = node;
		} else {
			snParent.right = node;
		}

		if (node.parent == null) {
			return true;
		}
		if (node.parent.parent == null) {
			return true;
		}

		insertFixUp(node);

		return true;

	}

	// getting the colour of the required node
	public Boolean getColor(Node node) {
		if (node == null || node == nillNode) {
			return BLACK;
		}
		return node.color;
	}

	// changing the color
	public void flipColor(Node node) {
		if (node == null || node == nillNode) {
			return;
		}
		node.color = RED;
		this.flipCount++;
		if (node.left != null) {
			node.left.color = BLACK;
			this.flipCount++;
		}
		if (node.right != null) {
			node.right.color = BLACK;
			this.flipCount++;
		}
	}

	// fixing up the redblack tree after inserting
	public void insertFixUp(Node node) {
		Node nUncle;
		while (node.parent.color == RED) {
			if (node.parent == node.parent.parent.right) {
				nUncle = node.parent.parent.left;
				if (nUncle.color == RED) {
					this.flipCount += 3;
					nUncle.color = BLACK;
					node.parent.color = BLACK;

					node.parent.parent.color = RED;

					node = node.parent.parent;
				} else {
					if (node == node.parent.left) {
						node = node.parent;
						rotateRight(node);
					}
					this.flipCount += 2;
					node.parent.color = BLACK;

					node.parent.parent.color = RED;

					rotateLeft(node.parent.parent);
				}
			} else {
				System.out.println("!!!!@@@");
				nUncle = node.parent.parent.right;

				if (nUncle.color == RED) {
					this.flipCount += 3;

					nUncle.color = BLACK;
					node.parent.color = BLACK;

					node.parent.parent.color = RED;

					node = node.parent.parent;
				} else {
					if (node == node.parent.right) {
						node = node.parent;
						rotateLeft(node);
					}
					this.flipCount += 2;
					node.parent.color = BLACK;

					node.parent.parent.color = RED;

					rotateRight(node.parent.parent);
				}
			}
			if (node == root) {
				break;
			}
		}

		root.color = BLACK;

	}

	public void transplant(Node rn, Node nr) {
		if (rn.parent == null) {
			this.root = nr;
		} else if (rn == rn.parent.left) {
			rn.parent.left = nr;
		} else {
			rn.parent.right = nr;
		}
		nr.parent = rn.parent;

	}

	//getting the minimum
	public Node minimum(Node node) {
		while (node.left != nillNode) {
			node = node.left;
		}
		return node;
	}

	// deleting the node on the basis of bookid
	public void delete(Integer key) {
		Node node = search(key);

		if (node == nillNode || node == null) {
			return;
		}
		Node nodeTobeReplace = node;
		boolean nodeTobeReplace_originalColor = node.color;
		Node fixUpNode;
		if (node.left == nillNode) {
			fixUpNode = node.right;
			transplant(node, node.right);
		} else if (node.right == nillNode) {
			fixUpNode = node.left;
			transplant(node, node.left);
		} else {
			nodeTobeReplace = minimum(node.right);
			nodeTobeReplace_originalColor = nodeTobeReplace.color;
			fixUpNode = nodeTobeReplace.right;
			if (nodeTobeReplace.parent == node) {
				fixUpNode.parent = nodeTobeReplace;
			} else {
				transplant(nodeTobeReplace, nodeTobeReplace.right);
				nodeTobeReplace.right = node.right;
				nodeTobeReplace.right.parent = nodeTobeReplace;
			}
			transplant(node, nodeTobeReplace);
			nodeTobeReplace.left = node.left;
			nodeTobeReplace.left.parent = nodeTobeReplace;
			nodeTobeReplace.color = node.color;
		}

		if (nodeTobeReplace_originalColor == BLACK) {
			deleteFixUp(fixUpNode);
		}
	}

	// fixing up the red black tree after deleting
	public void deleteFixUp(Node nodeToFix) {
		Node node;
		while (nodeToFix != this.root && nodeToFix.color == BLACK) {
			if (nodeToFix == nodeToFix.parent.left) {
				node = nodeToFix.parent.right;
				if (node.color == RED) {
					this.flipCount += 1;
					node.color = BLACK;
					nodeToFix.parent.color = RED;
					rotateLeft(nodeToFix.parent);
					node = nodeToFix.parent.right;
				}

				if (node.left.color == BLACK && node.right.color == BLACK) {
					this.flipCount++;
					node.color = RED;
					nodeToFix = nodeToFix.parent;
				} else {
					if (node.right.color == BLACK) {
						this.flipCount += 2;
						node.left.color = BLACK;
						node.color = RED;
						rotateRight(node);
						node = nodeToFix.parent.right;
					}
					this.flipCount += 2;
					node.color = nodeToFix.parent.color;
					nodeToFix.parent.color = BLACK;
					node.right.color = BLACK;
					rotateLeft(nodeToFix.parent);
					nodeToFix = root;
				}
			} else {
				node = nodeToFix.parent.left;
				if (node.color == RED) {
					this.flipCount += 1;
					node.color = BLACK;
					nodeToFix.parent.color = RED;
					rotateRight(nodeToFix.parent);
					node = nodeToFix.parent.left;
				}
				if (node.right.color == BLACK && node.right.color == BLACK) {
					this.flipCount++;
					node.color = RED;
					nodeToFix = nodeToFix.parent;
				} else {
					if (node.left.color == BLACK) {
						this.flipCount += 2;
						node.right.color = BLACK;
						node.color = RED;
						rotateLeft(node);
						node = nodeToFix.parent.left;
					}
					this.flipCount += 2;
					node.color = nodeToFix.parent.color;
					nodeToFix.parent.color = BLACK;
					node.left.color = BLACK;
					rotateRight(nodeToFix.parent);
					nodeToFix = root;

				}
			}
		}

		nodeToFix.color = BLACK;
	}

	// searching in the tree on the basis of given key
	public Node search(int key) {
		Node temp = this.root;
		while (temp != nillNode) {
			if (temp.key == (key)) {
				return temp;
			} else if (temp.key > key) {
				temp = temp.left;
			} else {
				temp = temp.right;
			}
		}
		return temp;
	}

	// adding the elements to the list in the given range of ride number
	private void rangeHelper(Node node, int key1, int key2, ArrayList<Node> arr) {
		if (node == null || node == nillNode) {
			return;
		}
		if (node.key > key2) {
			rangeHelper(node.left, key1, key2, arr);
		} else if (node.key >= key1 && node.key <= key2) {

			rangeHelper(node.left, key1, key2, arr);
			arr.add(node);
			rangeHelper(node.right, key1, key2, arr);

		} else {
			rangeHelper(node.right, key1, key2, arr);

		}
	}

	public ArrayList<Node> range(int key1, int key2) {
		ArrayList<Node> arr = new ArrayList<>();
		rangeHelper(root, key1, key2, arr);
		return arr;
	}

	public String findClosestBook(int targetID) {
		StringBuilder temp = new StringBuilder();
		Node closest = findClosestNode(root, targetID, null);
		if (closest == null) {
			temp.append("No books found in the tree.");
		}

		// Check for ties
		ArrayList<Node> closestBooks = new ArrayList<>();
		closestBooks.add(closest);

		int distance = Math.abs(closest.key - targetID);
		findTies(root, targetID, distance, closestBooks);

		// Sort by book IDs if there are ties
		closestBooks.sort(Comparator.comparingInt(node -> node.key));

		// Print the details of the closest book(s)
		for (Node book : closestBooks) {
			temp.append(book);
		}
		return temp.toString();
	}

	private Node findClosestNode(Node node, int targetID, Node currentClosest) {
		if (node == null || node == nillNode) {
			return currentClosest;
		}

		if (currentClosest == null || Math.abs(node.key - targetID) < Math.abs(currentClosest.key - targetID)) {
			currentClosest = node;
		}

		if (node.key > targetID) {
			return findClosestNode(node.left, targetID, currentClosest);
		} else {
			return findClosestNode(node.right, targetID, currentClosest);
		}
	}

	private void findTies(Node node, int targetID, int distance, ArrayList<Node> closestBooks) {
		if (node == null || node == nillNode) {
			return;
		}

		int currentDistance = Math.abs(node.key - targetID);
		if (currentDistance == distance && !closestBooks.contains(node)) {
			closestBooks.add(node);
		}

		findTies(node.left, targetID, distance, closestBooks);
		findTies(node.right, targetID, distance, closestBooks);
	}

}