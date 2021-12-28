package aud4;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class BasketballTournament {

    public static void main(String[] args) throws InterruptedException {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background
        for (Player player : threads) {
            player.start();
        }
        // after all of them are started, wait each of them to finish for maximum 5_000 ms
        for (Player player : threads) {
            player.join(5000);
        }
        // for each thread, terminate it if it is not finished
        for (Player player : threads) {
            if (player.isAlive()) {
                System.out.println("Possible deadlock!");
                player.interrupt();
            }
        }
        System.out.println("Tournament finished.");
    }
}

class Player extends Thread {

    static Semaphore hall = new Semaphore(20);
    static Semaphore dressingRoom = new Semaphore(10);
    static Semaphore game = new Semaphore(1);
    int roomPlayers = 0, gamePlayers = 0, finishedGamePlayers = 0;

    public void execute() throws InterruptedException {
        // at most 20 players should print this in parallel
        hall.acquire();
        System.out.println("Player inside.");
        dressingRoom.acquire();
        roomPlayers++;
        // at most 10 players may enter in the dressing room in parallel
        System.out.println("In dressing room.");
        if (roomPlayers == 10) {
            roomPlayers = 0;
            dressingRoom.release(10);
        }
        Thread.sleep(10);// this represent the dressing time
        gamePlayers++;
        // after all players are ready, they should start with the game together
        if (gamePlayers == 20) {
            gamePlayers = 0;
            game.acquire();
            System.out.println("Game started.");
        }
        Thread.sleep(100);// this represent the game duration
        System.out.println("Player done.");
        finishedGamePlayers++;
        // only one player should print the next line, representing that the game has finished
        if (finishedGamePlayers == 20) {
            System.out.println("Game finished.");
            finishedGamePlayers = 0;
            hall.release(20);
            game.release();
        }
    }
}
