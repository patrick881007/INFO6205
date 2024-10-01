// ThreeSumBenchmark.java
package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;

import java.util.Arrays;
import java.util.Random;

public class ThreeSumTiming {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000};
        Random rand = new Random();

        System.out.println("N, Quadratic, Quadrithmic, QuadraticWithCalipers, Cubic");

        for (int N : sizes) {
            // Generate sorted distinct array
            int[] array = generateSortedDistinctArray(N, rand);

            // Quadratic
            ThreeSumQuadratic quadratic = new ThreeSumQuadratic(array);
            long quadraticTime = timeAlgorithm(quadratic);

            // Quadrithmic
            ThreeSumQuadrithmic quadrithmic = new ThreeSumQuadrithmic(array);
            long quadrithmicTime = timeAlgorithm(quadrithmic);

            // Quadratic with Calipers
            ThreeSumQuadraticWithCalipers calipers = new ThreeSumQuadraticWithCalipers(array);
            long calipersTime = timeAlgorithm(calipers);

            // Cubic (using a naive implementation)
            ThreeSumCubic cubic = new ThreeSumCubic(array);
            long cubicTime = timeAlgorithm(cubic);

            System.out.printf("%d, %d, %d, %d, %d%n", N, quadraticTime, quadrithmicTime, calipersTime, cubicTime);
        }
    }

    private static int[] generateSortedDistinctArray(int N, Random rand) {
        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = rand.nextInt(2 * N) - N; // Range [-N, N]
        }
        Arrays.sort(array);
        // Remove duplicates
        return Arrays.stream(array).distinct().toArray();
    }

    private static long timeAlgorithm(ThreeSum algorithm) {
        try (Stopwatch sw = new Stopwatch()) {
            algorithm.getTriples();
            return sw.lap();
        }
    }
}
