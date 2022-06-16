package org.summerchill.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理各航道的不同的指标计算
 */
public class CalcBsc {
    // This pair is used to store the X and Y coordinate of a point respectively
    static class Pair {
        BigDecimal first;
        BigDecimal second;

        public Pair(BigDecimal first, BigDecimal second) {
            this.first = first;
            this.second = second;
        }
    }

    //Function to find the line given two points
    //Ax + By + C = 0
    //(y1 – y2)x + (x2 – x1)y + (x1y2 – x2y1) = 0
    static List<BigDecimal> lineFromPoints(Pair P, Pair Q) {
        List<BigDecimal> coefficientNumList = new ArrayList<>();
        BigDecimal a = Q.second.subtract(P.second);
        BigDecimal b = P.first.subtract(Q.first);
        BigDecimal c = Q.first.multiply(P.second).subtract(Q.second.multiply(P.first));
        coefficientNumList.add(a);
        coefficientNumList.add(b);
        coefficientNumList.add(c);
        return coefficientNumList;
        //System.out.println("The line passing through points P and Q is: " + a + "x + " + b + "y " + c + " = 0");
    }

    public static void getlinearFunctionValue(BigDecimal valueNum) {
        BigDecimal key1 = new BigDecimal("");
        BigDecimal value1 = new BigDecimal("");
        BigDecimal key2 = new BigDecimal("");
        BigDecimal value2 = new BigDecimal("");
        Pair pair_start = new Pair(key1, value1);
        Pair pair_end = new Pair(key2, value2);
        List<BigDecimal> coefficientNumList = lineFromPoints(pair_start, pair_end);
        //ax + by + c =0  => y = (-c -ax)/ b
        BigDecimal a = coefficientNumList.get(0);
        BigDecimal b = coefficientNumList.get(1);
        BigDecimal c = coefficientNumList.get(2);
        BigDecimal targetValue = c.multiply(new BigDecimal(-1)).subtract(a.multiply(valueNum)).divide(b, 2, RoundingMode.HALF_UP); //(((-1) * c) + ((-1) * a * valueNum))/b ;
        System.out.println(targetValue);
    }

    // Driver code
    public static void main(String[] args) {
        try {
            Pair P = new Pair(new BigDecimal(0.3), new BigDecimal(60));
            Pair Q = new Pair(new BigDecimal(-0.5), new BigDecimal(80));
            lineFromPoints(P, Q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
