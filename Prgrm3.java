import java.util.Arrays;
import java.util.Scanner;

public class Prgrm3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the values of the numeric vector
        System.out.print("Enter the values of the numeric vector (separated by space): ");
        String[] input = scanner.nextLine().split(" ");
        double[] vector = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            vector[i] = Double.parseDouble(input[i]);
        }

        // Normalize the numeric vector using min-max normalization
        double[] normalizedVector = minMaxNormalization(vector);

        // Print the normalized vector
        System.out.println("Normalized vector: " + Arrays.toString(normalizedVector));
    }

    // Method to normalize a numeric vector using min-max normalization
    private static double[] minMaxNormalization(double[] vector) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double value : vector) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        double[] normalizedVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - min) / (max - min);
        }

        return normalizedVector;
    }
}
