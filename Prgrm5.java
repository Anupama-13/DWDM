import java.util.Arrays;
import java.util.Scanner;

public class Prgrm5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the values of the numeric vector
        System.out.print("Enter the values of the numeric vector (separated by space): ");
        String[] input = scanner.nextLine().split(" ");
        double[] vector = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            vector[i] = Double.parseDouble(input[i]);
        }

        // Normalize the numeric vector using decimal scaling
        double[] normalizedVector = decimalScaling(vector);

        // Print the normalized vector
        System.out.println("Normalized vector: " + Arrays.toString(normalizedVector));
    }

    // Method to normalize a numeric vector using decimal scaling
    private static double[] decimalScaling(double[] vector) {
        double max = Double.MIN_VALUE;
        for (double value : vector) {
            if (Math.abs(value) > max) {
                max = Math.abs(value);
            }
        }

        int d = (int) Math.ceil(Math.log10(max));

        double[] normalizedVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / Math.pow(10, d);
        }

        return normalizedVector;
    }
}
