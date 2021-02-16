package com.company;

import com.google.gson.Gson;

public class JT {
    private int version;
    private DecisionTree decisionTree;

    public JT(DecisionTree decisionTree, int version) {
        this.version = version;
        this.decisionTree = decisionTree;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}