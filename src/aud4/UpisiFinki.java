package aud4;

import java.util.concurrent.Semaphore;

public class UpisiFinki {

    static Semaphore slobodnoUpisnoMesto;
    static Semaphore enter;
    static Semaphore here;
    static Semaphore done;

    public static void init() {
        slobodnoUpisnoMesto = new Semaphore(4);
        enter = new Semaphore(0);
        here = new Semaphore(0);
        done = new Semaphore(0);
    }

    public static class Clen extends Thread {

        public void zapishi() {
            System.out.println("Zapishuvam student...");
        }

        public void execute() throws InterruptedException {
            slobodnoUpisnoMesto.acquire(); //1 ceka za da se otvori vratata
            int i = 10; //br na studenti koi treba da gi zapishe,lokalna promenliva

            while (i > 0) {
                //zapishuvanje novi studenti
                enter.release();
                here.acquire();
                zapishi();
                done.release();
                i--;
            }
            slobodnoUpisnoMesto.release();
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Student extends Thread {

        public void ostaviDokumenti() {
            System.out.println("Ostavam dokument...");
        }

        public void execute() throws InterruptedException {
            enter.acquire();
            ostaviDokumenti();
            here.release();
            done.acquire();
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
