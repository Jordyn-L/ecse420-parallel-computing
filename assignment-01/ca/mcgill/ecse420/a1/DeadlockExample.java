package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExample {

    public static void main(String[] args) {

        ReentrantLock object_a = new ReentrantLock();
        ReentrantLock object_b = new ReentrantLock();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(new Hello(object_a, object_b));
        executor.execute(new Goodbye(object_a, object_b));
    }

    public static class Hello implements Runnable {

        ReentrantLock object_a;
        ReentrantLock object_b;

        public Hello(ReentrantLock a, ReentrantLock b){
            object_a = a;
            object_b = b;
        }

        @Override
        public void run() {
            object_a.lock();
            System.out.println("Hello program, has acquire lock a!");
            try {
                object_b.lock();
                System.out.println("Hello program, has acquire lock b!");
                try {
                    System.out.println("Hello World, I am not deadlocked!");
                }finally {
                    object_b.unlock();
                }
            } finally {
                object_a.unlock();
            }
        }


    }

    public static class Goodbye implements Runnable {

        ReentrantLock object_a;
        ReentrantLock object_b;

        public Goodbye(ReentrantLock a, ReentrantLock b){
            object_a = a;
            object_b = b;
        }

        @Override
        public void run() {
            object_b.lock();
            System.out.println("Goodbye program, has acquire lock b!");
            try {
                object_a.lock();
                System.out.println("Goodbye program, has acquire lock a!");
                try {
                    System.out.println("Goodbye World, I am not deadlocked!");
                }finally {
                    object_a.unlock();
                }
            } finally {
                object_b.unlock();
            }
        }


    }


}
