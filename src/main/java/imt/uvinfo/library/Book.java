package imt.uvinfo.library;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;

    public  Book(String titleArg) {
        title = titleArg;
    }

    public static List<Book> listFromTitles(List<String> titles) {
        List<Book> books = new ArrayList<>();

        for (String title : titles) {
            books.add(new Book(title));
        }

        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    public String title() {
        return title;
    }
}
