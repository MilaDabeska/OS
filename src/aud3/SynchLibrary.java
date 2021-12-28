package aud3;

import java.util.ArrayList;
import java.util.List;

public class SynchLibrary {
    List<String> books = new ArrayList<>();
    int capacity; //kolku max knigi moze da imame vo bibliotekata

    public SynchLibrary(int capacity) {
        this.capacity = capacity;
    }

    // 1.ako clenot vrati kniga vo bibliotekata

    public synchronized void returnBook(String book) throws InterruptedException {
//        if (books.size() != capacity)
        while (books.size() == capacity) { //se dodeka ne se namali brojot na knigi
            wait(); //cekaj
        }
        books.add(book);
        notifyAll(); //gi budi site threads sto spijat i da se aktiviraat site sto gi targetira taa notifikacija
    }

    // 2.ako clenot pojazmi kniga od bibliotekata

    public synchronized String borrowBook() throws InterruptedException {
        String book = "";
//        if (books.size() != 0)
        while (books.size() == 0) { //se dodeka brojot na knigi ni e 0
            wait(); //cekaj dur ne vrati nekoj clen
        }
        book = books.remove(0);
        notifyAll();
        return book;
    }
}
