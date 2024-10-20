package edu.neu.coe.info6205;

import java.util.Arrays;

/**
 * Class to provide the functionality of binary search.
 * NOTE that it is essentially the same as the Arrays.binarySearch method.
 */
public class BinarySearch {
    /**
     * Main program
     *
     * @param args Command-line args.
     */
    public static void main(String[] args) {
        int[] ar = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int res1 = binarySearch(ar, 0, ar.length - 1, 3);
        System.out.println(res1); // Expected output: 2
        int res2 = Arrays.binarySearch(ar, 0, ar.length, 3);
        System.out.println(res2); // Expected output: 2
    }

    /**
     * Method to do binary search.
     *
     * @param a    the ordered array.
     * @param from the first index of interest.
     * @param to   the first subsequent index that is NOT of interest.
     * @param key  the value we are searching for.
     * @return the index of the element whose value is <code>key</code>, or -1 if there is no such element.
     */
    static int binarySearch(int[] a, int from, int to, int key) {
        int lo = from;
        int hi = to;
        while (lo <= hi) {  // Use <= because we're working with inclusive ranges
            int mid = lo + (hi - lo) / 2;  // Calculate the middle point
            if (a[mid] == key) {
                return mid;  // Found the key
            } else if (a[mid] > key) {
                hi = mid - 1;  // Search in the left half
            } else {
                lo = mid + 1;  // Search in the right half
            }
        }
        return -1;  // Key not found
    }
}
