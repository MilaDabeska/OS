package aud3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadClass extends Thread {
    String name;
    Incrementor incrementor;

    public ThreadClass(String name, Incrementor incrementor) {
        this.name = name;
        this.incrementor = incrementor;
    }

    @Override
    public void run() { //se pojavuva koa kje povikame nova nishka od dadenata nishka
//        System.out.println("Execution into the Thread class");
        for (int i = 0; i < 20; i++) {
//            System.out.println("Thread" + name + ": " + i);
//            incrementor.safeMutexIncrement();
            try {
                incrementor.safeSemaphoreIncrement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Incrementor {
    private static int count = 0; //da go zgolemuvame so pomosh na threads
    private static Lock lock = new ReentrantLock();
    private static Semaphore semaphore = new Semaphore(2);
    //permit -> dozvola za kolku threads da ni se naogjaat vo kriticniot domen

    public static void unsafeIncrement() throws InterruptedException {
        count++; //ne e atomicna operacija,
        // sostavena e od 3 razlicni operacii,
        // kriticen domen


        // 1. read count
        // 2. increment count + 1
        // 3. write count
        Thread.sleep(1);
    }

    public synchronized void safeIncrement() { //monitor
        count++; //kriticen domen
//        synchronized (this){
//            count++;
//        }
    }

    public void safeClassIncrement() {
        synchronized (Incrementor.class) { //se bazira na nivo na klasa,a ne na nivo na proces
            count++; //kriticen domen
        }
    }

    public static void safeMutexIncrement() {
        //Mutex -> dozvola samo na eden thread da pristapi do kriticniot domen,i site dr da cekaat
        lock.lock();
        count++; //kriticen domen
        lock.unlock(); //bez ova doagja do deadlock,ne zavrshuva programata
    }

    public static void safeSemaphoreIncrement() throws InterruptedException {
        semaphore.acquire();
        count++; //kriticen domen
        semaphore.release();
    }

    public static int getCount() {
        return count;
    }
}

public class OperativniAud3 {
    public static void main(String[] args) throws InterruptedException { //glavna nishka
//        System.out.println("Execution into the main class");

        Incrementor incrementor1 = new Incrementor();
//        aud3.Incrementor incrementor2 = new aud3.Incrementor();
        ThreadClass t1 = new ThreadClass("T1", incrementor1); //born sostojba
        ThreadClass t2 = new ThreadClass("T2", incrementor1);

//        t1.run(); //ne se kreira nova nishka,se izvrshuva vo main nishkata
        t1.start(); //kreiranje na nova nishka,se izvrshuva vo t1 (aud3.ThreadClass), ready sostojba
        t2.start();

        t1.join();  //ceka dur ne zavrshi t1 nishkata
        t2.join();

//        if (t1.isAlive() && t2.isAlive()) {
//            System.out.println("Still alive");
//            t1.interrupt(); //prisilno zapiranje na nishkite
//            t2.interrupt(); //sostojba dead
//        }

        System.out.println(incrementor1.getCount()); //bez join() e 0
//        System.out.println(incrementor2.getCount());
    }
}
