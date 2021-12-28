package aud4;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Vinegar {

    static Semaphore c = new Semaphore(2);
    static Semaphore h = new Semaphore(4);
    static Semaphore o = new Semaphore(2);

    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            threads.add(new C());
            threads.add(new H());
            threads.add(new H());
            threads.add(new O());
        }
        // run all threads in background
        for (Thread thred : threads) {
            thred.start();
        }
        // after all of them are started, wait each of them to finish for maximum 2_000 ms
        for (Thread thred : threads) {
            thred.join(2000);
        }
        // for each thread, terminate it if it is not finished
        for (Thread thred : threads) {
            if (thred.isAlive()) {
                System.out.println("Possible deadlock!");
                thred.interrupt();
            }
        }
        System.out.println("Process finished.");

    }

    static class C extends Thread {

        public void execute() throws InterruptedException {
            // at most 2 atoms should print this in parallel
            c.acquire();
            System.out.println("C here.");
            // after all atoms are present, they should start with the bonding process together
            System.out.println("Molecule bonding.");
            Thread.sleep(100);// this represent the bonding process
            System.out.println("C done.");
            // only one atom should print the next line, representing that the molecule is created
            c.release();
            System.out.println("Molecule created.");
        }
    }

    static class H extends Thread {

        public void execute() throws InterruptedException {
            // at most 4 atoms should print this in parallel
            h.acquire();
            System.out.println("H here.");
            // after all atoms are present, they should start with the bonding process together
            System.out.println("Molecule bonding.");
            Thread.sleep(100);// this represent the bonding process
            System.out.println("H done.");
            // only one atom should print the next line, representing that the molecule is created
            h.release();
            System.out.println("Molecule created.");
        }
    }

    static class O extends Thread {

        public void execute() throws InterruptedException {
            // at most 2 atoms should print this in parallel
            o.acquire();
            System.out.println("O here.");
            // after all atoms are present, they should start with the bonding process together
            System.out.println("Molecule bonding.");
            Thread.sleep(100);// this represent the bonding process
            System.out.println("O done.");
            // only one atom should print the next line, representing that the molecule is created
            o.release();
            System.out.println("Molecule created.");
        }
    }
}
