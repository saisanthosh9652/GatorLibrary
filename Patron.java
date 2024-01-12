
public class Patron implements Comparable<Patron> {
	//attributes
	int id;
	int priorityNumber;
	//parameterized constructor
	public Patron(int id, int priorityNumber, int timeOfReservation) {
		super();
		this.id = id;
		this.priorityNumber = priorityNumber;
		this.timeOfReservation = timeOfReservation;
	}

	int timeOfReservation;

	@Override
	public int compareTo(Patron patron) {
		if (this.priorityNumber - patron.priorityNumber == 0) {
			return this.timeOfReservation - patron.timeOfReservation;
		}
		return this.priorityNumber - patron.priorityNumber;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id);
	}

}
