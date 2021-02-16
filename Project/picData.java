package com.company;

import java.util.function.Predicate;

public class picData {
    public int label;
    public boolean[] predicateAnswers;

    public picData(String[] picVector, Predicate<String[]>[] predicateArray) {
        this.label = Integer.parseInt(picVector[0]);
        this.predicateAnswers = new boolean[predicateArray.length];

        for(int i = 0; i < this.predicateAnswers.length; i++) {
            this.predicateAnswers[i] = predicateArray[i].test(picVector);
        }
    }
}
