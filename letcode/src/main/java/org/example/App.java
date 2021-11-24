package org.example;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(numWays(20, 5));
        System.out.println(numPlantingWays(20, 5));

    }

    public static int numWays(int n, int k) {
        // Corner cases
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return k;
        }

        // Base cases: free to paint any colors
        int prev2 = k;
        int prev1 = k * k;

        // Use the 2 most recent one to genertate the new one
        for (int i = 3; i <= n; i++) {
            int temp = prev1;
            prev1 = (prev1 + prev2) * (k - 1);
            prev2 = temp;
        }

        return prev1;
    }

    public static int numPlantingWays(int num, int cnt) {
        //特例1
        if (num == 0) {
            return 0;
        } else if (num == 1) {
            //特例2
            return cnt;
        } else {
            int colorNum1 = cnt;
            int colorNum2 = cnt * cnt;
            for (int i = 3; i <= num; i++) {
                int temp = colorNum2;
                colorNum2 = (colorNum2 + colorNum1) * (cnt - 1);
                colorNum1 = temp;
            }
            return colorNum2;
        }
    }
}
