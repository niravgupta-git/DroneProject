/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *
 * @author nirav gupta
 */
public class TripDetail implements Comparable<TripDetail>{
    
    int noOfAnamoliesInPath;
    LinkedList<Node> pathTaken;
    
    public TripDetail(){
        pathTaken = new LinkedList<Node>();
    }

    public int getNoOfAnamoliesInPath() {
        return noOfAnamoliesInPath;
    }

    public void setNoOfAnamoliesInPath(int noOfAnamoliesInPath) {
        this.noOfAnamoliesInPath = noOfAnamoliesInPath;
    }

    public LinkedList<Node> getPathTaken() {
        return pathTaken;
    }

    public void setPathTaken(LinkedList<Node> pathTaken) {
        this.pathTaken = pathTaken;
    }
    
    @Override
    public int compareTo(TripDetail td) {
        int compareAnomalies = ((TripDetail) td).getNoOfAnamoliesInPath(); 
 
		//ascending order
		return this.noOfAnamoliesInPath - compareAnomalies;
 
		//descending order
		//return compareQuantity - this.quantity;
    }
    
//    public int compareTo(Fruit compareFruit) {
// 
//		int compareQuantity = ((Fruit) compareFruit).getQuantity(); 
// 
//		//ascending order
//		return this.quantity - compareQuantity;
// 
//		//descending order
//		//return compareQuantity - this.quantity;
// 
//	}
}
