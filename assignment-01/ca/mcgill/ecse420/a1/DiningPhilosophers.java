package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

	public static void main(String[] args) {

//		Create single lock
//		ReentrantLock chopstick = new ReentrantLock();

		int numberOfPhilosophers = 5;
		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];

		// Chopsticks
		ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];
		for (int x = 0; x<numberOfPhilosophers; x++) {
			chopsticks[x] = new ReentrantLock(true);
		}

		for (int y = 0; y<numberOfPhilosophers; y++) {
			ReentrantLock left_chopstick = chopsticks[y];
			ReentrantLock right_chopstick = (y == numberOfPhilosophers-1)? chopsticks[0] : chopsticks[y+1];
			philosophers[y] = new Philosopher(y+1, left_chopstick, right_chopstick);
		}

		ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

		for (int z = 0; z<numberOfPhilosophers; z++) {
			executor.execute(philosophers[z]);
		}
		executor.shutdown(); // not permanent
	}

	public static class Philosopher implements Runnable {

		private int number; // make private + setter??
		private ReentrantLock left_chopstick;
		private ReentrantLock right_chopstick;

		public Philosopher(int number, ReentrantLock left_chopstick, ReentrantLock right_chopstick){
			this.number = number;
			this.left_chopstick = left_chopstick;
			this.right_chopstick = right_chopstick;
		}

		@Override
		public void run() {
			// assuming the philosophers must eat continuously
			if (this.number%2==1){
				while (true){
					left_chopstick.lock();
					try{
						right_chopstick.lock();
						try{
							System.out.println("Yummy "+ this.number +" is  eating");
							TimeUnit.SECONDS.sleep(3);
							System.out.println("Aaaaaah *burp* "+ this.number +" is done eating");
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						} finally {
							right_chopstick.unlock();
						}
					}finally {
						left_chopstick.unlock();
					}
				}
			}else{
				while (true){
					left_chopstick.lock();
					try{
						right_chopstick.lock();
						try{
							System.out.println("Yummy "+ this.number  +" is  eating");
							TimeUnit.SECONDS.sleep(3);
							System.out.println("Aaaaaah *burp* "+ this.number  +" is done eating");
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						} finally {
							right_chopstick.unlock();
						}
					}finally {
						left_chopstick.unlock();
					}
				}
			}
		}

	}

}
