package ca.mcgill.ecse420.a1;

public class MatrixMultiplication {
	
	private static final int NUMBER_THREADS = 1;
	private static final int MATRIX_SIZE = 9;

	public static void main(String[] args) {
	
	// Generate two random matrices, same size
	double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
	double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);

	executionTime(a, b, true);

	//printMatrix(sequentialMultiplyMatrix(a, b));
	// parallelMultiplyMatrix(a, b);

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
	 * Question 1.2
	 * Returns the result of a concurrent matrix multiplication
	 * The two matrices are randomly generated
	 * @param a is the first matrix
	 * @param b is the second matrix
	 * @return the result of the multiplication
	 * */
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		System.out.println("in parallel ;)");
		return null;

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


	/** helper methods **/


	/**
	 * print any sized matrix to the screen
	 * @param matrix is the printed matrix
	 */
	public static void printMatrix(double[][] matrix) {
		// [row][col]
		for (int currentRow = 0; currentRow < MATRIX_SIZE; currentRow++) {
			for (int currentColumn = 0; currentColumn < MATRIX_SIZE; currentColumn++) {
				// farmats the output as left-aligned double with a width of 8 and 1 decimal place ("-8.1d")
				System.out.printf("%-8.1f", matrix[currentRow][currentColumn]); // print one matrix value
			}
			//space between rows
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
			// hard coded
			sequentialMultiplyMatrix(a, b);
			endTime = System.nanoTime();

		}

		// parallel matrix multiplication method is called instead
		else {
			// for printing
			methodName = "parallelMultiplyMatrix";
			
			startTime = System.nanoTime();
			// hard coded
			parallelMultiplyMatrix(a, b);
			endTime = System.nanoTime();

		}

		// results
			executionTime = (endTime - startTime) / 1000000000.0; // convert nanoseconds to seconds
		// print final results
		System.out.printf("time to run %s: %f seconds\n",  methodName, executionTime);

	}


}
