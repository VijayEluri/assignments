/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package octa;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author abuzaher
 */
public class SlicingTree {

    ArrayList<SlicingTreeNode> nodeList;
    int numInternalNodes;
    int numExternalNodes;
    boolean updated;

    public SlicingTree() {
        this.nodeList = new ArrayList<SlicingTreeNode>();
        this.updated = false;
    }

    public void setNumInternalNodes(int numInternalNodes) {
        this.numInternalNodes = numInternalNodes;
    }

    public void setNumExternalNodes(int numExternalNodes) {
        this.numExternalNodes = numExternalNodes;
    }

    public void appendNode(SlicingTreeNode node) {
        this.nodeList.add(node);
    }

    public ArrayList<SlicingTreeNode> getNodeList() {
        return nodeList;
    }

    public void printInfo(ArrayList<SlicingTreeNode> traverserdList) {
        System.out.println("RESULT of preorder traversal");
        for (Iterator<SlicingTreeNode> it = traverserdList.iterator(); it.hasNext();) {
            SlicingTreeNode node = it.next();
            if (node instanceof SlicingTreeInternalNode) {

                System.out.println("Node " + node.getId() + " is an internal node.");

                SlicingTreeNode leftChild = ((SlicingTreeInternalNode) node).getLeftChild();

                if (leftChild instanceof SlicingTreeInternalNode) {
                    System.out.println("Left child of node " + node.getId() + ": internal node: " + leftChild.getId());
                } else {
                    System.out.println("Left child of node " + node.getId() + ": external node: " + leftChild.getId());
                }

                SlicingTreeNode rightChild = ((SlicingTreeInternalNode) node).getRightChild();
                if (rightChild instanceof SlicingTreeInternalNode) {
                    System.out.println("Right child of node " + node.getId() + ": internal node: " + rightChild.getId());
                } else {
                    System.out.println("Left child of node " + node.getId() + ": external node: " + rightChild.getId());
                }

            } else {
                System.out.println("Node " + node.getId() + " is an external node.");

            }
        }
    }

    /*
     * print tree info
     */
    public void printInfo() {
        System.out.println("Number of internal nodes (cuts): " + this.numInternalNodes);
        System.out.println("Number of external nodes (faces): " + this.numExternalNodes);

        if (!updated) {
            for (int i = 0; i < this.numInternalNodes; i++) {
                SlicingTreeInternalNode node = (SlicingTreeInternalNode) this.nodeList.get(i);
                System.out.println("Parent of inernal node (cut)" + node.getId() + ": " + node.getParentId());
                System.out.println("Node Type: " + node.getCutType());
                System.out.println("Slicing path for this node: " + node.getSlicingPath());
            }
            for (int i = this.numInternalNodes; i < nodeList.size(); i++) {
                SlicingTreeExternalNode node = (SlicingTreeExternalNode) this.nodeList.get(i);
                System.out.println("Parent of leaf node (face) " + node.getId() + ": " + node.getParentId());
                System.out.println("Area under face: " + node.getId() + ": " + node.getFaceArea());
            }
        } else {
            for (Iterator<SlicingTreeNode> it = nodeList.iterator(); it.hasNext();) {
                SlicingTreeNode node = it.next();
                if (node instanceof SlicingTreeInternalNode) {

                    System.out.println("Node " + node.getId() + " is an internal node.");

                    SlicingTreeNode leftChild = ((SlicingTreeInternalNode) node).getLeftChild();

                    if (leftChild instanceof SlicingTreeInternalNode) {
                        System.out.println("Left child of node " + node.getId() + ": internal node: " + leftChild.getId());
                    } else {
                        System.out.println("Left child of node " + node.getId() + ": external node: " + leftChild.getId());
                    }

                    SlicingTreeNode rightChild = ((SlicingTreeInternalNode) node).getRightChild();
                    if (rightChild instanceof SlicingTreeInternalNode) {
                        System.out.println("Right child of node " + node.getId() + ": internal node: " + rightChild.getId());
                    } else {
                        System.out.println("Left child of node " + node.getId() + ": external node: " + rightChild.getId());
                    }

                } else {
                    System.out.println("Node " + node.getId() + " is an external node.");

                }
            }
        }

    }

    /*
     * updates the child parent references in the the tree
     */
    public void updateTree() {
        for (Iterator<SlicingTreeNode> it = nodeList.iterator(); it.hasNext();) {
            SlicingTreeNode treeNode = it.next();
            int parentId = treeNode.getParentId();
            int parentIndex = parentId - 1;
            if (parentIndex > -1) {
                SlicingTreeInternalNode parent = (SlicingTreeInternalNode) this.nodeList.get(parentIndex);
                if (parent.getRightChild() == null) {
                    parent.setRightChild(treeNode);
                } else {
                    parent.setLeftChild(treeNode);
                }
            }
        }
        this.updated = true;
    }

    /*
     * Traverse the tree in root, right, left manner
     */
    public ArrayList<SlicingTreeNode> reversePreorderTraverse(int startIdx) {
        ArrayList<SlicingTreeNode> traveresedList = new ArrayList<SlicingTreeNode>();
        SlicingTreeNode rootNode = this.getNodeList().get(startIdx);

        // first add the rootnode in the traversed list
        traveresedList.add(rootNode);
//        System.out.println(rootNode.getId());
        if (rootNode instanceof SlicingTreeInternalNode) {
            // get right traversed list recursively
            ArrayList<SlicingTreeNode> rightTrversedList = this.reversePreorderTraverse(this.getNodeList().indexOf(((SlicingTreeInternalNode) rootNode).getRightChild()));
            // get left traversed list recursively
            ArrayList<SlicingTreeNode> leftTraversedList = this.reversePreorderTraverse(this.getNodeList().indexOf(((SlicingTreeInternalNode) rootNode).getLeftChild()));

            // now that we have all the traversed list, let's add them into our main list
            traveresedList.addAll(rightTrversedList);
            traveresedList.addAll(leftTraversedList);
        }
        return traveresedList;
    }
}