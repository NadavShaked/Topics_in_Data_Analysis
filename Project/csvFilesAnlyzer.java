package com.company;
import java.io.*;

public class csvFilesAnlyzer {
    public static void main(String[] args) throws Exception {
        PicturesAnalyzer picturesAnalyzer = new PicturesAnalyzer();
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] vector = row.split(",");
            picturesAnalyzer.addPicture(vector);
        }
        csvReader.close();

        try (PrintWriter writer = new PrintWriter(new File("digitsAnalyzer.csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("All pics Avg,");

            double[] picturesAveragePerPixel = picturesAnalyzer.getPicturesAveragePerPixel();
            for (int i = 0; i < picturesAveragePerPixel.length; i++) {
                sb.append(String.format("%.2f", picturesAveragePerPixel[i]) + ",");
            }
            sb.append('\n');

            for (int i = 0; i < 10; i++) {
                sb.append("digit " + i + ",");
                double[] digitPixelAvg = picturesAnalyzer.digitAnalyzerArray[i].getPixelAverage();
                for (int j = 0; j < digitPixelAvg.length; j++) {
                    sb.append(String.format("%.2f", digitPixelAvg[j]) + ",");
                }
                sb.append('\n');
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

class PicturesAnalyzer {
    public double numberOfPictures;
    public double[] pixelAccumulatorPerCell;
    public DigitAnlyzer[] digitAnalyzerArray;

    public PicturesAnalyzer() {
        this.numberOfPictures = 0;
        this.pixelAccumulatorPerCell = new double[784];

        this.digitAnalyzerArray = new DigitAnlyzer[10];
        for (int i = 0; i < this.digitAnalyzerArray.length; i++) {
            digitAnalyzerArray[i] = new DigitAnlyzer(i);
        }
    }

    public void addPicture(String[] vector) {
        this.numberOfPictures++;
        for (int i = 0; i + 1 < vector.length; i++) {
            this.pixelAccumulatorPerCell[i] += Integer.parseInt(vector[i + 1]);
        }

        int digit = Integer.parseInt(vector[0]);
        this.digitAnalyzerArray[digit].accumulate(vector);
    }

    public double[] getPicturesAveragePerPixel() {
        double[] picturesAveragePerPixel = new double[this.pixelAccumulatorPerCell.length];
        for(int i = 0; i < picturesAveragePerPixel.length; i++) {
            picturesAveragePerPixel[i] = this.pixelAccumulatorPerCell[i] / this.numberOfPictures;
        }

        return picturesAveragePerPixel;
    }
}

class DigitAnlyzer {
    public int digit;
    public double digitCount;
    public double[] pixelAccumulatorPerCell;

    public DigitAnlyzer(int digit) {
        this.digit = digit;
        this.digitCount = 0;
        this.pixelAccumulatorPerCell = new double[784];
    }

    public void accumulate(String[] vector) {
        this.digitCount++;
        for (int i = 0; i + 1 < this.pixelAccumulatorPerCell.length; i++) {
            this.pixelAccumulatorPerCell[i] += Integer.parseInt(vector[i + 1]);
        }
    }

    public double[] getPixelAverage() {
        double[] pixelAverage = new double[this.pixelAccumulatorPerCell.length];
        for (int i = 0; i < pixelAverage.length; i++) {
            pixelAverage[i] = this.pixelAccumulatorPerCell[i] / this.digitCount;
        }

        return pixelAverage;
    }
}