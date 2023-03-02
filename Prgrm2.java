import java.util.Scanner;

public class Prgrm2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the values of the two numeric attributes
        System.out.print("Enter the values of attribute 1 (separated by space): ");
        String[] input1 = scanner.nextLine().split(" ");
        double[] attribute1 = new double[input1.length];
        for (int i = 0; i < input1.length; i++) {
            attribute1[i] = Double.parseDouble(input1[i]);
        }

        System.out.print("Enter the values of attribute 2 (separated by space): ");
        String[] input2 = scanner.nextLine().split(" ");
        double[] attribute2 = new double[input2.length];
        for (int i = 0; i < input2.length; i++) {
            attribute2[i] = Double.parseDouble(input2[i]);
        }

        // Calculate the mean of the two attributes
        double mean1 = mean(attribute1);
        double mean2 = mean(attribute2);

        // Calculate the standard deviation of the two attributes
        double sd1 = standardDeviation(attribute1, mean1);
        double sd2 = standardDeviation(attribute2, mean2);

        // Calculate the covariance between the two attributes
        double covariance = covariance(attribute1, attribute2, mean1, mean2);

        // Calculate the Pearson correlation coefficient
        double correlationCoefficient = covariance / (sd1 * sd2);

        // Determine whether the two attributes are positively correlated, negatively correlated, or independent
        if (correlationCoefficient > 0) {
            System.out.println("The two attributes are positively correlated.");
        } else if (correlationCoefficient < 0) {
            System.out.println("The two attributes are negatively correlated.");
        } else {
            System.out.println("The two attributes are independent.");
        }
    }

    // Method to calculate the mean of an array of doubles
    private static double mean(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    // Method to calculate the standard deviation of an array of doubles
    private static double standardDeviation(double[] array, double mean) {
        double sum = 0;
        for (double value : array) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / array.length);
    }

    // Method to calculate the covariance between two arrays of doubles
    private static double covariance(double[] array1, double[] array2, double mean1, double mean2) {
        double sum = 0;
        for (int i = 0; i < array1.length; i++) {
            sum += (array1[i] - mean1) * (array2[i] - mean2);
        }
        return sum / array1.length;
    }
}
