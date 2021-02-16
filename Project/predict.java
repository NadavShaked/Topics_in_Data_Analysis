package com.company;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Predicate;

public class predict {
    public static void main(String[] args) throws Exception {
        validateInput(args);

        boolean debugMode = true;
        DecisionTree decisionTree = null;
        Predicate<String[]>[] predicateArray = null;

        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(args[0]));

            Map<?, ?> map = gson.fromJson(reader, Map.class);
            int version = ((Double) map.get("version")).intValue();
            predicateArray = PredicateGenerator.generatePredicateArray(version);

            LinkedTreeMap decisionTreeJson = (LinkedTreeMap) map.get("decisionTree");
            decisionTree = new DecisionTree(decisionTreeJson);

            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String row;

        int count = 0;
        int success = 0;

        while ((row = csvReader.readLine()) != null) {
            String[] vector = row.split(",");
            picData pic = new picData(vector, predicateArray);
            int predictedDigit = decisionTree.predictPicture(pic);

            if (debugMode) {
                count++;

                if (predictedDigit == Integer.parseInt(vector[0]))
                    success++;
                System.out.println("predicted digit: " + predictedDigit + " real digit: " + vector[0]);
            } else {
                System.out.println(predictedDigit);
            }
        }

        if (debugMode)
            System.out.println("success: " + success + " of: " + count);

        csvReader.close();
    }

    public static void validateInput(String[] args) throws Exception {
        boolean isTreeFileExist = new File(args[0]).isFile();
        if (!isTreeFileExist) {
            throw new Exception("Tree file doesnt exist");
        }

        boolean isTestSetFileExist = new File(args[1]).isFile();
        if (!isTestSetFileExist) {
            throw new Exception("Test set file doesnt exist");
        }
    }
}