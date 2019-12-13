package edu.stonybrook.cse308.gerrybackend.utils;

public class MathUtils {

    public static double calculatePercentDifference(double a, double b) {
        double absDiff = Math.abs(a - b);
        double avg = (a + b)/2;
        return absDiff / avg;
    }
}
