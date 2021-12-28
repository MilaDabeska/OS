package aud4;

import java.util.concurrent.Semaphore;

public class ProducerController {
    public static int NUM_RUN = -50;

    public static Semaphore accessBuffer; //go ogranicuva pristapot do bufferot
    public static Semaphore lock; //go ogranicuva pristapot do numCheks promenlivata
    public static Semaphore canCheck; //za kontrola vo bufferot

    public static void init() {
        accessBuffer = new Semaphore(1);
        lock = new Semaphore(1);
        canCheck = new Semaphore(10);
    }

    public static class Buffer {
        public int numChecks = 0;

        public void produce() {
            System.out.println("Producer is producing");
        }

        public void check() {
            System.out.println("Controller is cheking");
        }
    }

    public static class Producer extends Thread {
        private final Buffer buffer;

        public Producer(Buffer b) {
            this.buffer = b;
        }

        public void execute() throws InterruptedException {
            accessBuffer.acquire(); //se dodeka ne ni bide otvorena "vratata"
            this.buffer.produce();
            accessBuffer.release();
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Controller extends Thread {
        private final Buffer buffer;

        public Controller(Buffer buffer) {
            this.buffer = buffer;
        }

        public void execute() throws InterruptedException {
            lock.acquire(); //zastita na proverkata,slucajno da ne napravime brishenje
            if (this.buffer.numChecks == 0) { //ako brojot na kontrolori tekovno e 0
                accessBuffer.acquire(); //cekaj da bide otvoren semaforot
            }
            this.buffer.numChecks++; //informacija kolku tekovni kontroleri rabotat na lentata
            lock.release(); //go osloboduvame

            canCheck.acquire(); //da ne slucajno sme go nadminale brojot na tekovni aktivni kontroleri
            this.buffer.check(); //ako e otvoren semaforot,kje se povika check()
            lock.acquire(); // treba da go napravime atomicno bez da se sluci prebrishuvanje na numChecks
            this.buffer.numChecks--; //namaluvanje na vrednosta na kontroleri,sme zavrshile so check()
            canCheck.release(); //go osloboduvame
            if (this.buffer.numChecks == 0) { //da ne slucajno e posleden
                accessBuffer.release(); //dozvola za izvrshuvanje i da proizvede novi stavki produce()
            }
            lock.release();

        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
