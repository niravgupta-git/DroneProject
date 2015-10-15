/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JLabel;

/**
 *
 * @author nirav gupta
 */
public class AirDrone {
    
    private Node source;
    private Node destination;
    private Node nextNode;
    int noOfAnamoliesInPath;
    
    private ArrayList<Node> adjacentNodes;
    LinkedList<Node> actualPathTaken; 
    
    
    public AirDrone(){
        source = new Node();
        destination = new Node();
        nextNode = new Node();
        actualPathTaken = new LinkedList<Node>();
    }
    
    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public LinkedList<Node> getActualPathTaken() {
        return actualPathTaken;
    }

    public void setActualPathTaken(LinkedList<Node> actualPathTaken) {
        this.actualPathTaken = actualPathTaken;
    }

    

    public ArrayList<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(ArrayList<Node> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    
    
    
}
