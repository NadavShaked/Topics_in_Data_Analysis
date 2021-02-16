package com.company;

import java.util.Iterator;
import java.util.LinkedList;

public class Node {
    private int predicateId;
    private Node yesBranch;
    private Node noBranch;
    private int numOfPics; //N(L)
    private LinkedList<picData>[] picDataPerNumber;
    private double entropy; //H(L)
    private double maxIG; //IG(X, L) = H(L) - H(X)
    private LinkedList<picData>[] potentialYesPics;
    private LinkedList<picData>[] potentialNoPics;
    private int predNum;

    public Node(LinkedList<picData>[] picDataPerNumber, int predNum) throws InterruptedException {
        this.yesBranch = null;
        this.noBranch = null;
        this.picDataPerNumber = picDataPerNumber;
        this.numOfPics = 0;

        for (int i = 0; i < this.picDataPerNumber.length; i++)
            this.numOfPics += this.picDataPerNumber[i].size();
        this.entropy = calculateEntropy(this.picDataPerNumber, this.numOfPics);
        this.predNum = predNum;

        findMaxPredAndIG();
    }

    public int getPredicateId() {
        return this.predicateId;
    }

    public Node getYesBranch() {
        return this.yesBranch;
    }

    public Node getNoBranch() {
        return this.noBranch;
    }

    public int getLabel() {
        if(this.isLeaf()) {
            int label = 0;
            for (int i = 1; i < this.picDataPerNumber.length; i++) {
                if (this.picDataPerNumber[label].size() < this.picDataPerNumber[i].size())
                    label = i;
            }
            return label;
        }
        return -1;
    }

    public double getSplitVal() {
        return this.numOfPics * this.maxIG;
    }

    public boolean isLeaf() {
        return (this.yesBranch == null && this.noBranch == null);
    }

    public void leafToNode() throws InterruptedException {
        this.yesBranch = new Node(this.potentialYesPics, this.predNum);
        this.noBranch = new Node(this.potentialNoPics, this.predNum);
    }

    private double calculateEntropy(LinkedList<picData>[] picDataPerNumber, double numOfPics) {
        if (numOfPics == 0)
            return 0;

        double ret = 0;
        for (int i = 0; i < picDataPerNumber.length; i++) {
            if (picDataPerNumber[i].size() != 0) {
                double partition = picDataPerNumber[i].size() / numOfPics;
                ret -= partition * Math.log(partition);
            }
        }
        return ret;
    }

    private void findMaxPredAndIG() {
        int bestPredicateId = -1;
        double minLeafsEntropy = Double.POSITIVE_INFINITY;
        LinkedList<picData>[] minPotentialYesPics = null;
        LinkedList<picData>[] minPotentialNoPics = null;
        int predicateSize = this.predNum;

        for (int predicateId = 0; predicateId < predicateSize; predicateId++) {
            int numOfYesPics = 0;
            int numOfNoPics = 0;
            LinkedList<picData>[] yesPics = new LinkedList[this.picDataPerNumber.length];
            LinkedList<picData>[] noPics = new LinkedList[this.picDataPerNumber.length];
            for (int i = 0; i < this.picDataPerNumber.length; i++) {
                yesPics[i] = new LinkedList<>();
                noPics[i] = new LinkedList<>();
            }

            for (int i = 0; i < this.picDataPerNumber.length; i++) {
                Iterator<picData> picsIterator = this.picDataPerNumber[i].iterator();
                while(picsIterator.hasNext()) {
                    picData pic = picsIterator.next();
                    if (pic.predicateAnswers[predicateId]) {
                        yesPics[i].add(pic);
                        numOfYesPics++;
                    }
                    else {
                        noPics[i].add(pic);
                        numOfNoPics++;
                    }
                }
            }

            double leafsEntropy = calculatePredicateEntropy(yesPics, numOfYesPics, noPics, numOfNoPics);
            if (leafsEntropy < minLeafsEntropy) {
                bestPredicateId = predicateId;
                minLeafsEntropy = leafsEntropy;
                minPotentialYesPics = yesPics;
                minPotentialNoPics = noPics;
            }
        }

        this.predicateId = bestPredicateId;
        this.maxIG = this.entropy - minLeafsEntropy;
        this.potentialYesPics = minPotentialYesPics;
        this.potentialNoPics = minPotentialNoPics;
    }

    private double calculatePredicateEntropy(LinkedList<picData>[] yesPics, double numOfYesPics, LinkedList<picData>[] noPics, double numOfNoPics) {
        // (N(yes) / N(L)) * H(yes) + (N(no) / N(L)) * H(no)
        if (this.numOfPics == 0)
            return Double.POSITIVE_INFINITY;
        return (numOfYesPics / this.numOfPics) * calculateEntropy(yesPics, numOfYesPics) + (numOfNoPics / this.numOfPics) * calculateEntropy(noPics, numOfNoPics);
    }
}
