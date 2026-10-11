package ca.mcgill.ecse420.a1;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/** Demonstrates deterministic deadlock caused when acquiring two locks in opposite orders. */
public class Question2 {

  // The CyclicBarrier is the source of the determinism
  private static final CyclicBarrier barrier = new CyclicBarrier(2);

  public static void main(String[] args) {

    // Create locks representing non descript objects A and B
    ReentrantLock objectA = new ReentrantLock();
    ReentrantLock objectB = new ReentrantLock();

    // Initialize 2 threads
    ExecutorService executor = Executors.newFixedThreadPool(2);

    // Execute 2 threads
    executor.execute(new Hello(objectA, objectB));
    executor.execute(new Goodbye(objectA, objectB));

    // Prevent future task from being added
    executor.shutdown();
  }

  public static class Hello implements Runnable {

    ReentrantLock objectA;
    ReentrantLock objectB;

    // Program run by first thread
    public Hello(ReentrantLock objectA, ReentrantLock objectB) {
      this.objectA = objectA;
      this.objectB = objectB;
    }

    @Override
    public void run() {
      objectA.lock(); // Hello requests objectA lock first
      System.out.println("Hello program, has acquire lock a!");
      try {
        try {
          barrier.await(); // Forces a halt until the Goodbye thread reaches the barrier as well
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        objectB.lock();
        System.out.println("Hello program, has acquire lock b!");
        try {
          System.out.println("Hello World, I am not deadlocked!");
        } finally {
          objectB.unlock();
        }
      } finally {
        objectA.unlock();
      }
    }
  }

  // Program run by second thread
  public static class Goodbye implements Runnable {

    ReentrantLock objectA;
    ReentrantLock objectB;

    public Goodbye(ReentrantLock objectA, ReentrantLock objectB) {
      this.objectA = objectA;
      this.objectB = objectB;
    }

    @Override
    public void run() {
      objectB.lock(); // Goodbye requests objectB lock first
      System.out.println("Goodbye program, has acquire lock b!");
      try {
        try {
          barrier.await();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        objectA.lock();
        System.out.println("Goodbye program, has acquire lock a!");
        try {
          System.out.println("Goodbye World, I am not deadlocked!");
        } finally {
          objectA.unlock();
        }
      } finally {
        objectB.unlock();
      }
    }
  }
}
