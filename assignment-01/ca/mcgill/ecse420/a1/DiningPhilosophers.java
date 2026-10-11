package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

  public static void main(String[] args) {

    int numberOfPhilosophers = 5; // set number of philosophers
    Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];

    // Each chopstick is represented by a ReentrantLock
    ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];
    for (int x = 0; x < numberOfPhilosophers; x++) {
      chopsticks[x] = new ReentrantLock(true); // the true condition helps prevents starvation
    }

    // Left chopsticks are represented by y and right chopstick by y+1 modulo numberOfPhilosophers
    for (int y = 0; y < numberOfPhilosophers; y++) {
      ReentrantLock leftChopstick = chopsticks[y];
      ReentrantLock rightChopstick =
          (y == numberOfPhilosophers - 1) ? chopsticks[0] : chopsticks[y + 1];
      philosophers[y] =
          new Philosopher(
              y + 1, leftChopstick, rightChopstick); // Philosopher numbering starts at 1
    }

    ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

    for (int z = 0; z < numberOfPhilosophers; z++) {
      executor.execute(philosophers[z]);
    }
    executor.shutdown();
  }

  public static class Philosopher implements Runnable {

    private int number;
    private ReentrantLock leftChopstick;
    private ReentrantLock rightChopstick;

    public Philosopher(int number, ReentrantLock leftChopstick, ReentrantLock rightChopstick) {
      this.number = number;
      this.leftChopstick = leftChopstick;
      this.rightChopstick = rightChopstick;
    }

    @Override
    public void run() {
      // Odd philosophers acquire left first
      if (this.number % 2 == 1) {
        // Assuming the philosophers are eating from an infinite bowl of rice
        while (true) {
          leftChopstick.lock();
          try {
            rightChopstick.lock();
            try {
              System.out.println("Yummy " + this.number + " is  eating");
              TimeUnit.SECONDS.sleep(1);
              System.out.println("Aaaaaah *belch* " + this.number + " is done eating");
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            } finally {
              rightChopstick.unlock();
            }
          } finally {
            leftChopstick.unlock();
          }

          try {
            TimeUnit.SECONDS.sleep(1); // represents the philosophers thinking
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      } else { // Even philosophers acquire right first
        while (true) {
          rightChopstick.lock();
          try {
            leftChopstick.lock();
            try {
              System.out.println("Yummy " + this.number + " is eating");
              TimeUnit.SECONDS.sleep(1);
              System.out.println("Aaaaaah *belch* " + this.number + " is done eating");
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            } finally {
              leftChopstick.unlock();
            }
          } finally {
            rightChopstick.unlock();
          }

          try {
            TimeUnit.SECONDS.sleep(1); // represents the philosophers thinking
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }
}
