/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import javax.swing.JLabel;

/**
 *
 * @author nirav gupta
 */
public class Node {
    
    private JLabel nodeLabel;
    private String Xcordinate;
    private String Ycordinate;
    private boolean isAnamoly;
    private int anamolyCount;
   
    public boolean isIsAnamoly() {
        return isAnamoly;
    }

    public void setIsAnamoly(boolean isAnamoly) {
        this.isAnamoly = isAnamoly;
    }

    public String getXcordinate() {
        return Xcordinate;
    }

    public void setXcordinate(String Xcordinate) {
        this.Xcordinate = Xcordinate;
    }

    public String getYcordinate() {
        return Ycordinate;
    }

    public void setYcordinate(String Ycordinate) {
        this.Ycordinate = Ycordinate;
    }

    public JLabel getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(JLabel nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public int getAnamolyCount() {
        return anamolyCount;
    }

    public void setAnamolyCount(int anamolyCount) {
        this.anamolyCount = anamolyCount;
    }

    
    
    
    
}
