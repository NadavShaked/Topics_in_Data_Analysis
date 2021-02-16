package com.company;

import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Predicate;

public class learntree {
    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();
        boolean debugMode = true;
        validateInput(args);

        int P = Integer.parseInt(args[1]);
        int L = Integer.parseInt(args[2]);

        Predicate<String[]>[] predicateArray = PredicateGenerator.generatePredicateArray(Integer.parseInt(args[0]));
        int predicatesNumber = predicateArray.length;

        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(args[3]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkedList<picData>[] picDataPerNumber = new LinkedList[10];
        LinkedList<picData> validationPicturesSet = new LinkedList<>();
        Random rand = new Random();

        for (int i = 0; i < picDataPerNumber.length; i++)
            picDataPerNumber[i] = new LinkedList<>();

        int picturesCount = 0;
        String row;
        while ((row = csvReader.readLine()) != null) {
            picturesCount++;

            String[] vector = row.split(",");
            picData pic = new picData(vector, predicateArray);

            if (rand.nextInt(99) >= P) {
                picDataPerNumber[pic.label].add(pic);
            } else {
                validationPicturesSet.add(pic);
            }
        }
        csvReader.close();

        long stopTime = System.nanoTime();

        if (debugMode) {
            System.out.println(validationPicturesSet.size());
            System.out.println(stopTime - startTime);
        }

        double numOfIterations = Math.pow(2, L);

        DecisionTree[] decisionTreesArr = new DecisionTree[L + 1];

        BinTree mainTree = new BinTree(picDataPerNumber, predicatesNumber);
        int insertToCell = 0;
        int powCheck = 1;
        for (int i = 1; i <= numOfIterations; i++) {
            if (debugMode)
                System.out.println(i);

            mainTree.splitNode();
            if (i == powCheck) {
                decisionTreesArr[insertToCell] = DecisionTree.convertBinTreeToDecisionTree(mainTree);
                powCheck *= 2;
                insertToCell++;
            }
        }

        if (debugMode) {
            stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
        }

        System.out.println("num: " + picturesCount);

        getBestDecisionTree(decisionTreesArr, validationPicturesSet, Integer.parseInt(args[0]), args[4]);

        if (debugMode) {
            stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
        }
    }

    public static void getBestDecisionTree(DecisionTree[] decisionTrees, LinkedList<picData> validationPicturesSet, int version, String fileName) {
        double success = decisionTrees[0].calcSuccess(validationPicturesSet);
        int bestTreeIndex = 0;
        double tempSuccess = 0;
        for (int i = 1; i < decisionTrees.length; i++) {
            tempSuccess = decisionTrees[i].calcSuccess(validationPicturesSet);
            if (tempSuccess > success) {
                success = tempSuccess;
                bestTreeIndex = i;
            }
        }

        System.out.println("error: " + Math.round(100 - success));
        System.out.println("size: " + Math.round(Math.pow(2, bestTreeIndex)));

        JT jsonTree = new JT(decisionTrees[bestTreeIndex], version);

        try {
            FileWriter jsonFile = new FileWriter(fileName);
            jsonFile.write(jsonTree.toJSON());
            jsonFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void validateInput(String[] args) throws Exception {
        if (args.length != 5)
            throw new Exception("The program required 5 arguments");

        if (!(args[0].equals("1") || args[0].equals("2")))
            throw new Exception("Version value must be 1 or 2");

        try {
            int P = Integer.parseInt(args[1]);
            if (P < 0 || P > 100)
                throw new Exception("P value must be number between 0 to 100");
        } catch (NumberFormatException e) {
            throw new Exception("P value must be number");
        }

        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new Exception("L value must be number");
        }

        boolean isTrainingSetFileExist = new File(args[3]).isFile();
        if (!isTrainingSetFileExist) {
            throw new Exception("Training set file doesnt exist");
        }
    }
}
