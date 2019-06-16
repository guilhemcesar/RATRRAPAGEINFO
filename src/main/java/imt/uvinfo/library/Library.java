package imt.uvinfo.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {

    private List<Book> booksOwned;
    private List<Book> booksAvailable;
    private Map<Book, User> bookLoaned;
    private int quota;

    public Library() {
        booksOwned = new ArrayList<>();
        booksAvailable = new ArrayList<>();
        bookLoaned = new HashMap<>();
        setQuota(10);
    }

    public Library(List<Book> initialInventory) {
        booksOwned = new ArrayList<>(initialInventory);
        booksAvailable = new ArrayList<>(initialInventory);
        bookLoaned = new HashMap<>();
        setQuota(10);
    }

    public int inventorySize() {
        return booksOwned.size();
    }

    public void addToInventory(Book book) {
        booksOwned.add(book);
        booksAvailable.add(book);
    }

    public boolean owns(Book book) {
        return booksOwned.contains(book);
    }

    public boolean isAvailable(Book book) {
        return booksAvailable.contains(book);
    }

    public void loan(User user, Book loaned) throws CannotLoanException {

        if (!booksAvailable.contains(loaned) || !checkUserRespectQuota(user)) {
            throw new CannotLoanException();
        }

        booksAvailable.remove(loaned);
        bookLoaned.put(loaned, user);
    }

    public User currentBorrower(Book loaned) {
        return bookLoaned.get(loaned);
    }

    public List<Book> booksBorrowedBy(User user) {
        List<Book> books = new ArrayList<>();

        for(Map.Entry<Book, User> entry : bookLoaned.entrySet()) {
            if (entry.getValue().equals(user)) {
                books.add(entry.getKey());
            }
        }

        return books;
    }

    public void restitute(Book loaned) {
        booksAvailable.add(loaned);
        bookLoaned.remove(loaned);
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    private boolean checkUserRespectQuota(User user) {
        int loanedByThisUser = 0;

        for(Map.Entry<Book, User> entry : bookLoaned.entrySet()) {
            if (entry.getValue().equals(user)) {
                loanedByThisUser++;
            }
        }

        return loanedByThisUser < getQuota();
    }
}
