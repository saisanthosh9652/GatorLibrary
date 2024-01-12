class Node implements Comparable<Node> {
	//attributes
	boolean RED = true;
	boolean BLACK = false;
	Book book;
	int key;
	Node parent, left, right;
	// red = t black=f
	boolean color;

	// parameterised constructor
	public Node(int bookId, String bookName, String authorName, String availabilityStatus) {
		this.book = new Book(bookId, bookName, authorName, availabilityStatus);
		this.key = bookId;
		this.color = RED;

	}

	// setting the node
	void set(Node node) {
		this.key = node.key;
		this.book = node.book;
	}

	@Override
	public int compareTo(Node node) {
		return this.book.compareTo(node.book);
	}

	@Override
	public String toString() {
		return this.book.toString();
	}

}