package com.company;

import java.util.function.Predicate;

public class PredicateGenerator {
    public static Predicate<String[]>[] generatePredicateArray(int version) {
        Predicate<String[]>[] predicateArray;
        if (version == 1) {
            predicateArray = new Predicate[784];
            for (int i = 0; i < predicateArray.length; i++) {
                int finalI = i;
                predicateArray[i] = x -> (Integer.parseInt(x[finalI + 1])) > 128;
            }
        }
        else {
            predicateArray = new Predicate[1137];
            int i = 0;
            int j = 30;
            while (j < 757){
                while (j % 28 != 0){
                    int finalJ = j;
                    predicateArray[i] = x -> (get9avg(x, finalJ) > 300);
                    j++;
                    i++;
                }
                j+= 2;
            }


            for (int k = 1; k <= 27; k++) {
                for (j = 1; j <= 15; j++) {
                    int finalJ = j;
                    int finalK = k;

                    predicateArray[i] = x -> (sumCube(x, finalK, 2, finalJ, 14) > 600);
                    i++;
                }
            }

            for (int k = 1; k <= 28; k++) {
                int finalK = k;
                predicateArray[i] = x -> (extremeValueInColumnCount(x, finalK) > 1.5);
                i++;
                predicateArray[i] = x -> (extremeValueInRowCount(x, finalK) >1.1);
                i++;
            }
        }
        return predicateArray;
    }

    private static int get9avg(String[] x, int j) {
        int sum = Integer.parseInt(x[j-29]) + Integer.parseInt(x[j-28]) + Integer.parseInt(x[j-27])
                + Integer.parseInt(x[j-1]) + Integer.parseInt(x[j]) + Integer.parseInt(x[j+1]) +
                Integer.parseInt(x[j+27]) + Integer.parseInt(x[j+28]) + Integer.parseInt(x[j+29]);
        return sum;
    }

    private static int sumRow(String[] pic, int row, int startColumn, int numOfColumns) {
        int sum = 0;
        for (int i = 0; i < numOfColumns; i++) {
            sum += Integer.parseInt(pic[(row - 1) * 28 + startColumn + i]);
        }
        return sum;
    }

    private static int sumColumn(String[] pic, int column, int startRow, int numOfRows) {
        int sum = 0;
        for (int i = 0; i < numOfRows; i++) {
            sum += Integer.parseInt(pic[(startRow - 1) * 28 + 28 * i + column]);
        }
        return sum;
    }

    private static int sumCube(String[] pic, int startRow, int numOfRows, int startColumn, int numOfColumns) {
        int sum = 0;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                sum += Integer.parseInt(pic[(startRow - 1) * 28 + i * 28 + startColumn + j]);
            }
        }

        return sum;
    }

    private static int maxExtremeValueInRowCount(String[] pic, int fromRow, int toRow) {
        int max = 0;
        for (int i = fromRow; i <= toRow; i++) {
            int count = extremeValueInRowCount(pic, i);
            if (max < count) {
                max = count;
            }
        }

        return max;
    }

    private static float AvgExtremeValueInRowCount(String[] pic, int fromRow, int toRow) {
        float avg = 0;
        for (int i = fromRow; i <= toRow; i++) {
            avg += extremeValueInColumnCount(pic, i);
        }

        return avg / (toRow - fromRow + 1);
    }

    private static int extremeValueInRowCount(String[] pic, int row) {
        int count = 0;
        boolean isZero = Integer.parseInt(pic[1 + 28 * (row - 1)]) == 0;
        for (int i = 1; i < 29; i++) {
            if (isZero && Integer.parseInt(pic[28 * (row - 1) + i]) != 0) {
                count++;
                isZero = !isZero;
            }
            else if (!isZero && Integer.parseInt(pic[28 * (row - 1) + i]) == 0) {
                isZero = !isZero;
            }
        }
        return count;
    }

    private static int maxExtremeValueInColumnCount(String[] pic, int fromColumn, int toColumn) {
        int max = 0;
        for (int i = fromColumn; i <= toColumn; i++) {
            int count = extremeValueInColumnCount(pic, i);
            if (max < count) {
                max = count;
            }
        }

        return max;
    }

    private static float AvgExtremeValueInColumnCount(String[] pic, int fromColumn, int toColumn) {
        float avg = 0;
        for (int i = fromColumn; i <= toColumn; i++) {
            avg += extremeValueInColumnCount(pic, i);
        }

        float x = avg / (toColumn - fromColumn + 1);

        return x;
    }

    private static int extremeValueInColumnCount(String[] pic, int column) {
        int count = 0;
        boolean isZero = Integer.parseInt(pic[column + 1]) == 0;
        for (int i = 0; i < 28; i++) {
            if (isZero && Integer.parseInt(pic[28 * i + column]) != 0) {
                count++;
                isZero = !isZero;
            }
            else if (!isZero && Integer.parseInt(pic[28 * i + column]) == 0) {
                isZero = !isZero;
            }
        }
        return count;
    }
}