package aud4;

import java.util.concurrent.Semaphore;

public class FinkiToalet {

    public static class Toalet { //spodelen resurs
        public void vlezi() {
            System.out.println("Vleguva...");
        }

        public void izlezi() {
            System.out.println("Izleguva...");
        }
    }

    static Semaphore toalet;
    static Semaphore menLock;
    static Semaphore womanLock;

    static int numMen;
    static int numWoman;

    public static void init() {
        toalet = new Semaphore(1);
        menLock = new Semaphore(1);
        womanLock = new Semaphore(1);
        numMen = 0;
        numWoman = 0;
    }

    public static class Man extends Thread {
        private Toalet toalet;

        public Man(Toalet toalet) {
            this.toalet = toalet;
        }

        public void enter() throws InterruptedException {
            menLock.acquire();
            if (numMen == 0) {
                FinkiToalet.toalet.acquire(); //notfication deka toaletot e zafaten
            }
            numMen++; //mazi sto cekaat vo red
            this.toalet.vlezi();
            menLock.release();
        }

        public void exit() throws InterruptedException {
            menLock.acquire();
            this.toalet.izlezi();
            numMen--; //mazi sto zavrshile

            if (numMen == 0) { //ako nema mazi vo redicata
                FinkiToalet.toalet.release(); //dozvola za da vlezat zeni
            }
            menLock.release();
        }

        @Override
        public void run() {
            for (int i=0;i<numMen;i++){
                try {
                    enter();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Woman extends Thread {
        private Toalet toalet;

        public Woman(Toalet toalet) {
            this.toalet = toalet;
        }

        public void enter() throws InterruptedException {
            womanLock.acquire();
            if (numWoman == 0) {
                FinkiToalet.toalet.acquire(); //notfication deka toaletot e zafaten
            }
            numWoman++; //zeni sto cekaat vo red
            this.toalet.vlezi();
            womanLock.release();
        }

        public void exit() throws InterruptedException {
            womanLock.acquire();
            this.toalet.izlezi();
            numWoman--; //zeni sto zavrshile

            if (numWoman == 0) { //ako nema zeni vo redicata
                FinkiToalet.toalet.release(); //dozvola za da vlezat zeni
            }
            womanLock.release();
        }

        @Override
        public void run() {
            for (int i=0;i<numWoman;i++){
                try {
                    enter();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
