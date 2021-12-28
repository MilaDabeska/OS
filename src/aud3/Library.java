package aud3;

import java.util.ArrayList;
import java.util.List;

public class Library {
    public static void main(String[] args) throws InterruptedException {
        List<Member> members = new ArrayList<>();
        SemaphoreLibrary library = new SemaphoreLibrary(10);

        for (int i = 0; i < 10; i++) {
            Member member = new Member("aud3.Member " + i, library); //novi clenovi
            members.add(member); //se dodavaat novite clenovi
        }

        for (Member member : members) {
            member.start();
        }
        for (Member member : members) {
            member.join(2000);
        }

        System.out.println("Successfully");
    }
}

class Member extends Thread {
    private String name;
    private SemaphoreLibrary library;

    public Member(String name, SemaphoreLibrary library) {
        this.name = name;
        this.library = library;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println("aud3.Member " + i + " returns book");
            try {
                library.returnBook("Book " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("aud3.Member " + i + " borrows book");
            try {
                library.borrowBook();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}