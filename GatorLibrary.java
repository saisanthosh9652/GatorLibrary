import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GatorLibrary {
    private RedBlackTree redBlackTree;

    // default constructor
    GatorLibrary() {
        this.redBlackTree = new RedBlackTree();
    }

    // print the details of book by taking book id
    public Node PrintBook(int bookId) {
        Node node = redBlackTree.search(bookId);
        if (node == null || node == redBlackTree.nillNode) {
            node = new Node(-1, "", "", "");
        }
        return node;
    }

    // inserting the book
    public Boolean InsertBook(int bookId, String bookName, String authorName, String availabilityStatus) {
        Node node = new Node(bookId, bookName, authorName, availabilityStatus);
        if (redBlackTree.search(node.book.bookId) == redBlackTree.nillNode) {
            redBlackTree.insert(node);
            return true;
        } else {
            return false;
        }
    }

    // printing all books in the given range
    public ArrayList < Node > PrintBooks(int bookId1, int bookId2) {
        ArrayList < Node > array = redBlackTree.range(bookId1, bookId2);
        if (array.isEmpty()) {
            Node node = new Node(-1, "", "", "");
            array.add(node);
        }
        return array;
    }

    // main method
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: input file not specified.");
            return;
        }
        String inputFileName = args[0];
        StringBuilder output=new StringBuilder();
        System.out.println(inputFileName.split("\\.").length);
        
        output.append(inputFileName.split("\\.")[0]);
        output.append("_output_file");
        output.append(".txt");
        GatorLibrary gatorLibrary = new GatorLibrary();
        try {
            File input_file = new File(inputFileName);
            Scanner sc = new Scanner(input_file);
            FileWriter output_file = new FileWriter(output.toString());

            while (sc.hasNextLine()) {
                String input = sc.nextLine().strip();
                String[] in = input.substring(input.indexOf("(") + 1, input.indexOf(")")).split(",");

                if (input.startsWith("InsertBook")) {
                    int bookId = Integer.parseInt( in [0].strip());
                    String bookName = in [1].strip();
                    String authorName = in [2].strip();
                    String availabilityStatus = in [3].strip();
                    Boolean node = gatorLibrary.InsertBook(bookId, bookName, authorName, availabilityStatus);
                    if (node == false) {
                        String out = "Duplicate BookId";
                        output_file.write(out + "\n\n");
                    }
                } else if (input.startsWith("Print")) {
                    if ( in .length == 1) {
                        int bookId = Integer.parseInt( in [0].strip());
                        Node node = gatorLibrary.PrintBook(bookId);
                        if (node.book.bookId == -1) {
                            output_file.write("Book " + bookId + " not found in the Library");
                            output_file.write("\n\n");

                        } else {
                            output_file.write("" + node + "");
                            output_file.write("\n\n");
                        }
                    } else {
                        int bookNumber1 = Integer.parseInt( in [0].strip());
                        int bookNumber2 = Integer.parseInt( in [1].strip());
                        ArrayList < Node > arr = gatorLibrary.PrintBooks(bookNumber1, bookNumber2);

                        for (int i = 0; i < arr.size(); i++) {
                            output_file.write("" + arr.get(i) + "");
                            output_file.write("\n\n");
                        }
                    }
                } else if (input.startsWith("Borrow")) {
                    System.out.println( in .length);
                    int patronId = Integer.parseInt( in [0].strip());
                    int bookId = Integer.parseInt( in [1].strip());
                    int patronPriority = Integer.parseInt( in [2].strip());
                    output_file.write(gatorLibrary.borrowBook(patronId, bookId, patronPriority));
                    output_file.write("\n\n");

                } else if (input.startsWith("Return")) {
                    int patronId = Integer.parseInt( in [0].strip());
                    int bookId = Integer.parseInt( in [1].strip());
                    output_file.write(gatorLibrary.ReturnBook(patronId, bookId));
                    output_file.write("\n\n");

                } else if (input.startsWith("FindClosestBook")) {
                    int targetId = Integer.parseInt( in [0].strip());

                    output_file.write(gatorLibrary.FindClosestBook(targetId));
                    output_file.write("\n\n");

                } else if (input.startsWith("Delete")) {
                    int bookId = Integer.parseInt( in [0].strip());
                    output_file.write(gatorLibrary.DeleteBook(bookId));
                    output_file.write("\n\n");

                } else if (input.startsWith("ColorFlipCount")) {
                    output_file.write(gatorLibrary.ColorFlipcount());
                    output_file.write("\n\n");

                } else if (input.startsWith("Quit")) {
                    output_file.write("Program Terminated!!");
                    break;

                }
            }

            sc.close();
            output_file.close();
        } catch (IOException ioException) {
            ioException.getStackTrace();
        }
    }
    //getting the count of color flip
    private String ColorFlipcount() {
        StringBuilder temp = new StringBuilder();
        temp.append("Color Flip Count: " + redBlackTree.flipCount);

        return temp.toString();

    }
    //function to find the closest book
    private String FindClosestBook(int targetId) {
        return redBlackTree.findClosestBook(targetId);

    }

    //delete by book by its id
    private String DeleteBook(int bookId) {
        Node node = redBlackTree.search(bookId);
        StringBuilder temp = new StringBuilder();
        if (node != null) {
            temp.append("Book " + bookId + " is no longer available. ");
            if (!node.book.minHeap.isEmpty()) {
                List <Patron> list = node.book.minHeap.getAllElements();
                if (list.size() > 1) {
                	String result = list.stream().map(patron -> String.valueOf(patron.id)).collect(Collectors.joining(", "));                        // method to get id from
                    temp.append("Reservations made by Patrons " + result + " have been cancelled!");
                } else {
                    temp.append("Reservation made by Patron " + list.get(0).id + " has been cancelled!");
                }
            }
            redBlackTree.delete(bookId);
        }
        return temp.toString();
    }

    //returning book by the patron
    private String ReturnBook(int patronId, int bookId) {
        Node node = redBlackTree.search(bookId);
        StringBuilder temp = new StringBuilder();
        if (node.book.availabilityStatus.equals("No")) {
            temp.append("Book " + bookId + " Returned by Patron " + patronId);
            if (node.book.minHeap.isEmpty()) {
                node.book.availabilityStatus = "Yes";
                node.book.borrowedBy = null;
            } else {
                node.book.borrowedBy = node.book.minHeap.getMin();
                temp.append("\n\n" + "Book " + bookId + " Allotted to Patron " + node.book.borrowedBy.id);
                node.book.minHeap.removeMin();
            }
        }
        return temp.toString();

    }
    //borrowing the book
    private String borrowBook(int patronId, int bookId, int patronPriority) {
        Node node = redBlackTree.search(bookId);
        StringBuilder temp = new StringBuilder();
        System.out.println(node.book);
        Patron patron = new Patron(patronId, patronPriority, (int) Instant.now().getEpochSecond());
        String availabilityStatus = node.book.availabilityStatus;
        if (availabilityStatus.contains("Yes")) {
            node.book.availabilityStatus = "No";
            node.book.borrowedBy = patron;
            temp.append("Book " + node.book.bookId + " Borrowed by Patron " + patronId);

        } else {
            node.book.minHeap.insert(patron);
            temp.append("Book " + node.book.bookId + " Reserved by Patron " + patron.id);

        }
        System.out.println(node.book.borrowedBy.id);
        return temp.toString();
    }

}