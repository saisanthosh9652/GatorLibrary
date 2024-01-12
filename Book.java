import java.util.List;

class Book implements Comparable<Book> {
	//attributes
	public int bookId;
	public String bookName;
	public String authorName;
	public String availabilityStatus;
	public Patron borrowedBy;
	public MinHeap<Patron> minHeap;
	//default constructor
	public Book() {
		super();
		this.minHeap = new MinHeap<Patron>();
		this.borrowedBy = null;

	}
	//parameterized cost
	public Book(int bookId, String bookName, String authorName, String availabilityStatus) {
		this();
		this.bookId = bookId;
		this.bookName = bookName;
		this.authorName = authorName;
		this.availabilityStatus = availabilityStatus;
	}

	@Override
	public int compareTo(Book book) {
		return this.bookId - book.bookId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BookID = ").append(bookId).append("\n");
		sb.append("Title = \"").append(bookName).append("\"\n");
		sb.append("Author = \"").append(authorName).append("\"\n");
		sb.append("Availability = \"").append(availabilityStatus).append("\"\n");
		sb.append("BorrowedBy = ").append(borrowedBy != null ? borrowedBy.id : "None").append("\n");

		// Assuming MinHeap has a method to get all elements as a list
		List<Patron> reservationList = minHeap.getAllElements();
		sb.append("Reservations = [");
		for (int i = 0; i < reservationList.size(); i++) {
			sb.append(reservationList.get(i).id);
			if (i < reservationList.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		sb.append("\n");

		return sb.toString();
	}

}