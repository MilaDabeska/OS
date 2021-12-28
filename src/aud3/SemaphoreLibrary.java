package aud3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreLibrary {
    List<String> books = new ArrayList<>();
    int capacity; //kolku max knigi moze da imame vo bibliotekata
    Semaphore coordinator = new Semaphore(1); //kje ima permisija samo za 1 kluc,mutex
    Semaphore returnBookSemaphore = new Semaphore(10);
    Semaphore borrowBookSemaphore = new Semaphore(10);


    public SemaphoreLibrary(int capacity) {
        this.capacity = capacity;
    }

    // 1.ako clenot vrati kniga vo bibliotekata

    public void returnBook(String book) throws InterruptedException {
        returnBookSemaphore.acquire(); //na dadeniot clen kje mu dade kluc,site sto sakaat da vratat kniga
        coordinator.acquire(); //sme zaklucile kriticen domen
        while (books.size() == capacity) {
            coordinator.release();
            Thread.sleep(1000); //ceka
            coordinator.acquire();
        }
        books.add(book);
        coordinator.release(); //go predava klucot na drugiot red sto ceka,go osloboduva kr.domen
        borrowBookSemaphore.release();
    }

    // 2.ako clenot pojazmi kniga od bibliotekata

    public String borrowBook() throws InterruptedException {
        borrowBookSemaphore.acquire(); //se bara kluc
        String book = "";
        coordinator.acquire(); //se bara klucot od kr.domen
        while (books.size() == 0) {
            coordinator.release();
            Thread.sleep(1000);
            coordinator.acquire();
        }
        book = books.remove(0);
        returnBookSemaphore.release();
        return book;
    }
}