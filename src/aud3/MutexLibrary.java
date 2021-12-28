package aud3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//sinhronizacija so pomosh na Mutex

public class MutexLibrary {
    List<String> books = new ArrayList<>();
    int capacity; //kolku max knigi moze da imame vo bibliotekata
    public static Lock lock = new ReentrantLock();

    public MutexLibrary(int capacity) {
        this.capacity = capacity;
    }

    // 1.ako clenot vrati kniga vo bibliotekata

    public void returnBook(String book) throws InterruptedException {
        while (true) { //ceka i proveruva dali ima nekakva promena,dali kje dojde do uslovot(if)
            lock.lock();
            if (books.size() < capacity) {
                books.add(book);
                lock.unlock();
                break; //koa kje dojde do uslovot
            }
            lock.unlock(); //ako ne se ispolni uslovot(if)
        }
    }

    // 2.ako clenot pojazmi kniga od bibliotekata

    public String borrowBook() throws InterruptedException {
        String book = "";
        while (true) { //se proveruva dali uslovot se smenil
            lock.lock();
            if (books.size() > 0) {
                book = books.remove(0);
                lock.unlock();
                break; //se ispolnil uslovot
            }
            lock.unlock();
        }
        return book;
    }
}
