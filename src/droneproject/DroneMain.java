/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import java.util.ArrayList;

/**
 *
 * @author nirav gupta
 */
public class DroneMain {
    
    public ArrayList<Node> NodeList = new ArrayList();
    
    public String getStringForInt(int number){
      
        String value = null;
        switch(number){
            case 0: value = "zero";
                break;
                case 1: value = "one";
                break;
                case 2: value = "two";
                break;
                case 3: value = "three";
                break;
                case 4: value = "four";
                break;
                case 5: value = "five";
                break;
                case 6: value = "six";
                break;
                case 7: value = "seven";
                break;
                case 8: value = "eight";
                break;
                case 9: value = "nine";
                break;
                case 10: value = "ten";
                break;
                case 11: value = "eleven";
                break;
                case 12: value = "twelve";
                break;
                case 13: value = "thirteen";
                break;
                case 14: value = "fourteen";
                break;
                case 15: value = "fifteen";
                break;
                case 16: value = "sixteen";
                break;
                case 17: value = "seventeen";
                break;
                case 18: value = "eighteen";
                break;
                case 19: value = "nineteen";
                break;
        }
        return value;
    }
    
    public int getIntForString(String value){
      
        int number = 0 ;
        switch(value){
            case "zero": number = 0;
                break;
                case "one": number = 1;
                break;
                case "two": number = 2;
                break;
                case "three": number = 3;
                break;
                case "four": number = 4;
                break;
                case "five": number = 5;
                break;
                case "six": number = 6;
                break;
                case "seven": number = 7;
                break;
                case "eight": number = 8;
                break;
                case "nine": number = 9;
                break;
                case "ten": number = 10;
                break;
                case "eleven": number = 11;
                break;
                case "twelve": number = 12;
                break;
                case "thirteen": number = 13;
                break;
                case "fourteen": number = 14;
                break;
                case "fifteen": number = 15;
                break;
                case "sixteen": number = 16;
                break;
                case "seventeen": number = 17;
                break;
                case "eighteen": number = 18;
                break;
                case "nineteen": number = 19;
                break;
        }
        return number;
    }
    
    public void buildAnomalies(ArrayList<Node> nodeList) {
        int numberOfAnnomalies = 0 + (int) (Math.random() * 200);
        //System.out.println("number of annomalies*********");
        for (int i = 0; i < numberOfAnnomalies; i++) {
            
            int annomalyAt = 0 + (int) (Math.random() * 399);
            //System.out.println("annomaly at"+annomalyAt);
            nodeList.get(annomalyAt).setIsAnamoly(true);
            int count = nodeList.get(annomalyAt).getAnamolyCount();
            count=count+1;
            nodeList.get(annomalyAt).setAnamolyCount(count);
            //System.out.println(nodeList.get(annomalyAt).getXcordinate()+nodeList.get(annomalyAt).getYcordinate()+" - Count: "+nodeList.get(annomalyAt).getAnamolyCount());
        }
    }
    
    public void displayWholeList(ArrayList<Node> nodeList) {
        for (int i = 0; i < nodeList.size(); i++) {
            
            System.out.println(nodeList.get(i).getXcordinate() + nodeList.get(i).getYcordinate()+".setName("+'"'+nodeList.get(i).getXcordinate() + nodeList.get(i).getYcordinate()+'"'+");");
        }
    }
    
    
    
    
}
