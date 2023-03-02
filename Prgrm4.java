import java.util.Arrays;
import java.util.Scanner;

public class Prgrm4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the values of the numeric vector
        System.out.print("Enter the values of the numeric vector (separated by space): ");
        String[] input = scanner.nextLine().split(" ");
        double[] vector = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            vector[i] = Double.parseDouble(input[i]);
        }

        // Normalize the numeric vector using Z-score normalization
        double[] normalizedVector = zScoreNormalization(vector);

        // Print the normalized vector
        System.out.println("Normalized vector: " + Arrays.toString(normalizedVector));
    }

    // Method to normalize a numeric vector using Z-score normalization
    private static double[] zScoreNormalization(double[] vector) {
        double mean = 0;
        for (double value : vector) {
            mean += value;
        }
        mean /= vector.length;

        double variance = 0;
        for (double value : vector) {
            variance += Math.pow(value - mean, 2);
        }
        variance /= vector.length;

        double stdDeviation = Math.sqrt(variance);

        double[] normalizedVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - mean) / stdDeviation;
        }

        return normalizedVector;
    }
}
