import java.util.Arrays;

public class Prgrm1{
    
    // Smooths the data by bin means
    public static double[] binMeans(double[] data, int numBins) {
        double[] smoothedData = new double[data.length];
        int binSize = data.length / numBins;
        for (int i = 0; i < numBins; i++) {
            int start = i * binSize;
            int end = (i + 1) * binSize;
            if (i == numBins - 1) {
                end = data.length;
            }
            double binSum = 0.0;
            for (int j = start; j < end; j++) {
                binSum += data[j];
            }
            double binMean = binSum / (end - start);
            for (int j = start; j < end; j++) {
                smoothedData[j] = binMean;
            }
        }
        return smoothedData;
    }
    
    // Smooths the data by bin boundaries
    public static double[] binBoundaries(double[] data, int numBins) {
        double[] smoothedData = new double[data.length];
        double[] sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);
        int binSize = sortedData.length / numBins;
        for (int i = 0; i < numBins; i++) {
            double binMin = sortedData[i * binSize];
            double binMax = sortedData[(i + 1) * binSize - 1];
            int start = i * binSize;
            int end = (i + 1) * binSize;
            for (int j = start; j <end; j++) {
                if (sortedData[j]-binMin< binMax-sortedData[j]) {
                    smoothedData[j] = binMin;
                }
                else
                {
                    smoothedData[j]=binMax;
                }
            }
        }
        return smoothedData;
    }
    
    public static void main(String[] args) {
        double[] data = {4,8,9,15,21,21,24,25,26,28,29,34};
        int numBins = 3;
        
        // Smooth the data by bin means
        double[] smoothedData = binMeans(data, numBins);
        System.out.println("Smoothed data by bin means:");
        for (double value : smoothedData) {
            System.out.print(value + " ");
        }
        System.out.println();
        
        // Smooth the data by bin boundaries
        smoothedData = binBoundaries(data, numBins);
        System.out.println("Smoothed data by bin boundaries:");
        for (double value : smoothedData) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
