package imt.uvinfo.library;

import imt.support.exam.Scoring;
import imt.support.exam.Scoring.Points;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(Scoring.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibraryTests {

    static List<String> scifiTitles() {
        return Arrays.asList(
                "Steel Beach (John Varley)",
                "Golden Globe (John Varley)",
                "Titan/Wizard/Demon (John Varley)",
                "The Book of the New Sun (Gene Wolfe)",
                "Hyperion (Dan Simmons)",
                "Endymion (Dan Simmons)",
                "Schild's Ladder (Greg Egan)",
                "Les dépossédés (Ursula K. Le Guin)",
                "Snow Crash (Neal Stephenson)",
                "Anathem (Neal Stephenson)",
                "Good Omens (Terry Pratchett & Neil Gaiman)",
                "Little Brother (Cory Doctorow)");
    }

    static List<String> technicalTitles() {
        return Arrays.asList(
                "Objects First with Java (Barnes & Kölling)",
                "Practical Object-Oriented Design in Ruby (Sandi Metz)",
                "Smalltalk Best Practice Patterns (Kent Beck)",
                "Design Patterns (Gamma, Helm, Johnson, Vlissides)",
                "The Little Schemer (Friedman)",
                "How to Design Programs (Felleisen, Findler, Flatt, Krishnamurthi)",
                "Structure and Interpretation of Computer Programs (Abelson, Sussmann)",
                "The Art of the Metaobject Protocol (Kikzales, des Rivières, Bobrow)",
                "The Humane Interface (Jef Raskin)");
    }

    @Test
    @Order(1)
    @Points(1)
    void creatingBookRequiresTitle() {
        String title = "Les dépossédés (Ursula K. Le Guin)";
        Book novel = new Book(title);

        assertEquals(title, novel.title());
//        fail("Décommentez le corps du test et supprimez cette ligne.");
    }

    @Test
    @Order(2)
    void booksWithSameTitleAreEqualButStillDistinctObjects() {
        /* Pas de points ici, juste un petit rappel... */
        Book a = new Book("azerty".replace("az", "qw"));
        Book b = new Book("QWERTY".toLowerCase());

        assertEquals(a, b, "les objets a et b sont considérés égaux selon a.equals(b)");
        assertNotSame(a, b, "a et b référencent deux objets distincts: a!=b ou !(a==b)");
//        fail("Décommentez le corps du test et supprimez cette ligne.");
    }

    @Test
    @Order(3)
    @Points(2)
    void convenienceMethodToMakeListOfBooksFromTitles() {
        List<String> titles = technicalTitles();
        int howMany = titles.size();

        List<Book> manyBooks = Book.listFromTitles(titles);

        assertEquals(howMany, manyBooks.size());
        assertEquals(new Book("Objects First with Java (Barnes & Kölling)"),
                     manyBooks.get(0));
        assertEquals(new Book("The Humane Interface (Jef Raskin)"),
                     manyBooks.get(howMany - 1));
//        fail("Décommentez le corps du test et supprimez cette ligne.");
    }

    @Test
    @Order(4)
    @Points(1)
    void newLibraryOwnsNoBooks() {
        Library library = new Library();

        assertEquals(0, library.inventorySize());
        //fail("Décommentez le corps du test et supprimez cette ligne.");
    }

    @Test
    @Order(5)
    @Points(2)
    void afterAddingBooksToInventory() {
        Library library = new Library();
        Book javaBook = new Book("Objects First with Java (Barnes & Kölling)");
        Book novel = new Book("Snow Crash (Neil Stephenson)");

        library.addToInventory(javaBook);

        assertEquals(1, library.inventorySize());
        assertTrue(library.owns(javaBook));
        assertTrue(library.isAvailable(javaBook));

        library.addToInventory(novel);

        assertEquals(2, library.inventorySize());
        assertTrue(library.owns(novel));
        assertTrue(library.owns(javaBook));
        assertTrue(library.isAvailable(javaBook));
        assertTrue(library.isAvailable(novel));
    }

    @Test
    @Order(6)
    @Points(1)
    void bookNotInInventoryNeitherOwnedNorAvailable() {
        Library library = new Library();
        Book unknown = new Book("Java for Newbies (Purcell)");

        assertFalse(library.owns(unknown));
        assertFalse(library.isAvailable(unknown));
    }

    @Test
    @Order(7)
    @Points(2)
    void creatingLibraryWithInitialInventory() {
        List<Book> initialInventory = new ArrayList<>();
        initialInventory.addAll(Book.listFromTitles(technicalTitles()));
        initialInventory.addAll(Book.listFromTitles(scifiTitles()));

        Library library = new Library(initialInventory);

        assertEquals(technicalTitles().size() + scifiTitles().size(),
                     library.inventorySize());
        assertTrue(library.owns(new Book("Objects First with Java (Barnes & Kölling)")));
        assertTrue(library.owns(new Book("Snow Crash (Neal Stephenson)")));
    }

    @Test
    @Order(8)
    @Points(1)
    void canStillAddBooksToInitialInventory() {
        Library library = new Library(Book.listFromTitles(scifiTitles()));
        Book javaBook = new Book("Objects First with Java (Barnes & Kölling)");

        library.addToInventory(javaBook);

        assertEquals(scifiTitles().size() + 1,
                     library.inventorySize());
        assertTrue(library.owns(javaBook));
    }

    @Test
    @Order(9)
    @Points(2)
    void loaningBookAndFindingBorrower() throws Exception {
        User user = new User("Alice");
        Book loaned = new Book("Les dépossédés (Ursula K. Le Guin)");
        Library library = new Library(Book.listFromTitles(scifiTitles()));

        library.loan(user, loaned);

        assertEquals(user, library.currentBorrower(loaned));
    }

    @Test
    @Order(10)
    @Points(1)
    void availableBookHasNoBorrower() {
        Book book = new Book("Les dépossédés (Ursula K. Le Guin)");
        Library library = new Library();

        library.addToInventory(book);

        assertNull(library.currentBorrower(book));
    }

    @Test
    @Order(11)
    @Points(1)
    void multipleLoansToMultipleBorrowers() throws Exception {
        Library library = new Library(Book.listFromTitles(scifiTitles()));
        User alice = new User("Alice");
        User bob = new User("bob");
        Book leguin = new Book("Les dépossédés (Ursula K. Le Guin)");
        Book egan = new Book("Schild's Ladder (Greg Egan)");
        Book doctorow = new Book("Little Brother (Cory Doctorow)");

        library.loan(alice, leguin);
        library.loan(bob, egan);
        library.loan(alice, doctorow);

        assertEquals(alice, library.currentBorrower(leguin));
        assertEquals(alice, library.currentBorrower(doctorow));
        assertEquals(bob, library.currentBorrower(egan));
    }

    @Test
    @Order(12)
    @Points(2)
    void loanedBookStillOwnedButNotAvailableAnymore() throws Exception {
        User user = new User("Alice");
        Book loaned = new Book("Les dépossédés (Ursula K. Le Guin)");
        Book other = new Book("Schild's Ladder (Greg Egan)");
        Library library = new Library(Book.listFromTitles(scifiTitles()));

        library.loan(user, loaned);

        assertTrue(library.owns(loaned));
        assertTrue(library.owns(other));

        assertFalse(library.isAvailable(loaned));
        assertTrue(library.isAvailable(other));
    }

    @Test
    @Order(13)
    @Points(2)
    void gettingBooksBorrowedByGivenUser() throws Exception {
        Library library = new Library(Book.listFromTitles(scifiTitles()));
        User user = new User("Alice");
        List<Book> books = Arrays.asList(
                new Book("Les dépossédés (Ursula K. Le Guin)"),
                new Book("Schild's Ladder (Greg Egan)"),
                new Book("Little Brother (Cory Doctorow)"));
        for (Book eachBook : books) library.loan(user, eachBook);

        List<Book> borrowed = library.booksBorrowedBy(user);

        assertTrue(borrowed.containsAll(books));
    }

    @Test
    @Order(14)
    @Points(2)
    void bringBackLoanedBook() throws Exception {
        User user = new User("Alice");
        Book loaned = new Book("Les dépossédés (Ursula K. Le Guin)");
        Library library = new Library(Book.listFromTitles(scifiTitles()));
        library.loan(user, loaned);

        library.restitute(loaned);

        assertTrue(library.isAvailable(loaned));
        assertNull(library.currentBorrower(loaned));
    }

    @Test
    @Order(15)
    @Points(2)
    void borrowingNotOwnedBookThrowsException() {
        User user = new User("Bob");
        Book unknown = new Book("Java for Newbies (Purcell)");
        Library library = new Library(Book.listFromTitles(scifiTitles()));

        assertThrows(CannotLoanException.class, () -> library.loan(user, unknown));

        assertNull(library.currentBorrower(unknown));
        assertFalse(library.owns(unknown));
        assertFalse(library.isAvailable(unknown));
    }

    @Test
    @Order(16)
    @Points(2)
    void borrowingNotAvailableBookThrowsException() {
        User alice = new User("Alice");
        User bob = new User("bob");
        Book loaned = new Book("Les dépossédés (Ursula K. Le Guin)");
        Library library = new Library(Book.listFromTitles(scifiTitles()));

        assertDoesNotThrow(() -> library.loan(alice, loaned));
        assertThrows(CannotLoanException.class,
                     () -> library.loan(bob, loaned));

        assertEquals(alice, library.currentBorrower(loaned));
        assertFalse(library.isAvailable(loaned));
    }

    @Test
    @Order(17)
    @Points(2)
    void borrowingTooManyBooksThrowsException() {
        Library library = new Library(Book.listFromTitles(scifiTitles()));
        User alice = new User("Alice");
        Book leguin = new Book("Les dépossédés (Ursula K. Le Guin)");
        Book egan = new Book("Schild's Ladder (Greg Egan)");
        Book doctorow = new Book("Little Brother (Cory Doctorow)");

        int quota = 2;
        library.setQuota(2);
        assertDoesNotThrow(() -> library.loan(alice, leguin));
        assertDoesNotThrow(() -> library.loan(alice, egan));
        assertThrows(CannotLoanException.class, () -> library.loan(alice, doctorow));

        library.restitute(leguin);
        assertDoesNotThrow(() -> library.loan(alice, doctorow));
    }
}
