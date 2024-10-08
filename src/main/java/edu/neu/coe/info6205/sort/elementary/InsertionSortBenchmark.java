package edu.neu.coe.info6205.sort.elementary;

import java.util.Arrays;
import java.util.Random;

public class InsertionSortBenchmark {

    private static Integer[] generateRandomArray(int n) {
        Random random = new Random();
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }
     
    private static Integer[] generateOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        return array;
    }
    
    private static Integer[] generateReverseOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = n - i;
        }
        return array;
    }
    
    private static Integer[] generatePartiallyOrderedArray(int n) {
        Integer[] array = generateOrderedArray(n);
        Random random = new Random();
        int shuffleCount = n / 10; // Shuffle 10% of the elements
        for (int i = 0; i < shuffleCount; i++) {
            int index1 = random.nextInt(n);
            int index2 = random.nextInt(n);
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        return array;
    }

    private static long timeSort(Integer[] arrayToSort) {
        // Warm-up phase
        InsertionSort<Integer> sorter = new InsertionSort<>();
        for (int i = 0; i < 10; i++) {
            Integer[] copy = Arrays.copyOf(arrayToSort, arrayToSort.length);
            sorter.sort(copy);
        }
    
        // Timing phase
        Integer[] copy = Arrays.copyOf(arrayToSort, arrayToSort.length);
        long startTime = System.nanoTime();
        sorter.sort(copy);
        long endTime = System.nanoTime();
    
        // Return elapsed time in nanoseconds
        return endTime - startTime;
    }    
    

    public static void main(String[] args) {
        int initialN = 1000;
        int maxN = 16000; // Adjust as needed
        int runs = 5; // Number of runs for averaging
    
        System.out.println("n\tRandom\tOrdered\tPartial\tReverse");
    
        for (int n = initialN; n <= maxN; n *= 2) {
            // Accumulators for total times
            long totalRandomTime = 0;
            long totalOrderedTime = 0;
            long totalPartialTime = 0;
            long totalReverseTime = 0;
    
            for (int i = 0; i < runs; i++) {
                // Generate arrays
                Integer[] randomArray = generateRandomArray(n);
                Integer[] orderedArray = generateOrderedArray(n);
                Integer[] partialArray = generatePartiallyOrderedArray(n);
                Integer[] reverseArray = generateReverseOrderedArray(n);
    
                // Time the sorts
                totalRandomTime += timeSort(randomArray);
                totalOrderedTime += timeSort(orderedArray);
                totalPartialTime += timeSort(partialArray);
                totalReverseTime += timeSort(reverseArray);
            }
    
            // Calculate average times
            double avgRandomTime = totalRandomTime / (double) runs;
            double avgOrderedTime = totalOrderedTime / (double) runs;
            double avgPartialTime = totalPartialTime / (double) runs;
            double avgReverseTime = totalReverseTime / (double) runs;
    
            // Convert nanoseconds to milliseconds
            avgRandomTime /= 1_000_000.0;
            avgOrderedTime /= 1_000_000.0;
            avgPartialTime /= 1_000_000.0;
            avgReverseTime /= 1_000_000.0;
    
            // Print the results
            System.out.printf("%d\t%.3f\t%.3f\t%.3f\t%.3f\n", n, avgRandomTime, avgOrderedTime, avgPartialTime, avgReverseTime);
        }
    }
    
}
