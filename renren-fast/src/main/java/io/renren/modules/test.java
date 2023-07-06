package io.renren.modules;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        int[] arr = new int[37];
        int num = 8;
        for (int i = 0;i<37;i++) {
            if (num != 20) {

                arr[i] = num;
                num++;
            } else {
                num++;
                i--;
            }

        }

        splitInto4(arr);
        splitInto6(arr);
    }

    private static void splitInto6(int[] arr) {
        int groupSize = 4;
        int startIndex = 0;

        while (startIndex < arr.length) {
            int endIndex = Math.min(startIndex + groupSize, arr.length);
            int[] group = Arrays.copyOfRange(arr, startIndex, endIndex);
            System.out.println(Arrays.toString(group));
            startIndex += groupSize;
        }
    }

    private static void splitInto4(int[] arr) {
        int groupSize = 6;
        int startIndex = 0;

        while (startIndex < arr.length) {
            int endIndex = Math.min(startIndex + groupSize, arr.length);
            int[] group = Arrays.copyOfRange(arr, startIndex, endIndex);
            System.out.println(Arrays.toString(group));
            startIndex += groupSize;
        }
    }
}
