package org.example;

/**
 *
 */

import java.util.Scanner;

public class App {
    public static void main(String args[]) {
        System.out.println("******** n variable linear equation ********");
        // Initializing the variables
        char[] variable = {'a', 'b', 'c', 'x', 'y', 'z', 'w'};
        System.out.println("Enter the number of variables");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println("Enter the coefficients variable");
        System.out.println("Enter in the format shown below");
        System.out.println("ax + by + cz + ... = d");

        // Input of coefficients from user
        int[][] matrix = new int[num][num];
        int[][] constt = new int[num][1];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                matrix[i][j] = sc.nextInt();
            }
            constt[i][0] = sc.nextInt();
        }
        // Representation of linear equations in form of matrix
        System.out.println("Matrix representation of above linear equations is: ");
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                System.out.print(" " + matrix[i][j]);
            }
            System.out.print("  " + variable[i]);
            System.out.print("  =  " + constt[i][0]);
            System.out.println();
        }
        sc.close();
    }
}
