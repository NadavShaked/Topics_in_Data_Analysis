package com.company;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BinTree {
    private Node root;
    private PriorityQueue<Node> maxHeap;

    public BinTree(LinkedList<picData>[] picDataPerNumber, int predNum) throws InterruptedException {
        this.root = new Node(picDataPerNumber, predNum);
        this.maxHeap = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if(n1.getSplitVal() - n2.getSplitVal() > 0)
                    return -1;
                else if (n1.getSplitVal() - n2.getSplitVal() == 0)
                    return 0;
                return 1;
            }
        });
        this.maxHeap.add(this.root);
    }

    public Node getRoot() {
        return this.root;
    }

    public void splitNode() throws InterruptedException {
        Node n = this.maxHeap.remove();
        n.leafToNode();
        this.maxHeap.add(n.getYesBranch());
        this.maxHeap.add(n.getNoBranch());
    }
}