package com.company;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Iterator;
import java.util.LinkedList;

public class DecisionTree {
    private int predicateId;
    private int label;
    private DecisionTree yesBranch;
    private DecisionTree noBranch;

    public DecisionTree (int predicateId,  DecisionTree yesBranch, DecisionTree noBranch) {
        this.predicateId = predicateId;
        this.label = -1;
        this.yesBranch = yesBranch;
        this.noBranch = noBranch;
    }

    public DecisionTree (int label) {
        this.predicateId = -1;
        this.label = label;
        this.yesBranch = null;
        this.noBranch = null;
    }

    public DecisionTree (LinkedTreeMap decisionTreeJson) {
        this.label = ((Double)decisionTreeJson.get("label")).intValue();
        this.predicateId = ((Double)decisionTreeJson.get("predicateId")).intValue();

        if (this.label == -1) {
            this.yesBranch = new DecisionTree((LinkedTreeMap) decisionTreeJson.get("yesBranch"));
            this.noBranch = new DecisionTree((LinkedTreeMap) decisionTreeJson.get("noBranch"));
        }
        else {
            this.yesBranch = null;
            this.noBranch = null;
        }
    }

    public boolean isLeaf() {
        return (yesBranch == null && noBranch == null);
    }

    public double calcSuccess(LinkedList<picData> validationPicturesSet) {
        int success = 0;
        Iterator<picData> it = validationPicturesSet.iterator();
        while (it.hasNext()) {
            picData pic = it.next();
            if (pic.label == predictPicture(pic))
                success++;
        }

        return ((double)success / (double)validationPicturesSet.size()) * 100;
    }

    public int predictPicture(picData pic) {
        if (this.isLeaf())
            return this.label;
        if (pic.predicateAnswers[this.predicateId])
            return this.yesBranch.predictPicture(pic);
        return this.noBranch.predictPicture(pic);
    }

    public static DecisionTree convertBinTreeToDecisionTree(BinTree tree) {
        return convertNodeToDecisionTree(tree.getRoot());
    }

    private static DecisionTree convertNodeToDecisionTree(Node node) {
        if (node.isLeaf()) {
            return new DecisionTree(node.getLabel());
        }
        DecisionTree yesBranch = convertNodeToDecisionTree(node.getYesBranch());
        DecisionTree noBranch = convertNodeToDecisionTree(node.getNoBranch());
        int predicateId = node.getPredicateId();
        return new DecisionTree(predicateId, yesBranch, noBranch);
    }
}
