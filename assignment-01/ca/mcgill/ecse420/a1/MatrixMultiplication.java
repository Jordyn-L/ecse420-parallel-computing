package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors;

public class MatrixMultiplication {

	private static final int NUMBER_THREADS = 8;
	private static final int MATRIX_SIZE = 100;

	public static void main(String[] args) {
	
	// Generate two random matrices, same size
	double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	
	// Question 1.2

	// printMatrix(a);
	// printMatrix(b);
	// printMatrix(parallelMultiplyMatrix(a, b));

	// true is for sequential computation

	// executionTime(a, b, true);
	// executionTime(a, b, false);

	}
	
	/**
	 * Question 1.1
	 * Returns the result of a sequential matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {

		// matrix to hold results
		double matrix[][] = new double[MATRIX_SIZE][MATRIX_SIZE];
		
		for (int row = 0; row < MATRIX_SIZE; row++){
			for (int col = 0; col < MATRIX_SIZE; col++) {
				// for the move accros the row and column
				for (int helperCount = 0; helperCount < MATRIX_SIZE ; helperCount++) {

					// add the new value to the current value then assign the new value
					matrix[row][col] += a[row][helperCount] * b[helperCount][col];
				}
			}
		}
		
		return matrix;
	}
	
	/**
	 * Question 1.2 (Thread Pool)
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {

		// reference for every task object
		double[][] resultMatrix = new double[MATRIX_SIZE][MATRIX_SIZE];
 
		// re-use threads to complete the tasks
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);

		// create Tasks for each row
		for (int rowCount = 0; rowCount < MATRIX_SIZE; rowCount++) {
			executor.execute(new CalculateMatrixMultiplicationRow(a, b, rowCount, resultMatrix));
		}

		// stop accepting new tasks
		executor.shutdown();
		// wait for execution to wrap up after shutdown
		while (!executor.isTerminated()) { }

		return resultMatrix;
	}

	// nested Task class for thread pool
	public static class CalculateMatrixMultiplicationRow implements Runnable {

		// each thread divides the work by calculating a row for the result matrix
		private double[][] a;
		private double[][] b;
		private int row; // result row number and row number from matrix a
		private double[][] resultMatrix; 

		public CalculateMatrixMultiplicationRow(double[][] a, double[][] b, int row, double[][] resultMatrix) {

			this.a = a;
			this.b = b;
			this.row = row;
			this.resultMatrix = resultMatrix;
		}

		// calculates the desired result row
		@Override
		public void run() {

			// for testing threads
			// System.out.println("Processing row " + this.row + " on " + Thread.currentThread().getName());

			for (int col = 0; col < MATRIX_SIZE; col++) {
				// for the move accros the row and column
				for (int helperCount = 0; helperCount < MATRIX_SIZE; helperCount++) {
					// row never changes
					this.resultMatrix[this.row][col] += this.a[this.row][helperCount] * this.b[helperCount][col];
					
				}
				
			}
			
		}
	}

	/**
	 * Populates a matrix of given size with randomly generated integers between 0-10.
	 * @param numRows number of rows
	 * @param numCols number of cols
	 * @return matrix
	 */
	private static double[][] generateRandomMatrix (int numRows, int numCols) {

		double matrix[][] = new double[numRows][numCols];

		for (int row = 0 ; row < numRows ; row++ ) {
			for (int col = 0 ; col < numCols ; col++ ) {
				matrix[row][col] = (double) ((int) (Math.random() * 10.0));
			}
		}
		return matrix;
    }

	// helper methods 

	/**
	 * print any sized matrix to the screen
	 * @param matrix is the printed matrix
	 */
	public static void printMatrix(double[][] matrix) {

		for (int currentRow = 0; currentRow < MATRIX_SIZE; currentRow++) {
			for (int currentColumn = 0; currentColumn < MATRIX_SIZE; currentColumn++) {
				// formats the output as left-aligned double with a width of 8 and 1 decimal place ("-8.1d")
				System.out.printf("%-8.1f", matrix[currentRow][currentColumn]); // print one matrix value
			}
			// new line
			System.out.println();
		}
	}

	/**
	 * Question 1.3
	 * print time to execute sequential or parallel matrix multiplication
	 * @param a first matrix
	 * @param b second matrix
	 * @param sequential is to choose which method to run. true is sequential, false is parallel
	 */
	public static void executionTime( double[][] a, double[][] b, boolean sequential){
		
		String methodName;
		double executionTime;
		long startTime = 0;
		long endTime = 0;


		if (sequential) {

			// for printing
			methodName = "sequentialMultiplyMatrix";

			startTime = System.nanoTime();
			sequentialMultiplyMatrix(a, b);
			endTime = System.nanoTime();

		}

		// parallel matrix multiplication method is called instead
		else {
			
			// for printing
			methodName = "parallelMultiplyMatrix";
			
			startTime = System.nanoTime();
			parallelMultiplyMatrix(a, b);
			endTime = System.nanoTime();

		}

		// results
		// convert nanoseconds to seconds
		executionTime = (endTime - startTime) / 1000000000.0;

		// print final results
		int threadAmount;
		if (!sequential) {
			threadAmount = NUMBER_THREADS;
		}
		else {
			threadAmount = 1;
		}
		
		System.out.printf("time to run %s with square matrix of size %d and %d threads: %f seconds\n",
		methodName, MATRIX_SIZE, threadAmount,  executionTime);
	}
}
