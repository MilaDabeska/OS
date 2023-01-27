package exams.synchronization;

import java.util.concurrent.Semaphore;

public class StudentProject {

    static Semaphore teachers;
    static Semaphore students;
    static Semaphore studentHere;
    static Semaphore canWork;
    static Semaphore finishedWorking;
    static Semaphore canLeave;

    static Semaphore lock;
    static int count;

    public static void init() {
        teachers = new Semaphore(2);
        students = new Semaphore(7);
        studentHere = new Semaphore(0);
        canWork = new Semaphore(0);
        finishedWorking = new Semaphore(0);
        canLeave = new Semaphore(0);
        lock = new Semaphore(1);
        count = 0;
    }

    static class Teacher extends Thread {

        public void work() {
            System.out.println("Teacher is working...");
        }

        public void execute() throws InterruptedException {
            lock.acquire();
            studentHere.acquire(7);
            count++;

            if (count == 2) {
                count = 0;
                canWork.release(8);
                finishedWorking.acquire(8);
                canLeave.release(7);

                finishedWorking.release();
            }
        }
    }

    static class Student extends Thread {

        public void work() {
            System.out.println("Student is working...");
        }

        public void execute() throws InterruptedException {
            students.acquire();
            studentHere.release();

            canWork.acquire();
            work();
            finishedWorking.release();
            canLeave.acquire();

            students.release();
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
