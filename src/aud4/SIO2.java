package aud4;

import java.util.concurrent.Semaphore;

public class SIO2 {
    public static int NUM_RUN = 50;

    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere; //da kazeme deka sme pristignale
    static Semaphore oHere;
    static Semaphore ready;

    public static void init() {
        si = new Semaphore(1);
        o = new Semaphore(2);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }

    public static class Si extends Thread {

        public void bond() {
            System.out.println("Si is bonding now");
        }


        public void execute() throws InterruptedException {
            si.acquire();
            siHere.release(2); //notfication za prviot i vtoriot atom na kislorod
            oHere.acquire(2); //da bideme izvesteni deka pristignal nekoj atom na O,cekame 2 poraki
            ready.release(2); //vo isto vreme
            bond();
            si.release();
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

    public static class O extends Thread {

        public void bond() {
            System.out.println("O is bonding now");
        }

        public void execute() throws InterruptedException {
            o.acquire(1); //cekame edno izvestuvanje,poraka
            siHere.acquire();
            oHere.release();
            ready.acquire(); //signal od strana na koordinatorot na Si deka treba da pocneme so povik na metodot bond()
            bond();
            o.release();
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
