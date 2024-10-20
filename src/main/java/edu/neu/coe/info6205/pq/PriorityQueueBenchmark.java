package edu.neu.coe.info6205.pq;

import java.util.*;
import java.util.function.Consumer;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import java.util.function.Supplier;

public class PriorityQueueBenchmark {

    public static void main(String[] args) {
        int maxM = 4095; // Maximum heap capacity
        int runs = 10; // Number of repetitions for averaging
        int[] heapSizes = {512, 1024, 2048, 4095}; // Different sizes of heap M for log-log plot
        int insertions = 16000;
        int deletions = 4000;

        // Seed for reproducibility
        Random random = new Random(12345); // Fixed seed

        // Data structures to store the results
        Map<String, List<Double>> benchmarkResults = new LinkedHashMap<>();
        Map<String, List<Integer>> maxSpilledElements = new LinkedHashMap<>(); // Store highest priority spilled elements

        // Initialize results lists
        benchmarkResults.put("Binary Heap", new ArrayList<>());
        benchmarkResults.put("Binary Heap with Floyd's Trick", new ArrayList<>());
        benchmarkResults.put("4-ary Heap", new ArrayList<>());
        benchmarkResults.put("4-ary Heap with Floyd's Trick", new ArrayList<>());

        maxSpilledElements.put("Binary Heap", new ArrayList<>());
        maxSpilledElements.put("Binary Heap with Floyd's Trick", new ArrayList<>());
        maxSpilledElements.put("4-ary Heap", new ArrayList<>());
        maxSpilledElements.put("4-ary Heap with Floyd's Trick", new ArrayList<>());

        // Perform benchmarks for each heap size M
        for (int M : heapSizes) {
            System.out.println("Benchmarking with M = " + M);

            // Binary Heap
            double timeBinaryHeap = benchmarkPriorityQueue("Binary Heap", M, insertions, deletions, false, false, random, runs, maxSpilledElements.get("Binary Heap"));
            benchmarkResults.get("Binary Heap").add(timeBinaryHeap);
            System.out.println("Binary Heap average time: " + timeBinaryHeap + " ms");

            // Binary Heap with Floyd's Trick
            double timeBinaryHeapFloyd = benchmarkPriorityQueue("Binary Heap with Floyd's Trick", M, insertions, deletions, false, true, random, runs, maxSpilledElements.get("Binary Heap with Floyd's Trick"));
            benchmarkResults.get("Binary Heap with Floyd's Trick").add(timeBinaryHeapFloyd);
            System.out.println("Binary Heap with Floyd's Trick average time: " + timeBinaryHeapFloyd + " ms");

            // 4-ary Heap
            double timeFourAryHeap = benchmarkPriorityQueue("4-ary Heap", M, insertions, deletions, true, false, random, runs, maxSpilledElements.get("4-ary Heap"));
            benchmarkResults.get("4-ary Heap").add(timeFourAryHeap);
            System.out.println("4-ary Heap average time: " + timeFourAryHeap + " ms");

            // 4-ary Heap with Floyd's Trick
            double timeFourAryHeapFloyd = benchmarkPriorityQueue("4-ary Heap with Floyd's Trick", M, insertions, deletions, true, true, random, runs, maxSpilledElements.get("4-ary Heap with Floyd's Trick"));
            benchmarkResults.get("4-ary Heap with Floyd's Trick").add(timeFourAryHeapFloyd);
            System.out.println("4-ary Heap with Floyd's Trick average time: " + timeFourAryHeapFloyd + " ms");
        }

        // Output the max spilled elements
        System.out.println("\nHighest priority spilled elements for each implementation:");
        maxSpilledElements.forEach((key, values) -> {
            System.out.println(key + ": " + values);
        });

        // You can now output the benchmarkResults map to a CSV or use for plotting in Google Sheets
    }

    private static double benchmarkPriorityQueue(String description, int M, int insertions, int deletions,
                                                 boolean is4ary, boolean useFloyd, Random random, int runs,
                                                 List<Integer> maxSpilled) {
        // Supplier to create a new priority queue instance
        Supplier<PriorityQueue<Integer>> supplier = () -> {
            PriorityQueue<Integer> pq;
            if (is4ary) {
                pq = new FourAryHeap<Integer>(M, Comparator.naturalOrder(), useFloyd);
            } else {
                pq = new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), useFloyd);
            }
            return pq;
        };

        // Function to perform the operations
        Consumer<PriorityQueue<Integer>> function = pq -> {
            List<Integer> spilledElements = new ArrayList<>();
            // Insert elements
            for (int i = 0; i < insertions; i++) {
                int element = random.nextInt();
                if (pq.size() >= M) {
                    try {
                        int spilled = pq.take();  // Remove root if size exceeds M
                        spilledElements.add(spilled); // Keep track of spilled elements
                    } catch (PQException e) {
                        // Handle empty queue exception
                    }
                }
                pq.give(element);
            }

            // Remove elements
            for (int i = 0; i < deletions; i++) {
                try {
                    pq.take();
                } catch (PQException e) {
                    // Handle empty queue exception
                }
            }

            // Report the highest priority spilled element
            spilledElements.stream().max(Comparator.naturalOrder())
                    .ifPresent(maxSpilled::add); // Add the max element to the result list
        };

        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(description, function);

        return benchmark.runFromSupplier(supplier, runs);
    }

}
