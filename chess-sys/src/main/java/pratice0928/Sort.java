package pratice0928;

import java.util.Arrays;

public class Sort {
    //插入排序
    //[0,bound) 已排序区间
    //[bound,length)待排区间
    public static void insertSort(int[] array) {
        for (int bound = 1; bound < array.length; bound++) {
            int val = array[bound];
            int cur = bound - 1;
            for (; cur >= 0; cur--) {// 2 3 1
                if (array[cur] > val) {
                    array[cur + 1] = array[cur];
                } else {
                    break;
                }
            }
            array[cur + 1] = val;
        }
    }

    //选择排序
    public static void selectSort(int[] array) {
        for (int bound = 0; bound < array.length; bound++) {
            for (int cur = bound + 1; cur < array.length; cur++) {
                if (array[bound] > array[cur]) {
                    swap(array,bound,cur);
                }
            }
        }
    }

    private static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    //冒泡排序
    public static void bubbleSort(int[] array) {//9 5 2 7 3 6
        for (int bound = 0; bound < array.length - 1; bound++) {
            for (int cur = 0; cur < array.length - bound - 1; cur++) {
                if (array[cur] < array[cur + 1]) {
                    swap(array,cur,cur + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] array = {9,5,2,7,3,6,8};
        bubbleSort(array);
        System.out.println(Arrays.toString(array));
    }
}
