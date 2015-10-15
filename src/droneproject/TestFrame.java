/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author nirav gupta
 */
public class TestFrame extends javax.swing.JFrame {

    Test test;
    HashMap<String, Component> componentMap;
    ArrayList<Node> fullNodeList;
    DroneMain dm;
    Timer timer;
    HashMap<JLabel, HashMap<Node, ArrayList<Node>>> relativeMap;
    HashMap<Node, ArrayList<Node>> adjNodeMap;
    private ArrayList<JLabel> dronePreviousNodeJLabelList;
    LinkedList<Node> expectedDronePath;
    LinkedList<Node> actualOldExpectedDronePath;
    //ArrayList <LinkedList<Node>> allPathsList;
    AirDrone airDrone;
    Node source;
    Node destination;
    static int i;
    static int k;
    static int xx=0;
    static int counter1;
    static int limit1=19;
    static int limit2=19;
    
    static int tripCount=0;
    TripDetail tripDetail;
    
    
    /**
     * Creates new form TestFrame
     */
    
    //machine learning data structures
    HashMap<String, List<TripDetail>> destinationConfidenceFactorMap = new HashMap<String, List<TripDetail>>();
    HashMap<String, HashMap<String, List<TripDetail>>> sourceDestinationFullMap = new HashMap<String, HashMap<String, List<TripDetail>>>();
    HashMap<String, List<TripDetail>> tempMap;
    
    List<TripDetail> tripList = new ArrayList<TripDetail>();
    
    public TestFrame() {
        initComponents();
        //test = new Test();
        dm = new DroneMain();
        
        fullNodeList = dm.NodeList;
        generateCodinates();
        setLabels();
        createComponentMap();
        
        
        ImageIcon image = new ImageIcon("C:\\Users\\nirav gupta\\Desktop\\map.jpg"); 
        JLabel imageLabel = new JLabel("", image, JLabel.CENTER); 
        JPanel panel = new JPanel(new BorderLayout());
        panel.add( imageLabel, BorderLayout.CENTER );
        
        tempMap = new HashMap<String, List<TripDetail>>();
    }

    public void calculateDronePath(Node source,Node destination,LinkedList<Node> path){
        int destXint = dm.getIntForString(destination.getXcordinate());
        String destYvalue = destination.getYcordinate();
        destYvalue = destYvalue.substring(0, 1).toLowerCase() + destYvalue.substring(1);
        int destYint = dm.getIntForString(destYvalue);
        
        int sourceXint = dm.getIntForString(source.getXcordinate());
        String sourceYvalue = source.getYcordinate();
        sourceYvalue = sourceYvalue.substring(0, 1).toLowerCase() + sourceYvalue.substring(1);
        int sourceYint = dm.getIntForString(sourceYvalue);
        
        int tempX=sourceXint;
        int tempY=sourceYint;
        
        
        
        while(tempX!=destXint || tempY!=destYint){
            if (tempX>destXint){
                tempX=tempX-1;
            }
            if (tempX<destXint){
                tempX= tempX+1;
            }
            if (tempY<destYint){
                tempY= tempY+1;
            }
            Node newNode = new Node();
            newNode.setXcordinate(dm.getStringForInt(tempX));
            String tempYvalue =dm.getStringForInt(tempY);
            tempYvalue = tempYvalue.substring(0, 1).toUpperCase() + tempYvalue.substring(1);
            newNode.setYcordinate(tempYvalue);
            path.add(newNode);
            
//            System.out.println("****node**** ");
//            System.out.println("Xvalue:  "+newNode.getXcordinate());
//            System.out.println("Yvalue:  "+newNode.getYcordinate());
//            System.out.println(" ");
        }
    }
    
    
    
    public void generateCodinates() {
  
        for (int i=0; i<=19;i++){
            for (int j=0; j<=19;j++){
                String value1 = dm.getStringForInt(i);
                String value2 = dm.getStringForInt(j);
                String Value2upper = value2.substring(0, 1).toUpperCase() + value2.substring(1);
                Node n = new Node();
                n.setXcordinate(value1);
                n.setYcordinate(Value2upper);
                dm.NodeList.add(n);
                }
        }
        
        
//        for (Node n: dm.NodeList){
//            System.out.println(n.getXcordinate()+n.getYcordinate());
//        }
    }
    
   
    private void createComponentMap() {
        componentMap = new HashMap<String, Component>();
        List<Component> components = getAllComponents(getContentPane());
        for (Component comp : components) {
            //System.out.println("comp" + comp.getName());
            componentMap.put(comp.getName(), comp);
        }
    }
    
    private List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof JLabel) {
                compList.addAll(getAllComponents((JLabel) comp));
            }
        }
        return compList;
    }
 
    
    //Timer and get component methods
    
    public JLabel getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (JLabel) componentMap.get(name);
        } else {
            return null;
        }
    }
    
    
    public class Task extends TimerTask {
        @Override
        public void run() {
            deleteAnomalies();
            dm.buildAnomalies(fullNodeList);
            //System.out.println("in run");
            for (Node node : fullNodeList) {
                //System.out.println("node:" + node.getXcordinate() + node.getYcordinate() + node.isIsAnamoly());
                if (node.isIsAnamoly()) {
                    //System.out.println("Inside if *****");
                    StringBuffer sb = new StringBuffer();
                    sb.append(node.getXcordinate());
                    sb.append(node.getYcordinate());
                    String s = sb.toString();
                    //System.out.println("s" + s);
                    JLabel j = (JLabel) getComponentByName(s);
                    j.setForeground(Color.BLUE);
                    //deleteAnomalies();
                }
            }
        }
    }
    
    public class flyExpectedTask extends TimerTask {
        @Override
        public void run() {
            if (xx < expectedDronePath.size()) {
                //deletePreviousDronePathNode();
                Node tempNode = expectedDronePath.get(xx);

                //if (tempNode!=null){
                if (tempNode.getXcordinate().equalsIgnoreCase(airDrone.getDestination().getXcordinate()) && tempNode.getYcordinate().equalsIgnoreCase(airDrone.getDestination().getYcordinate())){
                    
                    //add source destination and path followed to machine map
                    
                    String sourceNodeVal = getStringValueOfNode(airDrone.getSource());
                    String destNodeVal = getStringValueOfNode(airDrone.getDestination());
                    
                    //check to add the tripdetail intoo the map of same source and destination
                    tripList.add(tripDetail);
                    destinationConfidenceFactorMap.put(destNodeVal, tripList);
                    sourceDestinationFullMap.put(sourceNodeVal, destinationConfidenceFactorMap);
                    //sourceDestinationFullMap.put(expectedDronePath.getFirst(), destinationConfidenceFactorMap);
                    
                    //Cancel and reset timers
                    cancelAllTimerAndReset();
                    JOptionPane.showMessageDialog(null, "!!! Destination reached !!! ");
                }
                //}
                
                for (Node listNode : fullNodeList) {
                    if (listNode.getXcordinate().equalsIgnoreCase(tempNode.getXcordinate()) && listNode.getYcordinate().equalsIgnoreCase(tempNode.getYcordinate())) {
                        if (!listNode.isIsAnamoly()) {

                            String a = tempNode.getXcordinate();
                            String b = tempNode.getYcordinate();
                            b = b.substring(0, 1).toUpperCase() + b.substring(1);
                            StringBuffer sb = new StringBuffer();
                            sb.append(a);
                            sb.append(b);
                            String s = sb.toString();
                            //System.out.println("s" + s);
                            JLabel j = (JLabel) getComponentByName(s);
                            j.setForeground(Color.RED);
                            //add previous node to arraylist to turn it off grom red
                            dronePreviousNodeJLabelList.add(j);
                            
                            airDrone.actualPathTaken.add(tempNode);
                            tripDetail.pathTaken.add(tempNode);
                            
                            xx++;
                            break;
                        }else{
                            
                            tripDetail.noOfAnamoliesInPath = tripDetail.noOfAnamoliesInPath+1;
                            
                            if(xx==0){
                                tempNode = expectedDronePath.get(xx);
                            }else{
                                tempNode = expectedDronePath.get(xx-1);
                            }
                            ArrayList<Node> nextNodeAdjacentList = createAdjacentNodeList(tempNode);
                            Node newTempNode =  calculateConfidentNode(nextNodeAdjacentList,tempNode);
                                    //nextNodeAdjacentList.get(0);
                            
                            String a = newTempNode.getXcordinate();
                            String b = newTempNode.getYcordinate();
                            b = b.substring(0, 1).toUpperCase() + b.substring(1);
                            StringBuffer sb = new StringBuffer();
                            sb.append(a);
                            sb.append(b);
                            String s = sb.toString();
                            //System.out.println("s" + s);
                            JLabel j = (JLabel) getComponentByName(s);
                            j.setForeground(Color.RED);
                            //add previous node to arraylist to turn it off grom red
                            dronePreviousNodeJLabelList.add(j);
                            
                            airDrone.actualPathTaken.add(tempNode);
                            tripDetail.pathTaken.add(tempNode);
                            
                            xx=0;
                            expectedDronePath.clear();
                            calculateDronePath(newTempNode, airDrone.getDestination(), expectedDronePath);
                            break;
                        }
                    }
                }
            }
        }
        }
    
    
    public Node calculateConfidentNode(ArrayList<Node> list,Node forNode){
        Node node=null;
        //calculate next node to be picked up
        
        for (Node listNode : fullNodeList) {
                for (Node sourceAdjNode : list) {
                    if (listNode.getXcordinate().equalsIgnoreCase(sourceAdjNode.getXcordinate()) && listNode.getYcordinate().equalsIgnoreCase(sourceAdjNode.getYcordinate())) {
                        if (!listNode.isIsAnamoly()) {
                            
//                          int destXint = dm.getIntForString(airDrone.getDestination().getXcordinate());
//                          int destYint = dm.getIntForString(airDrone.getDestination().getYcordinate());  
//                       
//                          int adjXint = dm.getIntForString(listNode.getXcordinate());
//                          int adjYint = dm.getIntForString(listNode.getYcordinate());
//                          
                          //put random number in node as confidence factor or level of wind pressure example as confidence factor 
                          node=sourceAdjNode;
                          break;
                        }
                    }
                } 
            }
        return node;
    }
    
    
    public void deleteAnomalies(){
    for (Node node : fullNodeList) {
        if (node.isIsAnamoly()){
        JLabel j = (JLabel) getComponentByName(node.getXcordinate()+node.getYcordinate());
        j.setForeground(Color.BLACK);
        node.setIsAnamoly(false);
        }
    }
    }
    
    public void clearAllAnomalies() {
        for (Node node : fullNodeList) {

            JLabel j = (JLabel) getComponentByName(node.getXcordinate() + node.getYcordinate());
            j.setForeground(Color.BLACK);
            node.setIsAnamoly(false);

        }
    }
    
    public void deletePreviousDronePathNode(){
        for(JLabel jb:dronePreviousNodeJLabelList){
            jb.setForeground(Color.BLACK);
        }
    }
    
    public String getStringValueOfNode(Node n){
         
        String a = n.getXcordinate();
        String b = n.getYcordinate();
        b = b.substring(0, 1).toUpperCase() + b.substring(1);
        StringBuffer sb = new StringBuffer();
        sb.append(a);
        sb.append(b);
        String s = sb.toString();
        return s;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        zeroZero = new javax.swing.JLabel();
        zeroOne = new javax.swing.JLabel();
        zeroTwo = new javax.swing.JLabel();
        zeroThree = new javax.swing.JLabel();
        zeroFour = new javax.swing.JLabel();
        zeroFive = new javax.swing.JLabel();
        zeroSix = new javax.swing.JLabel();
        zeroSeven = new javax.swing.JLabel();
        zeroEight = new javax.swing.JLabel();
        zeroNine = new javax.swing.JLabel();
        zeroTen = new javax.swing.JLabel();
        zeroEleven = new javax.swing.JLabel();
        zeroTwelve = new javax.swing.JLabel();
        zeroThirteen = new javax.swing.JLabel();
        zeroFourteen = new javax.swing.JLabel();
        zeroEighteen = new javax.swing.JLabel();
        zeroNineteen = new javax.swing.JLabel();
        zeroSixteen = new javax.swing.JLabel();
        zeroSeventeen = new javax.swing.JLabel();
        zeroFifteen = new javax.swing.JLabel();
        oneOne = new javax.swing.JLabel();
        oneZero = new javax.swing.JLabel();
        oneFive = new javax.swing.JLabel();
        oneFour = new javax.swing.JLabel();
        oneThree = new javax.swing.JLabel();
        oneTwo = new javax.swing.JLabel();
        oneSeven = new javax.swing.JLabel();
        oneSix = new javax.swing.JLabel();
        oneThirteen = new javax.swing.JLabel();
        oneFourteen = new javax.swing.JLabel();
        oneEleven = new javax.swing.JLabel();
        oneTwelve = new javax.swing.JLabel();
        oneNine = new javax.swing.JLabel();
        oneTen = new javax.swing.JLabel();
        oneEight = new javax.swing.JLabel();
        oneSeventeen = new javax.swing.JLabel();
        oneSixteen = new javax.swing.JLabel();
        oneNineteen = new javax.swing.JLabel();
        oneEighteen = new javax.swing.JLabel();
        oneFifteen = new javax.swing.JLabel();
        twoFive = new javax.swing.JLabel();
        twoZero = new javax.swing.JLabel();
        twoOne = new javax.swing.JLabel();
        twoSeventeen = new javax.swing.JLabel();
        twoSixteen = new javax.swing.JLabel();
        twoTen = new javax.swing.JLabel();
        twoEight = new javax.swing.JLabel();
        twoFifteen = new javax.swing.JLabel();
        twoEighteen = new javax.swing.JLabel();
        twoNineteen = new javax.swing.JLabel();
        twoFour = new javax.swing.JLabel();
        twoThree = new javax.swing.JLabel();
        twoTwo = new javax.swing.JLabel();
        twoTwelve = new javax.swing.JLabel();
        twoEleven = new javax.swing.JLabel();
        twoNine = new javax.swing.JLabel();
        twoSix = new javax.swing.JLabel();
        twoSeven = new javax.swing.JLabel();
        twoFourteen = new javax.swing.JLabel();
        twoThirteen = new javax.swing.JLabel();
        threeEleven = new javax.swing.JLabel();
        threeTwelve = new javax.swing.JLabel();
        threeFourteen = new javax.swing.JLabel();
        threeSeven = new javax.swing.JLabel();
        threeSix = new javax.swing.JLabel();
        threeNine = new javax.swing.JLabel();
        threeThirteen = new javax.swing.JLabel();
        threeSeventeen = new javax.swing.JLabel();
        threeTen = new javax.swing.JLabel();
        threeSixteen = new javax.swing.JLabel();
        threeFifteen = new javax.swing.JLabel();
        threeEight = new javax.swing.JLabel();
        threeEighteen = new javax.swing.JLabel();
        threeNineteen = new javax.swing.JLabel();
        threeFour = new javax.swing.JLabel();
        threeThree = new javax.swing.JLabel();
        threeTwo = new javax.swing.JLabel();
        threeOne = new javax.swing.JLabel();
        threeZero = new javax.swing.JLabel();
        threeFive = new javax.swing.JLabel();
        fourNineteen = new javax.swing.JLabel();
        fourThree = new javax.swing.JLabel();
        fourFour = new javax.swing.JLabel();
        fourOne = new javax.swing.JLabel();
        fourTwo = new javax.swing.JLabel();
        fourFive = new javax.swing.JLabel();
        fourZero = new javax.swing.JLabel();
        fourTwelve = new javax.swing.JLabel();
        fourFourteen = new javax.swing.JLabel();
        fourEleven = new javax.swing.JLabel();
        fourSeventeen = new javax.swing.JLabel();
        fourThirteen = new javax.swing.JLabel();
        fourSixteen = new javax.swing.JLabel();
        fourTen = new javax.swing.JLabel();
        fourSeven = new javax.swing.JLabel();
        fourNine = new javax.swing.JLabel();
        fourSix = new javax.swing.JLabel();
        fourFifteen = new javax.swing.JLabel();
        fourEight = new javax.swing.JLabel();
        fourEighteen = new javax.swing.JLabel();
        fiveThirteen = new javax.swing.JLabel();
        fiveSixteen = new javax.swing.JLabel();
        fiveEleven = new javax.swing.JLabel();
        fiveSeventeen = new javax.swing.JLabel();
        fiveTwelve = new javax.swing.JLabel();
        fiveFourteen = new javax.swing.JLabel();
        fiveFive = new javax.swing.JLabel();
        fiveZero = new javax.swing.JLabel();
        fiveOne = new javax.swing.JLabel();
        fiveTwo = new javax.swing.JLabel();
        fiveSix = new javax.swing.JLabel();
        fiveFifteen = new javax.swing.JLabel();
        fiveEight = new javax.swing.JLabel();
        fiveEighteen = new javax.swing.JLabel();
        fiveTen = new javax.swing.JLabel();
        fiveSeven = new javax.swing.JLabel();
        fiveNine = new javax.swing.JLabel();
        fiveThree = new javax.swing.JLabel();
        fiveNineteen = new javax.swing.JLabel();
        fiveFour = new javax.swing.JLabel();
        sixFifteen = new javax.swing.JLabel();
        sixEight = new javax.swing.JLabel();
        sixFourteen = new javax.swing.JLabel();
        sixFive = new javax.swing.JLabel();
        sixSeventeen = new javax.swing.JLabel();
        sixTwelve = new javax.swing.JLabel();
        sixTwo = new javax.swing.JLabel();
        sixSix = new javax.swing.JLabel();
        sixZero = new javax.swing.JLabel();
        sixOne = new javax.swing.JLabel();
        sixTen = new javax.swing.JLabel();
        sixSeven = new javax.swing.JLabel();
        sixNine = new javax.swing.JLabel();
        sixThree = new javax.swing.JLabel();
        sixNineteen = new javax.swing.JLabel();
        sixFour = new javax.swing.JLabel();
        sixEighteen = new javax.swing.JLabel();
        sixThirteen = new javax.swing.JLabel();
        sixSixteen = new javax.swing.JLabel();
        sixEleven = new javax.swing.JLabel();
        sevenFourteen = new javax.swing.JLabel();
        sevenFifteen = new javax.swing.JLabel();
        sevenEight = new javax.swing.JLabel();
        sevenNineteen = new javax.swing.JLabel();
        sevenFour = new javax.swing.JLabel();
        sevenThree = new javax.swing.JLabel();
        sevenEleven = new javax.swing.JLabel();
        sevenSixteen = new javax.swing.JLabel();
        sevenThirteen = new javax.swing.JLabel();
        sevenEighteen = new javax.swing.JLabel();
        sevenFive = new javax.swing.JLabel();
        sevenSeventeen = new javax.swing.JLabel();
        sevenTwo = new javax.swing.JLabel();
        sevenTwelve = new javax.swing.JLabel();
        sevenZero = new javax.swing.JLabel();
        sevenSix = new javax.swing.JLabel();
        sevenTen = new javax.swing.JLabel();
        sevenOne = new javax.swing.JLabel();
        sevenNine = new javax.swing.JLabel();
        sevenSeven = new javax.swing.JLabel();
        eightTen = new javax.swing.JLabel();
        eightOne = new javax.swing.JLabel();
        eightZero = new javax.swing.JLabel();
        eightSix = new javax.swing.JLabel();
        eightTwelve = new javax.swing.JLabel();
        eightSeven = new javax.swing.JLabel();
        eightNine = new javax.swing.JLabel();
        eightNineteen = new javax.swing.JLabel();
        eightFour = new javax.swing.JLabel();
        eightThree = new javax.swing.JLabel();
        eightEleven = new javax.swing.JLabel();
        eightTwo = new javax.swing.JLabel();
        eightSeventeen = new javax.swing.JLabel();
        eightThirteen = new javax.swing.JLabel();
        eightSixteen = new javax.swing.JLabel();
        eightFive = new javax.swing.JLabel();
        eightEighteen = new javax.swing.JLabel();
        eightEight = new javax.swing.JLabel();
        eightFifteen = new javax.swing.JLabel();
        eightFourteen = new javax.swing.JLabel();
        nineFifteen = new javax.swing.JLabel();
        nineEight = new javax.swing.JLabel();
        nineFourteen = new javax.swing.JLabel();
        nineSixteen = new javax.swing.JLabel();
        nineThirteen = new javax.swing.JLabel();
        nineEighteen = new javax.swing.JLabel();
        nineFive = new javax.swing.JLabel();
        nineTwelve = new javax.swing.JLabel();
        nineSix = new javax.swing.JLabel();
        nineNine = new javax.swing.JLabel();
        nineSeven = new javax.swing.JLabel();
        nineFour = new javax.swing.JLabel();
        nineNineteen = new javax.swing.JLabel();
        nineThree = new javax.swing.JLabel();
        nineEleven = new javax.swing.JLabel();
        nineTwo = new javax.swing.JLabel();
        nineSeventeen = new javax.swing.JLabel();
        nineZero = new javax.swing.JLabel();
        nineOne = new javax.swing.JLabel();
        nineTen = new javax.swing.JLabel();
        tenFourteen = new javax.swing.JLabel();
        tenEight = new javax.swing.JLabel();
        tenFifteen = new javax.swing.JLabel();
        tenFour = new javax.swing.JLabel();
        tenSeven = new javax.swing.JLabel();
        tenNine = new javax.swing.JLabel();
        tenSix = new javax.swing.JLabel();
        tenNineteen = new javax.swing.JLabel();
        tenSixteen = new javax.swing.JLabel();
        tenTwelve = new javax.swing.JLabel();
        tenFive = new javax.swing.JLabel();
        tenEighteen = new javax.swing.JLabel();
        tenThirteen = new javax.swing.JLabel();
        tenTen = new javax.swing.JLabel();
        tenEleven = new javax.swing.JLabel();
        tenThree = new javax.swing.JLabel();
        tenSeventeen = new javax.swing.JLabel();
        tenTwo = new javax.swing.JLabel();
        tenOne = new javax.swing.JLabel();
        tenZero = new javax.swing.JLabel();
        elevenFourteen = new javax.swing.JLabel();
        elevenFifteen = new javax.swing.JLabel();
        elevenEight = new javax.swing.JLabel();
        elevenSixteen = new javax.swing.JLabel();
        elevenTwelve = new javax.swing.JLabel();
        elevenSix = new javax.swing.JLabel();
        elevenNineteen = new javax.swing.JLabel();
        elevenSeven = new javax.swing.JLabel();
        elevenNine = new javax.swing.JLabel();
        elevenFour = new javax.swing.JLabel();
        elevenThirteen = new javax.swing.JLabel();
        elevenFive = new javax.swing.JLabel();
        elevenEighteen = new javax.swing.JLabel();
        elevenTwo = new javax.swing.JLabel();
        elevenOne = new javax.swing.JLabel();
        elevenZero = new javax.swing.JLabel();
        elevenTen = new javax.swing.JLabel();
        elevenEleven = new javax.swing.JLabel();
        elevenThree = new javax.swing.JLabel();
        elevenSeventeen = new javax.swing.JLabel();
        twelveFourteen = new javax.swing.JLabel();
        twelveFifteen = new javax.swing.JLabel();
        twelveEight = new javax.swing.JLabel();
        twelveSixteen = new javax.swing.JLabel();
        twelveNineteen = new javax.swing.JLabel();
        twelveSeven = new javax.swing.JLabel();
        twelveTwelve = new javax.swing.JLabel();
        twelveSix = new javax.swing.JLabel();
        twelveThirteen = new javax.swing.JLabel();
        twelveFive = new javax.swing.JLabel();
        twelveNine = new javax.swing.JLabel();
        twelveFour = new javax.swing.JLabel();
        twelveEighteen = new javax.swing.JLabel();
        twelveOne = new javax.swing.JLabel();
        twelveTwo = new javax.swing.JLabel();
        twelveZero = new javax.swing.JLabel();
        twelveTen = new javax.swing.JLabel();
        twelveEleven = new javax.swing.JLabel();
        twelveThree = new javax.swing.JLabel();
        twelveSeventeen = new javax.swing.JLabel();
        thirteenFourteen = new javax.swing.JLabel();
        thirteenFifteen = new javax.swing.JLabel();
        thirteenEight = new javax.swing.JLabel();
        thirteenSeventeen = new javax.swing.JLabel();
        thirteenThree = new javax.swing.JLabel();
        thirteenEleven = new javax.swing.JLabel();
        thirteenZero = new javax.swing.JLabel();
        thirteenTen = new javax.swing.JLabel();
        thirteenOne = new javax.swing.JLabel();
        thirteenTwo = new javax.swing.JLabel();
        thirteenSix = new javax.swing.JLabel();
        thirteenTwelve = new javax.swing.JLabel();
        thirteenFive = new javax.swing.JLabel();
        thirteenThirteen = new javax.swing.JLabel();
        thirteenFour = new javax.swing.JLabel();
        thirteenNine = new javax.swing.JLabel();
        thirteenEighteen = new javax.swing.JLabel();
        thirteenSixteen = new javax.swing.JLabel();
        thirteenNineteen = new javax.swing.JLabel();
        thirteenSeven = new javax.swing.JLabel();
        fourteenFourteen = new javax.swing.JLabel();
        fourteenEight = new javax.swing.JLabel();
        fourteenFifteen = new javax.swing.JLabel();
        fourteenFive = new javax.swing.JLabel();
        fourteenTwo = new javax.swing.JLabel();
        fourteenOne = new javax.swing.JLabel();
        fourteenTwelve = new javax.swing.JLabel();
        fourteenSix = new javax.swing.JLabel();
        fourteenThree = new javax.swing.JLabel();
        fourteenEleven = new javax.swing.JLabel();
        fourteenZero = new javax.swing.JLabel();
        fourteenTen = new javax.swing.JLabel();
        fourteenSeventeen = new javax.swing.JLabel();
        fourteenNineteen = new javax.swing.JLabel();
        fourteenSeven = new javax.swing.JLabel();
        fourteenEighteen = new javax.swing.JLabel();
        fourteenSixteen = new javax.swing.JLabel();
        fourteenFour = new javax.swing.JLabel();
        fourteenNine = new javax.swing.JLabel();
        fourteenThirteen = new javax.swing.JLabel();
        fifteenTwelve = new javax.swing.JLabel();
        fifteenOne = new javax.swing.JLabel();
        fifteenTwo = new javax.swing.JLabel();
        fifteenFive = new javax.swing.JLabel();
        fifteenSeventeen = new javax.swing.JLabel();
        fifteenTen = new javax.swing.JLabel();
        fifteenZero = new javax.swing.JLabel();
        fifteenEleven = new javax.swing.JLabel();
        fifteenThree = new javax.swing.JLabel();
        fifteenSix = new javax.swing.JLabel();
        fifteenEighteen = new javax.swing.JLabel();
        fifteenSeven = new javax.swing.JLabel();
        fifteenFour = new javax.swing.JLabel();
        fifteenSixteen = new javax.swing.JLabel();
        fifteenNineteen = new javax.swing.JLabel();
        fifteenThirteen = new javax.swing.JLabel();
        fifteenNine = new javax.swing.JLabel();
        fifteenFifteen = new javax.swing.JLabel();
        fifteenFourteen = new javax.swing.JLabel();
        fifteenEight = new javax.swing.JLabel();
        sixteenSeventeen = new javax.swing.JLabel();
        sixteenFive = new javax.swing.JLabel();
        sixteenThree = new javax.swing.JLabel();
        sixteenEleven = new javax.swing.JLabel();
        sixteenZero = new javax.swing.JLabel();
        sixteenTen = new javax.swing.JLabel();
        sixteenFour = new javax.swing.JLabel();
        sixteenSeven = new javax.swing.JLabel();
        sixteenEighteen = new javax.swing.JLabel();
        sixteenSix = new javax.swing.JLabel();
        sixteenSixteen = new javax.swing.JLabel();
        sixteenThirteen = new javax.swing.JLabel();
        sixteenNineteen = new javax.swing.JLabel();
        sixteenFifteen = new javax.swing.JLabel();
        sixteenNine = new javax.swing.JLabel();
        sixteenEight = new javax.swing.JLabel();
        sixteenFourteen = new javax.swing.JLabel();
        sixteenTwo = new javax.swing.JLabel();
        sixteenOne = new javax.swing.JLabel();
        sixteenTwelve = new javax.swing.JLabel();
        seventeenFive = new javax.swing.JLabel();
        seventeenSeventeen = new javax.swing.JLabel();
        seventeenThree = new javax.swing.JLabel();
        seventeenThirteen = new javax.swing.JLabel();
        seventeenNineteen = new javax.swing.JLabel();
        seventeenSix = new javax.swing.JLabel();
        seventeenSixteen = new javax.swing.JLabel();
        seventeenSeven = new javax.swing.JLabel();
        seventeenEighteen = new javax.swing.JLabel();
        seventeenTen = new javax.swing.JLabel();
        seventeenFour = new javax.swing.JLabel();
        seventeenEleven = new javax.swing.JLabel();
        seventeenZero = new javax.swing.JLabel();
        seventeenFifteen = new javax.swing.JLabel();
        seventeenOne = new javax.swing.JLabel();
        seventeenTwelve = new javax.swing.JLabel();
        seventeenNine = new javax.swing.JLabel();
        seventeenEight = new javax.swing.JLabel();
        seventeenFourteen = new javax.swing.JLabel();
        seventeenTwo = new javax.swing.JLabel();
        eighteenNineteen = new javax.swing.JLabel();
        eighteenThirteen = new javax.swing.JLabel();
        eighteenSeven = new javax.swing.JLabel();
        eighteenEighteen = new javax.swing.JLabel();
        eighteenSix = new javax.swing.JLabel();
        eighteenSixteen = new javax.swing.JLabel();
        eighteenEleven = new javax.swing.JLabel();
        eighteenZero = new javax.swing.JLabel();
        eighteenTen = new javax.swing.JLabel();
        eighteenFour = new javax.swing.JLabel();
        eighteenFifteen = new javax.swing.JLabel();
        eighteenTwelve = new javax.swing.JLabel();
        eighteenOne = new javax.swing.JLabel();
        eighteenNine = new javax.swing.JLabel();
        eighteenEight = new javax.swing.JLabel();
        eighteenFourteen = new javax.swing.JLabel();
        eighteenTwo = new javax.swing.JLabel();
        eighteenThree = new javax.swing.JLabel();
        eighteenFive = new javax.swing.JLabel();
        eighteenSeventeen = new javax.swing.JLabel();
        nineteenEight = new javax.swing.JLabel();
        nineteenNine = new javax.swing.JLabel();
        nineteenSeventeen = new javax.swing.JLabel();
        nineteenFive = new javax.swing.JLabel();
        nineteenThree = new javax.swing.JLabel();
        nineteenTwo = new javax.swing.JLabel();
        nineteenFourteen = new javax.swing.JLabel();
        nineteenNineteen = new javax.swing.JLabel();
        nineteenThirteen = new javax.swing.JLabel();
        nineteenSeven = new javax.swing.JLabel();
        nineteenTen = new javax.swing.JLabel();
        nineteenZero = new javax.swing.JLabel();
        nineteenFifteen = new javax.swing.JLabel();
        nineteenFour = new javax.swing.JLabel();
        nineteenOne = new javax.swing.JLabel();
        nineteenTwelve = new javax.swing.JLabel();
        nineteenEighteen = new javax.swing.JLabel();
        nineteenSix = new javax.swing.JLabel();
        nineteenSixteen = new javax.swing.JLabel();
        nineteenEleven = new javax.swing.JLabel();
        startSimulationjButton1 = new javax.swing.JButton();
        resetjButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(new java.awt.Color(153, 153, 153));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        zeroZero.setText("#");
        getContentPane().add(zeroZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 54, -1, -1));

        zeroOne.setText("#");
        getContentPane().add(zeroOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 54, -1, -1));

        zeroTwo.setText("#");
        getContentPane().add(zeroTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 54, -1, -1));

        zeroThree.setText("#");
        getContentPane().add(zeroThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 54, -1, -1));

        zeroFour.setText("#");
        getContentPane().add(zeroFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 54, -1, -1));

        zeroFive.setText("#");
        getContentPane().add(zeroFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 54, -1, -1));

        zeroSix.setText("#");
        getContentPane().add(zeroSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 54, -1, -1));

        zeroSeven.setText("#");
        getContentPane().add(zeroSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 54, -1, -1));

        zeroEight.setText("#");
        getContentPane().add(zeroEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 54, -1, -1));

        zeroNine.setText("#");
        getContentPane().add(zeroNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 54, -1, -1));

        zeroTen.setText("#");
        getContentPane().add(zeroTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 54, -1, -1));

        zeroEleven.setText("#");
        getContentPane().add(zeroEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 54, -1, -1));

        zeroTwelve.setText("#");
        getContentPane().add(zeroTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 54, -1, -1));

        zeroThirteen.setText("#");
        getContentPane().add(zeroThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 54, -1, -1));

        zeroFourteen.setText("#");
        getContentPane().add(zeroFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 54, -1, -1));

        zeroEighteen.setText("#");
        getContentPane().add(zeroEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 54, -1, -1));

        zeroNineteen.setText("#");
        getContentPane().add(zeroNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 54, -1, -1));

        zeroSixteen.setText("#");
        getContentPane().add(zeroSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 54, -1, -1));

        zeroSeventeen.setText("#");
        getContentPane().add(zeroSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 54, -1, -1));

        zeroFifteen.setText("#");
        getContentPane().add(zeroFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 54, -1, -1));

        oneOne.setText("#");
        getContentPane().add(oneOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 91, -1, -1));

        oneZero.setText("#");
        getContentPane().add(oneZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 91, -1, -1));

        oneFive.setText("#");
        getContentPane().add(oneFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 91, -1, -1));

        oneFour.setText("#");
        getContentPane().add(oneFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 91, -1, -1));

        oneThree.setText("#");
        getContentPane().add(oneThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 91, -1, -1));

        oneTwo.setText("#");
        getContentPane().add(oneTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 91, -1, -1));

        oneSeven.setText("#");
        getContentPane().add(oneSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 91, -1, -1));

        oneSix.setText("#");
        getContentPane().add(oneSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 91, -1, -1));

        oneThirteen.setText("#");
        getContentPane().add(oneThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 91, -1, -1));

        oneFourteen.setText("#");
        getContentPane().add(oneFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 91, -1, -1));

        oneEleven.setText("#");
        getContentPane().add(oneEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 91, -1, -1));

        oneTwelve.setText("#");
        getContentPane().add(oneTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 91, -1, -1));

        oneNine.setText("#");
        getContentPane().add(oneNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 91, -1, -1));

        oneTen.setText("#");
        getContentPane().add(oneTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 91, -1, -1));

        oneEight.setText("#");
        getContentPane().add(oneEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 91, -1, -1));

        oneSeventeen.setText("#");
        getContentPane().add(oneSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 91, -1, -1));

        oneSixteen.setText("#");
        getContentPane().add(oneSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 91, -1, -1));

        oneNineteen.setText("#");
        getContentPane().add(oneNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 91, -1, -1));

        oneEighteen.setText("#");
        getContentPane().add(oneEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 91, -1, -1));

        oneFifteen.setText("#");
        getContentPane().add(oneFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 91, -1, -1));

        twoFive.setText("#");
        getContentPane().add(twoFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 128, -1, -1));

        twoZero.setText("#");
        getContentPane().add(twoZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 128, -1, -1));

        twoOne.setText("#");
        getContentPane().add(twoOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 128, -1, -1));

        twoSeventeen.setText("#");
        getContentPane().add(twoSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 128, -1, -1));

        twoSixteen.setText("#");
        getContentPane().add(twoSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 128, -1, -1));

        twoTen.setText("#");
        getContentPane().add(twoTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 128, -1, -1));

        twoEight.setText("#");
        getContentPane().add(twoEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 128, -1, -1));

        twoFifteen.setText("#");
        getContentPane().add(twoFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 128, -1, -1));

        twoEighteen.setText("#");
        getContentPane().add(twoEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 128, -1, -1));

        twoNineteen.setText("#");
        getContentPane().add(twoNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 128, -1, -1));

        twoFour.setText("#");
        getContentPane().add(twoFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 128, -1, -1));

        twoThree.setText("#");
        getContentPane().add(twoThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 128, -1, -1));

        twoTwo.setText("#");
        getContentPane().add(twoTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 128, -1, -1));

        twoTwelve.setText("#");
        getContentPane().add(twoTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 128, -1, -1));

        twoEleven.setText("#");
        getContentPane().add(twoEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 128, -1, -1));

        twoNine.setText("#");
        getContentPane().add(twoNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 128, -1, -1));

        twoSix.setText("#");
        getContentPane().add(twoSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 128, -1, -1));

        twoSeven.setText("#");
        getContentPane().add(twoSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 128, -1, -1));

        twoFourteen.setText("#");
        getContentPane().add(twoFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 128, -1, -1));

        twoThirteen.setText("#");
        getContentPane().add(twoThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 128, -1, -1));

        threeEleven.setText("#");
        getContentPane().add(threeEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 165, -1, -1));

        threeTwelve.setText("#");
        getContentPane().add(threeTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 165, -1, -1));

        threeFourteen.setText("#");
        getContentPane().add(threeFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 165, -1, -1));

        threeSeven.setText("#");
        getContentPane().add(threeSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 165, -1, -1));

        threeSix.setText("#");
        getContentPane().add(threeSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 165, -1, -1));

        threeNine.setText("#");
        getContentPane().add(threeNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 165, -1, -1));

        threeThirteen.setText("#");
        getContentPane().add(threeThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 165, -1, -1));

        threeSeventeen.setText("#");
        getContentPane().add(threeSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 165, -1, -1));

        threeTen.setText("#");
        getContentPane().add(threeTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 165, -1, -1));

        threeSixteen.setText("#");
        getContentPane().add(threeSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 165, -1, -1));

        threeFifteen.setText("#");
        getContentPane().add(threeFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 165, -1, -1));

        threeEight.setText("#");
        getContentPane().add(threeEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 165, -1, -1));

        threeEighteen.setText("#");
        getContentPane().add(threeEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 165, -1, -1));

        threeNineteen.setText("#");
        getContentPane().add(threeNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 165, -1, -1));

        threeFour.setText("#");
        getContentPane().add(threeFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 165, -1, -1));

        threeThree.setText("#");
        getContentPane().add(threeThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 165, -1, -1));

        threeTwo.setText("#");
        getContentPane().add(threeTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 165, -1, -1));

        threeOne.setText("#");
        getContentPane().add(threeOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 165, -1, -1));

        threeZero.setText("#");
        getContentPane().add(threeZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 165, -1, -1));

        threeFive.setText("#");
        getContentPane().add(threeFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 165, -1, -1));

        fourNineteen.setText("#");
        getContentPane().add(fourNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 202, -1, -1));

        fourThree.setText("#");
        getContentPane().add(fourThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 202, -1, -1));

        fourFour.setText("#");
        getContentPane().add(fourFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 202, -1, -1));

        fourOne.setText("#");
        getContentPane().add(fourOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 202, -1, -1));

        fourTwo.setText("#");
        getContentPane().add(fourTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 202, -1, -1));

        fourFive.setText("#");
        getContentPane().add(fourFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 202, -1, -1));

        fourZero.setText("#");
        getContentPane().add(fourZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 202, -1, -1));

        fourTwelve.setText("#");
        getContentPane().add(fourTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 202, -1, -1));

        fourFourteen.setText("#");
        getContentPane().add(fourFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 202, -1, -1));

        fourEleven.setText("#");
        getContentPane().add(fourEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 202, -1, -1));

        fourSeventeen.setText("#");
        getContentPane().add(fourSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 202, -1, -1));

        fourThirteen.setText("#");
        getContentPane().add(fourThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 202, -1, -1));

        fourSixteen.setText("#");
        getContentPane().add(fourSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 202, -1, -1));

        fourTen.setText("#");
        getContentPane().add(fourTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 202, -1, -1));

        fourSeven.setText("#");
        getContentPane().add(fourSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 202, -1, -1));

        fourNine.setText("#");
        getContentPane().add(fourNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 202, -1, -1));

        fourSix.setText("#");
        getContentPane().add(fourSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 202, -1, -1));

        fourFifteen.setText("#");
        getContentPane().add(fourFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 202, -1, -1));

        fourEight.setText("#");
        getContentPane().add(fourEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 202, -1, -1));

        fourEighteen.setText("#");
        getContentPane().add(fourEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 202, -1, -1));

        fiveThirteen.setText("#");
        getContentPane().add(fiveThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 239, -1, -1));

        fiveSixteen.setText("#");
        getContentPane().add(fiveSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 239, -1, -1));

        fiveEleven.setText("#");
        getContentPane().add(fiveEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 239, -1, -1));

        fiveSeventeen.setText("#");
        getContentPane().add(fiveSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 239, -1, -1));

        fiveTwelve.setText("#");
        getContentPane().add(fiveTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 239, -1, -1));

        fiveFourteen.setText("#");
        getContentPane().add(fiveFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 239, -1, -1));

        fiveFive.setText("#");
        getContentPane().add(fiveFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 239, -1, -1));

        fiveZero.setText("#");
        getContentPane().add(fiveZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 239, -1, -1));

        fiveOne.setText("#");
        getContentPane().add(fiveOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 239, -1, -1));

        fiveTwo.setText("#");
        getContentPane().add(fiveTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 239, -1, -1));

        fiveSix.setText("#");
        getContentPane().add(fiveSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 239, -1, -1));

        fiveFifteen.setText("#");
        getContentPane().add(fiveFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 239, -1, -1));

        fiveEight.setText("#");
        getContentPane().add(fiveEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 239, -1, -1));

        fiveEighteen.setText("#");
        getContentPane().add(fiveEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 239, -1, -1));

        fiveTen.setText("#");
        getContentPane().add(fiveTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 239, -1, -1));

        fiveSeven.setText("#");
        getContentPane().add(fiveSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 239, -1, -1));

        fiveNine.setText("#");
        getContentPane().add(fiveNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 239, -1, -1));

        fiveThree.setText("#");
        getContentPane().add(fiveThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 239, -1, -1));

        fiveNineteen.setText("#");
        getContentPane().add(fiveNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 239, -1, -1));

        fiveFour.setText("#");
        getContentPane().add(fiveFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 239, -1, -1));

        sixFifteen.setText("#");
        getContentPane().add(sixFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 276, -1, -1));

        sixEight.setText("#");
        getContentPane().add(sixEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 276, -1, -1));

        sixFourteen.setText("#");
        getContentPane().add(sixFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 276, -1, -1));

        sixFive.setText("#");
        getContentPane().add(sixFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 276, -1, -1));

        sixSeventeen.setText("#");
        getContentPane().add(sixSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 276, -1, -1));

        sixTwelve.setText("#");
        getContentPane().add(sixTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 276, -1, -1));

        sixTwo.setText("#");
        getContentPane().add(sixTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 276, -1, -1));

        sixSix.setText("#");
        getContentPane().add(sixSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 276, -1, -1));

        sixZero.setText("#");
        getContentPane().add(sixZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 276, -1, -1));

        sixOne.setText("#");
        getContentPane().add(sixOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 276, -1, -1));

        sixTen.setText("#");
        getContentPane().add(sixTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 276, -1, -1));

        sixSeven.setText("#");
        getContentPane().add(sixSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 276, -1, -1));

        sixNine.setText("#");
        getContentPane().add(sixNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 276, -1, -1));

        sixThree.setText("#");
        getContentPane().add(sixThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 276, -1, -1));

        sixNineteen.setText("#");
        getContentPane().add(sixNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 276, -1, -1));

        sixFour.setText("#");
        getContentPane().add(sixFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 276, -1, -1));

        sixEighteen.setText("#");
        getContentPane().add(sixEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 276, -1, -1));

        sixThirteen.setText("#");
        getContentPane().add(sixThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 276, -1, -1));

        sixSixteen.setText("#");
        getContentPane().add(sixSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 276, -1, -1));

        sixEleven.setText("#");
        getContentPane().add(sixEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 276, -1, -1));

        sevenFourteen.setText("#");
        getContentPane().add(sevenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 313, -1, -1));

        sevenFifteen.setText("#");
        getContentPane().add(sevenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 313, -1, -1));

        sevenEight.setText("#");
        getContentPane().add(sevenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 313, -1, -1));

        sevenNineteen.setText("#");
        getContentPane().add(sevenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 313, -1, -1));

        sevenFour.setText("#");
        getContentPane().add(sevenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 313, -1, -1));

        sevenThree.setText("#");
        getContentPane().add(sevenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 313, -1, -1));

        sevenEleven.setText("#");
        getContentPane().add(sevenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 313, -1, -1));

        sevenSixteen.setText("#");
        getContentPane().add(sevenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 313, -1, -1));

        sevenThirteen.setText("#");
        getContentPane().add(sevenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 313, -1, -1));

        sevenEighteen.setText("#");
        getContentPane().add(sevenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 313, -1, -1));

        sevenFive.setText("#");
        getContentPane().add(sevenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 313, -1, -1));

        sevenSeventeen.setText("#");
        getContentPane().add(sevenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 313, -1, -1));

        sevenTwo.setText("#");
        getContentPane().add(sevenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 313, -1, -1));

        sevenTwelve.setText("#");
        getContentPane().add(sevenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 313, -1, -1));

        sevenZero.setText("#");
        getContentPane().add(sevenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 313, -1, -1));

        sevenSix.setText("#");
        getContentPane().add(sevenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 313, -1, -1));

        sevenTen.setText("#");
        getContentPane().add(sevenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 313, -1, -1));

        sevenOne.setText("#");
        getContentPane().add(sevenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 313, -1, -1));

        sevenNine.setText("#");
        getContentPane().add(sevenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 313, -1, -1));

        sevenSeven.setText("#");
        getContentPane().add(sevenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 313, -1, -1));

        eightTen.setText("#");
        getContentPane().add(eightTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 350, -1, -1));

        eightOne.setText("#");
        getContentPane().add(eightOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 350, -1, -1));

        eightZero.setText("#");
        getContentPane().add(eightZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 350, -1, -1));

        eightSix.setText("#");
        getContentPane().add(eightSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 350, -1, -1));

        eightTwelve.setText("#");
        getContentPane().add(eightTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 350, -1, -1));

        eightSeven.setText("#");
        getContentPane().add(eightSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 350, -1, -1));

        eightNine.setText("#");
        getContentPane().add(eightNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 350, -1, -1));

        eightNineteen.setText("#");
        getContentPane().add(eightNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 350, -1, -1));

        eightFour.setText("#");
        getContentPane().add(eightFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 350, -1, -1));

        eightThree.setText("#");
        getContentPane().add(eightThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 350, -1, -1));

        eightEleven.setText("#");
        getContentPane().add(eightEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 350, -1, -1));

        eightTwo.setText("#");
        getContentPane().add(eightTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 350, -1, -1));

        eightSeventeen.setText("#");
        getContentPane().add(eightSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 350, -1, -1));

        eightThirteen.setText("#");
        getContentPane().add(eightThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 350, -1, -1));

        eightSixteen.setText("#");
        getContentPane().add(eightSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 350, -1, -1));

        eightFive.setText("#");
        getContentPane().add(eightFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 350, -1, -1));

        eightEighteen.setText("#");
        getContentPane().add(eightEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 350, -1, -1));

        eightEight.setText("#");
        getContentPane().add(eightEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 350, -1, -1));

        eightFifteen.setText("#");
        getContentPane().add(eightFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 350, -1, -1));

        eightFourteen.setText("#");
        getContentPane().add(eightFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 350, -1, -1));

        nineFifteen.setText("#");
        getContentPane().add(nineFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 387, -1, -1));

        nineEight.setText("#");
        getContentPane().add(nineEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 387, -1, -1));

        nineFourteen.setText("#");
        getContentPane().add(nineFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 387, -1, -1));

        nineSixteen.setText("#");
        getContentPane().add(nineSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 387, -1, -1));

        nineThirteen.setText("#");
        getContentPane().add(nineThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 387, -1, -1));

        nineEighteen.setText("#");
        getContentPane().add(nineEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 387, -1, -1));

        nineFive.setText("#");
        getContentPane().add(nineFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 387, -1, -1));

        nineTwelve.setText("#");
        getContentPane().add(nineTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 387, -1, -1));

        nineSix.setText("#");
        getContentPane().add(nineSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 387, -1, -1));

        nineNine.setText("#");
        getContentPane().add(nineNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 387, -1, -1));

        nineSeven.setText("#");
        getContentPane().add(nineSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 387, -1, -1));

        nineFour.setText("#");
        getContentPane().add(nineFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 387, -1, -1));

        nineNineteen.setText("#");
        getContentPane().add(nineNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 387, -1, -1));

        nineThree.setText("#");
        getContentPane().add(nineThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 387, -1, -1));

        nineEleven.setText("#");
        getContentPane().add(nineEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 387, -1, -1));

        nineTwo.setText("#");
        getContentPane().add(nineTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 387, -1, -1));

        nineSeventeen.setText("#");
        getContentPane().add(nineSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 387, -1, -1));

        nineZero.setText("#");
        getContentPane().add(nineZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 387, -1, -1));

        nineOne.setText("#");
        getContentPane().add(nineOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 387, -1, -1));

        nineTen.setText("#");
        getContentPane().add(nineTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 387, -1, -1));

        tenFourteen.setText("#");
        getContentPane().add(tenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 424, -1, -1));

        tenEight.setText("#");
        getContentPane().add(tenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 424, -1, -1));

        tenFifteen.setText("#");
        getContentPane().add(tenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 424, -1, -1));

        tenFour.setText("#");
        getContentPane().add(tenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 424, -1, -1));

        tenSeven.setText("#");
        getContentPane().add(tenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 424, -1, -1));

        tenNine.setText("#");
        getContentPane().add(tenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 424, -1, -1));

        tenSix.setText("#");
        getContentPane().add(tenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 424, -1, -1));

        tenNineteen.setText("#");
        getContentPane().add(tenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 424, -1, -1));

        tenSixteen.setText("#");
        getContentPane().add(tenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 424, -1, -1));

        tenTwelve.setText("#");
        getContentPane().add(tenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 424, -1, -1));

        tenFive.setText("#");
        getContentPane().add(tenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 424, -1, -1));

        tenEighteen.setText("#");
        getContentPane().add(tenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 424, -1, -1));

        tenThirteen.setText("#");
        getContentPane().add(tenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 424, -1, -1));

        tenTen.setText("#");
        getContentPane().add(tenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 424, -1, -1));

        tenEleven.setText("#");
        getContentPane().add(tenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 424, -1, -1));

        tenThree.setText("#");
        getContentPane().add(tenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 424, -1, -1));

        tenSeventeen.setText("#");
        getContentPane().add(tenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 424, -1, -1));

        tenTwo.setText("#");
        getContentPane().add(tenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 424, -1, -1));

        tenOne.setText("#");
        getContentPane().add(tenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 424, -1, -1));

        tenZero.setText("#");
        getContentPane().add(tenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 424, -1, -1));

        elevenFourteen.setText("#");
        getContentPane().add(elevenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 461, -1, -1));

        elevenFifteen.setText("#");
        getContentPane().add(elevenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 461, -1, -1));

        elevenEight.setText("#");
        getContentPane().add(elevenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 461, -1, -1));

        elevenSixteen.setText("#");
        getContentPane().add(elevenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 461, -1, -1));

        elevenTwelve.setText("#");
        getContentPane().add(elevenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 461, -1, -1));

        elevenSix.setText("#");
        getContentPane().add(elevenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 461, -1, -1));

        elevenNineteen.setText("#");
        getContentPane().add(elevenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 461, -1, -1));

        elevenSeven.setText("#");
        getContentPane().add(elevenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 461, -1, -1));

        elevenNine.setText("#");
        getContentPane().add(elevenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 461, -1, -1));

        elevenFour.setText("#");
        getContentPane().add(elevenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 461, -1, -1));

        elevenThirteen.setText("#");
        getContentPane().add(elevenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 461, -1, -1));

        elevenFive.setText("#");
        getContentPane().add(elevenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 461, -1, -1));

        elevenEighteen.setText("#");
        getContentPane().add(elevenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 461, -1, -1));

        elevenTwo.setText("#");
        getContentPane().add(elevenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 461, -1, -1));

        elevenOne.setText("#");
        getContentPane().add(elevenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 461, -1, -1));

        elevenZero.setText("#");
        getContentPane().add(elevenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 461, -1, -1));

        elevenTen.setText("#");
        getContentPane().add(elevenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 461, -1, -1));

        elevenEleven.setText("#");
        getContentPane().add(elevenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 461, -1, -1));

        elevenThree.setText("#");
        getContentPane().add(elevenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 461, -1, -1));

        elevenSeventeen.setText("#");
        getContentPane().add(elevenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 461, -1, -1));

        twelveFourteen.setText("#");
        getContentPane().add(twelveFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 498, -1, -1));

        twelveFifteen.setText("#");
        getContentPane().add(twelveFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 498, -1, -1));

        twelveEight.setText("#");
        getContentPane().add(twelveEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 498, -1, -1));

        twelveSixteen.setText("#");
        getContentPane().add(twelveSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 498, -1, -1));

        twelveNineteen.setText("#");
        getContentPane().add(twelveNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 498, -1, -1));

        twelveSeven.setText("#");
        getContentPane().add(twelveSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 498, -1, -1));

        twelveTwelve.setText("#");
        getContentPane().add(twelveTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 498, -1, -1));

        twelveSix.setText("#");
        getContentPane().add(twelveSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 498, -1, -1));

        twelveThirteen.setText("#");
        getContentPane().add(twelveThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 498, -1, -1));

        twelveFive.setText("#");
        getContentPane().add(twelveFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 498, -1, -1));

        twelveNine.setText("#");
        getContentPane().add(twelveNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 498, -1, -1));

        twelveFour.setText("#");
        getContentPane().add(twelveFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 498, -1, -1));

        twelveEighteen.setText("#");
        getContentPane().add(twelveEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 498, -1, -1));

        twelveOne.setText("#");
        getContentPane().add(twelveOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 498, -1, -1));

        twelveTwo.setText("#");
        getContentPane().add(twelveTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 498, -1, -1));

        twelveZero.setText("#");
        getContentPane().add(twelveZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 498, -1, -1));

        twelveTen.setText("#");
        getContentPane().add(twelveTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 498, -1, -1));

        twelveEleven.setText("#");
        getContentPane().add(twelveEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 498, -1, -1));

        twelveThree.setText("#");
        getContentPane().add(twelveThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 498, -1, -1));

        twelveSeventeen.setText("#");
        getContentPane().add(twelveSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 498, -1, -1));

        thirteenFourteen.setText("#");
        getContentPane().add(thirteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 535, -1, -1));

        thirteenFifteen.setText("#");
        getContentPane().add(thirteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 535, -1, -1));

        thirteenEight.setText("#");
        getContentPane().add(thirteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 535, -1, -1));

        thirteenSeventeen.setText("#");
        getContentPane().add(thirteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 535, -1, -1));

        thirteenThree.setText("#");
        getContentPane().add(thirteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 535, -1, -1));

        thirteenEleven.setText("#");
        getContentPane().add(thirteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 535, -1, -1));

        thirteenZero.setText("#");
        getContentPane().add(thirteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 535, -1, -1));

        thirteenTen.setText("#");
        getContentPane().add(thirteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 535, -1, -1));

        thirteenOne.setText("#");
        getContentPane().add(thirteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 535, -1, -1));

        thirteenTwo.setText("#");
        getContentPane().add(thirteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 535, -1, -1));

        thirteenSix.setText("#");
        getContentPane().add(thirteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 535, -1, -1));

        thirteenTwelve.setText("#");
        getContentPane().add(thirteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 535, -1, -1));

        thirteenFive.setText("#");
        getContentPane().add(thirteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 535, -1, -1));

        thirteenThirteen.setText("#");
        getContentPane().add(thirteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 535, -1, -1));

        thirteenFour.setText("#");
        getContentPane().add(thirteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 535, -1, -1));

        thirteenNine.setText("#");
        getContentPane().add(thirteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 535, -1, -1));

        thirteenEighteen.setText("#");
        getContentPane().add(thirteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 535, -1, -1));

        thirteenSixteen.setText("#");
        getContentPane().add(thirteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 535, -1, -1));

        thirteenNineteen.setText("#");
        getContentPane().add(thirteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 535, -1, -1));

        thirteenSeven.setText("#");
        getContentPane().add(thirteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 535, -1, -1));

        fourteenFourteen.setText("#");
        getContentPane().add(fourteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 572, -1, -1));

        fourteenEight.setText("#");
        getContentPane().add(fourteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 572, -1, -1));

        fourteenFifteen.setText("#");
        getContentPane().add(fourteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 572, -1, -1));

        fourteenFive.setText("#");
        getContentPane().add(fourteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 572, -1, -1));

        fourteenTwo.setText("#");
        getContentPane().add(fourteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 572, -1, -1));

        fourteenOne.setText("#");
        getContentPane().add(fourteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 572, -1, -1));

        fourteenTwelve.setText("#");
        getContentPane().add(fourteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 572, -1, -1));

        fourteenSix.setText("#");
        getContentPane().add(fourteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 572, -1, -1));

        fourteenThree.setText("#");
        getContentPane().add(fourteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 572, -1, -1));

        fourteenEleven.setText("#");
        getContentPane().add(fourteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 572, -1, -1));

        fourteenZero.setText("#");
        getContentPane().add(fourteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 572, -1, -1));

        fourteenTen.setText("#");
        getContentPane().add(fourteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 572, -1, -1));

        fourteenSeventeen.setText("#");
        getContentPane().add(fourteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 572, -1, -1));

        fourteenNineteen.setText("#");
        getContentPane().add(fourteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 572, -1, -1));

        fourteenSeven.setText("#");
        getContentPane().add(fourteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 572, -1, -1));

        fourteenEighteen.setText("#");
        getContentPane().add(fourteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 572, -1, -1));

        fourteenSixteen.setText("#");
        getContentPane().add(fourteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 572, -1, -1));

        fourteenFour.setText("#");
        getContentPane().add(fourteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 572, -1, -1));

        fourteenNine.setText("#");
        getContentPane().add(fourteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 572, -1, -1));

        fourteenThirteen.setText("#");
        getContentPane().add(fourteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 572, -1, -1));

        fifteenTwelve.setText("#");
        getContentPane().add(fifteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 609, -1, -1));

        fifteenOne.setText("#");
        getContentPane().add(fifteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 609, -1, -1));

        fifteenTwo.setText("#");
        getContentPane().add(fifteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 609, -1, -1));

        fifteenFive.setText("#");
        getContentPane().add(fifteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 609, -1, -1));

        fifteenSeventeen.setText("#");
        getContentPane().add(fifteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 609, -1, -1));

        fifteenTen.setText("#");
        getContentPane().add(fifteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 609, -1, -1));

        fifteenZero.setText("#");
        getContentPane().add(fifteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 609, -1, -1));

        fifteenEleven.setText("#");
        getContentPane().add(fifteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 609, -1, -1));

        fifteenThree.setText("#");
        getContentPane().add(fifteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 609, -1, -1));

        fifteenSix.setText("#");
        getContentPane().add(fifteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 609, -1, -1));

        fifteenEighteen.setText("#");
        getContentPane().add(fifteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 609, -1, -1));

        fifteenSeven.setText("#");
        getContentPane().add(fifteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 609, -1, -1));

        fifteenFour.setText("#");
        getContentPane().add(fifteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 609, -1, -1));

        fifteenSixteen.setText("#");
        getContentPane().add(fifteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 609, -1, -1));

        fifteenNineteen.setText("#");
        getContentPane().add(fifteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 609, -1, -1));

        fifteenThirteen.setText("#");
        getContentPane().add(fifteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 609, -1, -1));

        fifteenNine.setText("#");
        getContentPane().add(fifteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 609, -1, -1));

        fifteenFifteen.setText("#");
        getContentPane().add(fifteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 609, -1, -1));

        fifteenFourteen.setText("#");
        getContentPane().add(fifteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 609, -1, -1));

        fifteenEight.setText("#");
        getContentPane().add(fifteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 609, -1, -1));

        sixteenSeventeen.setText("#");
        getContentPane().add(sixteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 646, -1, -1));

        sixteenFive.setText("#");
        getContentPane().add(sixteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 646, -1, -1));

        sixteenThree.setText("#");
        getContentPane().add(sixteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 646, -1, -1));

        sixteenEleven.setText("#");
        getContentPane().add(sixteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 646, -1, -1));

        sixteenZero.setText("#");
        getContentPane().add(sixteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 646, -1, -1));

        sixteenTen.setText("#");
        getContentPane().add(sixteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 646, -1, -1));

        sixteenFour.setText("#");
        getContentPane().add(sixteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 646, -1, -1));

        sixteenSeven.setText("#");
        getContentPane().add(sixteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 646, -1, -1));

        sixteenEighteen.setText("#");
        getContentPane().add(sixteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 646, -1, -1));

        sixteenSix.setText("#");
        getContentPane().add(sixteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 646, -1, -1));

        sixteenSixteen.setText("#");
        getContentPane().add(sixteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 646, -1, -1));

        sixteenThirteen.setText("#");
        getContentPane().add(sixteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 646, -1, -1));

        sixteenNineteen.setText("#");
        getContentPane().add(sixteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 646, -1, -1));

        sixteenFifteen.setText("#");
        getContentPane().add(sixteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 646, -1, -1));

        sixteenNine.setText("#");
        getContentPane().add(sixteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 646, -1, -1));

        sixteenEight.setText("#");
        getContentPane().add(sixteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 646, -1, -1));

        sixteenFourteen.setText("#");
        getContentPane().add(sixteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 646, -1, -1));

        sixteenTwo.setText("#");
        getContentPane().add(sixteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 646, -1, -1));

        sixteenOne.setText("#");
        getContentPane().add(sixteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 646, -1, -1));

        sixteenTwelve.setText("#");
        getContentPane().add(sixteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 646, -1, -1));

        seventeenFive.setText("#");
        getContentPane().add(seventeenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 683, -1, -1));

        seventeenSeventeen.setText("#");
        getContentPane().add(seventeenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 683, -1, -1));

        seventeenThree.setText("#");
        getContentPane().add(seventeenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 683, -1, -1));

        seventeenThirteen.setText("#");
        getContentPane().add(seventeenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 683, -1, -1));

        seventeenNineteen.setText("#");
        getContentPane().add(seventeenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 683, -1, -1));

        seventeenSix.setText("#");
        getContentPane().add(seventeenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 683, -1, -1));

        seventeenSixteen.setText("#");
        getContentPane().add(seventeenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 683, -1, -1));

        seventeenSeven.setText("#");
        getContentPane().add(seventeenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 683, -1, -1));

        seventeenEighteen.setText("#");
        getContentPane().add(seventeenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 683, -1, -1));

        seventeenTen.setText("#");
        getContentPane().add(seventeenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 683, -1, -1));

        seventeenFour.setText("#");
        getContentPane().add(seventeenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 683, -1, -1));

        seventeenEleven.setText("#");
        getContentPane().add(seventeenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 683, -1, -1));

        seventeenZero.setText("#");
        getContentPane().add(seventeenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 683, -1, -1));

        seventeenFifteen.setText("#");
        getContentPane().add(seventeenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 683, -1, -1));

        seventeenOne.setText("#");
        getContentPane().add(seventeenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 683, -1, -1));

        seventeenTwelve.setText("#");
        getContentPane().add(seventeenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 683, -1, -1));

        seventeenNine.setText("#");
        getContentPane().add(seventeenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 683, -1, -1));

        seventeenEight.setText("#");
        getContentPane().add(seventeenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 683, -1, -1));

        seventeenFourteen.setText("#");
        getContentPane().add(seventeenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 683, -1, -1));

        seventeenTwo.setText("#");
        getContentPane().add(seventeenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 683, -1, -1));

        eighteenNineteen.setText("#");
        getContentPane().add(eighteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 720, -1, -1));

        eighteenThirteen.setText("#");
        getContentPane().add(eighteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 720, -1, -1));

        eighteenSeven.setText("#");
        getContentPane().add(eighteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 720, -1, -1));

        eighteenEighteen.setText("#");
        getContentPane().add(eighteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 720, -1, -1));

        eighteenSix.setText("#");
        getContentPane().add(eighteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 720, -1, -1));

        eighteenSixteen.setText("#");
        getContentPane().add(eighteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 720, -1, -1));

        eighteenEleven.setText("#");
        getContentPane().add(eighteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 720, -1, -1));

        eighteenZero.setText("#");
        getContentPane().add(eighteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 720, -1, -1));

        eighteenTen.setText("#");
        getContentPane().add(eighteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 720, -1, -1));

        eighteenFour.setText("#");
        getContentPane().add(eighteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 720, -1, -1));

        eighteenFifteen.setText("#");
        getContentPane().add(eighteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 720, -1, -1));

        eighteenTwelve.setText("#");
        getContentPane().add(eighteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 720, -1, -1));

        eighteenOne.setText("#");
        getContentPane().add(eighteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 720, -1, -1));

        eighteenNine.setText("#");
        getContentPane().add(eighteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 720, -1, -1));

        eighteenEight.setText("#");
        getContentPane().add(eighteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 720, -1, -1));

        eighteenFourteen.setText("#");
        getContentPane().add(eighteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 720, -1, -1));

        eighteenTwo.setText("#");
        getContentPane().add(eighteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 720, -1, -1));

        eighteenThree.setText("#");
        getContentPane().add(eighteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 720, -1, -1));

        eighteenFive.setText("#");
        getContentPane().add(eighteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 720, -1, -1));

        eighteenSeventeen.setText("#");
        getContentPane().add(eighteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 720, -1, -1));

        nineteenEight.setText("#");
        getContentPane().add(nineteenEight, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 757, -1, -1));

        nineteenNine.setText("#");
        getContentPane().add(nineteenNine, new org.netbeans.lib.awtextra.AbsoluteConstraints(437, 757, -1, -1));

        nineteenSeventeen.setText("#");
        getContentPane().add(nineteenSeventeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 757, -1, -1));

        nineteenFive.setText("#");
        getContentPane().add(nineteenFive, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 757, -1, -1));

        nineteenThree.setText("#");
        getContentPane().add(nineteenThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 757, -1, -1));

        nineteenTwo.setText("#");
        getContentPane().add(nineteenTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 757, -1, -1));

        nineteenFourteen.setText("#");
        getContentPane().add(nineteenFourteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 757, -1, -1));

        nineteenNineteen.setText("#");
        getContentPane().add(nineteenNineteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(954, 757, -1, -1));

        nineteenThirteen.setText("#");
        getContentPane().add(nineteenThirteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 757, -1, -1));

        nineteenSeven.setText("#");
        getContentPane().add(nineteenSeven, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 757, -1, -1));

        nineteenTen.setText("#");
        getContentPane().add(nineteenTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 757, -1, -1));

        nineteenZero.setText("#");
        getContentPane().add(nineteenZero, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 757, -1, -1));

        nineteenFifteen.setText("#");
        getContentPane().add(nineteenFifteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(742, 757, -1, -1));

        nineteenFour.setText("#");
        getContentPane().add(nineteenFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 757, -1, -1));

        nineteenOne.setText("#");
        getContentPane().add(nineteenOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 757, -1, -1));

        nineteenTwelve.setText("#");
        getContentPane().add(nineteenTwelve, new org.netbeans.lib.awtextra.AbsoluteConstraints(578, 757, -1, -1));

        nineteenEighteen.setText("#");
        getContentPane().add(nineteenEighteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 757, -1, -1));

        nineteenSix.setText("#");
        getContentPane().add(nineteenSix, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 757, -1, -1));

        nineteenSixteen.setText("#");
        getContentPane().add(nineteenSixteen, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 757, -1, -1));

        nineteenEleven.setText("#");
        getContentPane().add(nineteenEleven, new org.netbeans.lib.awtextra.AbsoluteConstraints(527, 757, -1, -1));

        startSimulationjButton1.setText("START");
        startSimulationjButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationjButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(startSimulationjButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 11, 199, -1));

        resetjButton1.setForeground(new java.awt.Color(255, 0, 0));
        resetjButton1.setText("RESET");
        resetjButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetjButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(resetjButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(498, 11, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startSimulationjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationjButton1ActionPerformed
        
        relativeMap = new HashMap<JLabel, HashMap<Node, ArrayList<Node>>>();
        adjNodeMap = new HashMap<Node, ArrayList<Node>>();
        expectedDronePath = new LinkedList<>();
        actualOldExpectedDronePath = new LinkedList<>();
        airDrone = new AirDrone();
        dronePreviousNodeJLabelList = new ArrayList<>();
        tripDetail = new TripDetail();
        
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 1000, 4000);
        
        airDrone.getSource().setXcordinate("zero");
        airDrone.getSource().setYcordinate("Zero");
        
        airDrone.getDestination().setXcordinate("ten");
        airDrone.getDestination().setYcordinate("Ten");
        
        calculateDronePath(airDrone.getSource(),airDrone.getDestination(),expectedDronePath);
        actualOldExpectedDronePath = expectedDronePath;
        
        tripDetail.pathTaken.add(source);
        
        
        //check machine for existing path
//        for (String keyInMap : frequencyData.keySet()){
//            if (frequencyData.get(keyInMap).equals(key)) {
//                return key;
        
//        Map<String, Map<String, Value>> outerMap = new HashMap<String, HashMap<String, Value>>();
//Map<String, Value> innerMap = new HashMap<String, Value>();    
//innerMap.put("innerKey", new Value());
        
//        Storing a map
//
//outerMap.put("key", innerMap);
//Retrieving a map and its values
//
//Map<String, Value> map = outerMap.get("key");
//Value value = map.get("innerKey");
        
        
        
        if(tripCount>0){ 
            String sourceNodeVal2 = getStringValueOfNode(airDrone.getSource());
            String destNodeVal2 = getStringValueOfNode(airDrone.getDestination());
                
                //check if any trip details exist in machine map and if not, then create it
              tempMap  = sourceDestinationFullMap.get(sourceNodeVal2);
              List<TripDetail> tempTripList = tempMap.get(destNodeVal2);
              
              if(!(tempTripList==null)){
                  TripDetail tempTripDetail;
                 //Get list of all trips between source and destination and calculate best one
                  if (tempTripList.size()==1){
                      tempTripDetail=tempTripList.get(0);
                  }else{
                      Collections.sort(tempTripList);
                      //Arrays.sort(tempTripList.toArray());
                      tempTripDetail=tempTripList.get(0);
                  }
                 expectedDronePath=tempTripDetail.pathTaken;
                  
              }else{
                  //Make new trip list for non existant paths for the new source and destination
                  tripList = new ArrayList<TripDetail>();
              }
        }
        //increase number of trip counts in every new trip
        tripCount++;
        
        timer.scheduleAtFixedRate(new flyExpectedTask(), 1000, 1000);
    }//GEN-LAST:event_startSimulationjButton1ActionPerformed

    private void resetjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetjButton1ActionPerformed
       
        cancelAllTimerAndReset();
    }//GEN-LAST:event_resetjButton1ActionPerformed

    public void cancelAllTimerAndReset(){
        i = 0;
        k = 0;
        xx = 0;
        counter1 = 0;
        expectedDronePath.clear();
        timer.cancel();
        clearAllAnomalies();
    }
    
    public ArrayList<Node> createAdjacentNodeList(Node n){
        
        ArrayList<Node> adjNodeArray = new ArrayList<>();
        Node tempNode = new Node();
        
        String xValue= n.getXcordinate();
        String yValue = n.getYcordinate();
        yValue=yValue.toLowerCase();
        
        String adjXName;
        String adjYName;
        int adjXno;
        int adjYno;
        
        int xInt = dm.getIntForString(xValue);
        int yInt = dm.getIntForString(yValue);
        
        if (xInt==0 && yInt==0){
            
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            adjYName=adjYName.substring(0, 1).toUpperCase() + adjYName.substring(1);
            tempNode.setXcordinate(adjXName);
            tempNode.setYcordinate(adjYName);
            adjNodeArray.add(tempNode);
            
            Node n3 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            adjYName=adjYName.substring(0, 1).toUpperCase() + adjYName.substring(1);
            n3.setXcordinate(adjXName);
            n3.setYcordinate(adjYName);
            adjNodeArray.add(n3);
            
            Node n4 = new Node();
            adjXno=xInt+1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            adjYName=adjYName.substring(0, 1).toUpperCase() + adjYName.substring(1);
            n4.setXcordinate(adjXName);
            n4.setYcordinate(adjYName);
            adjNodeArray.add(n4);
            
            
        } else if(xInt==19 && yInt==19){
            
            
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            tempNode.setXcordinate(adjXName);
            tempNode.setYcordinate(adjYName);
            adjNodeArray.add(tempNode);
            
            Node n3 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n3.setXcordinate(adjXName);
            n3.setYcordinate(adjYName);
            adjNodeArray.add(n3);
            
            Node n4 = new Node();
            adjXno=xInt-1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n4.setXcordinate(adjXName);
            n4.setYcordinate(adjYName);
            adjNodeArray.add(n4);
            
        } else if(xInt==0 && yInt==19){
            
            
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            tempNode.setXcordinate(adjXName);
            tempNode.setYcordinate(adjYName);
            adjNodeArray.add(tempNode);
            
            Node n3 = new Node();
            adjXno=xInt+1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n3.setXcordinate(adjXName);
            n3.setYcordinate(adjYName);
            adjNodeArray.add(n3);
            
            Node n4 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n4.setXcordinate(adjXName);
            n4.setYcordinate(adjYName);
            adjNodeArray.add(n4);
            
        } else if(xInt==19 && yInt==0){
            
            
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            tempNode.setXcordinate(adjXName);
            tempNode.setYcordinate(adjYName);
            adjNodeArray.add(tempNode);
            
            Node n3 = new Node();
            adjXno=xInt-1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n3.setXcordinate(adjXName);
            n3.setYcordinate(adjYName);
            adjNodeArray.add(n3);
            
            Node n4 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n4.setXcordinate(adjXName);
            n4.setYcordinate(adjYName);
            adjNodeArray.add(n4);
            
        }else if(xInt==0 && (yInt>=1 && yInt<=18)){
            
            Node n8 = new Node();
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n8.setXcordinate(adjXName);
            n8.setYcordinate(adjYName);
            adjNodeArray.add(n8);
            
            Node n9 = new Node();
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n9.setXcordinate(adjXName);
            n9.setYcordinate(adjYName);
            adjNodeArray.add(n9);
            
            Node n10 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n10.setXcordinate(adjXName);
            n10.setYcordinate(adjYName);
            adjNodeArray.add(n10);
            
            Node n11 = new Node();
            adjXno=xInt+1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n11.setXcordinate(adjXName);
            n11.setYcordinate(adjYName);
            adjNodeArray.add(n11);
            
            Node n12 = new Node();
            adjXno=xInt+1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n12.setXcordinate(adjXName);
            n12.setYcordinate(adjYName);
            adjNodeArray.add(n12);
            
        } else if(xInt==19 && (yInt>=1 && yInt<=18)){
            
            Node n13 = new Node();
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n13.setXcordinate(adjXName);
            n13.setYcordinate(adjYName);
            adjNodeArray.add(n13);
            
            Node n14 = new Node();
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n14.setXcordinate(adjXName);
            n14.setYcordinate(adjYName);
            adjNodeArray.add(tempNode);
            
            Node n15 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n15.setXcordinate(adjXName);
            n15.setYcordinate(adjYName);
            adjNodeArray.add(n15);
            
            Node n16 = new Node();
            adjXno=xInt-1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n16.setXcordinate(adjXName);
            n16.setYcordinate(adjYName);
            adjNodeArray.add(n16);
            
            Node n17 = new Node();
            adjXno=xInt-1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n17.setXcordinate(adjXName);
            n17.setYcordinate(adjYName);
            adjNodeArray.add(n17);
            
        } else if((xInt>=1 && xInt<=18) && yInt==0){
            
            Node n18 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n18.setXcordinate(adjXName);
            n18.setYcordinate(adjYName);
            adjNodeArray.add(n18);
            
            Node n19 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n19.setXcordinate(adjXName);
            n19.setYcordinate(adjYName);
            adjNodeArray.add(n19);
            
            Node n20 = new Node();
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n20.setXcordinate(adjXName);
            n20.setYcordinate(adjYName);
            adjNodeArray.add(n20);
            
            Node n21 = new Node();
            adjXno=xInt-1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n21.setXcordinate(adjXName);
            n21.setYcordinate(adjYName);
            adjNodeArray.add(n21);
            
            Node n22 = new Node();
            adjXno=xInt+1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n22.setXcordinate(adjXName);
            n22.setYcordinate(adjYName);
            adjNodeArray.add(n22);
            
        } else if((xInt>=1 && xInt<=18) && yInt==19){
            
            Node n23 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n23.setXcordinate(adjXName);
            n23.setYcordinate(adjYName);
            adjNodeArray.add(n23);
            
            Node n24 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n24.setXcordinate(adjXName);
            n24.setYcordinate(adjYName);
            adjNodeArray.add(n24);
            
            Node n25 = new Node();
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n25.setXcordinate(adjXName);
            n25.setYcordinate(adjYName);
            adjNodeArray.add(n25);
            
            Node n26 = new Node();
            adjXno=xInt-1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n26.setXcordinate(adjXName);
            n26.setYcordinate(adjYName);
            adjNodeArray.add(n26);
            
            Node n27 = new Node();
            adjXno=xInt+1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n27.setXcordinate(adjXName);
            n27.setYcordinate(adjYName);
            adjNodeArray.add(n27);
            
        }  else {
            
            Node n28 = new Node();
            adjXno=xInt;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n28.setXcordinate(adjXName);
            n28.setYcordinate(adjYName);
            adjNodeArray.add(n28);
            
            Node n29 = new Node();
            adjXno=xInt;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n29.setXcordinate(adjXName);
            n29.setYcordinate(adjYName);
            adjNodeArray.add(n29);
            
            Node n30 = new Node();
            adjXno=xInt-1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n30.setXcordinate(adjXName);
            n30.setYcordinate(adjYName);
            adjNodeArray.add(n30);
            
            Node n31 = new Node();
            adjXno=xInt-1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n31.setXcordinate(adjXName);
            n31.setYcordinate(adjYName);
            adjNodeArray.add(n31);
            
            Node n32 = new Node();
            adjXno=xInt-1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n32.setXcordinate(adjXName);
            n32.setYcordinate(adjYName);
            adjNodeArray.add(n32);
            
            Node n33 = new Node();
            adjXno=xInt+1;
            adjYno=yInt;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n33.setXcordinate(adjXName);
            n33.setYcordinate(adjYName);
            adjNodeArray.add(n33);
            
            Node n34 = new Node();
            adjXno=xInt+1;
            adjYno=yInt-1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n34.setXcordinate(adjXName);
            n34.setYcordinate(adjYName);
            adjNodeArray.add(n34);
            
            Node n35 = new Node();
            adjXno=xInt+1;
            adjYno=yInt+1;
            adjXName=dm.getStringForInt(adjXno);
            adjYName=dm.getStringForInt(adjYno);
            n35.setXcordinate(adjXName);
            n35.setYcordinate(adjYName);
            adjNodeArray.add(n35);
            
        }
        
        adjNodeMap.put(n, adjNodeArray); 
        
//        for (Node n2: adjNodeArray){
//            System.out.println(n2.getXcordinate()+n2.getYcordinate());
//            System.out.println(" ");
//        }
        
        return adjNodeArray;
    }
    
    
    private void setLabels(){
        
        zeroZero.setName("zeroZero");
        zeroOne.setName("zeroOne");
        zeroTwo.setName("zeroTwo");
        zeroThree.setName("zeroThree");
        zeroFour.setName("zeroFour");
        zeroFive.setName("zeroFive");
        zeroSix.setName("zeroSix");
        zeroSeven.setName("zeroSeven");
        zeroEight.setName("zeroEight");
        zeroNine.setName("zeroNine");
        zeroTen.setName("zeroTen");
        zeroEleven.setName("zeroEleven");
        zeroTwelve.setName("zeroTwelve");
        zeroThirteen.setName("zeroThirteen");
        zeroFourteen.setName("zeroFourteen");
        zeroFifteen.setName("zeroFifteen");
        zeroSixteen.setName("zeroSixteen");
        zeroSeventeen.setName("zeroSeventeen");
        zeroEighteen.setName("zeroEighteen");
        zeroNineteen.setName("zeroNineteen");
        oneZero.setName("oneZero");
        oneOne.setName("oneOne");
        oneTwo.setName("oneTwo");
        oneThree.setName("oneThree");
        oneFour.setName("oneFour");
        oneFive.setName("oneFive");
        oneSix.setName("oneSix");
        oneSeven.setName("oneSeven");
        oneEight.setName("oneEight");
        oneNine.setName("oneNine");
        oneTen.setName("oneTen");
        oneEleven.setName("oneEleven");
        oneTwelve.setName("oneTwelve");
        oneThirteen.setName("oneThirteen");
        oneFourteen.setName("oneFourteen");
        oneFifteen.setName("oneFifteen");
        oneSixteen.setName("oneSixteen");
        oneSeventeen.setName("oneSeventeen");
        oneEighteen.setName("oneEighteen");
        oneNineteen.setName("oneNineteen");
        twoZero.setName("twoZero");
        twoOne.setName("twoOne");
        twoTwo.setName("twoTwo");
        twoThree.setName("twoThree");
        twoFour.setName("twoFour");
        twoFive.setName("twoFive");
        twoSix.setName("twoSix");
        twoSeven.setName("twoSeven");
        twoEight.setName("twoEight");
        twoNine.setName("twoNine");
        twoTen.setName("twoTen");
        twoEleven.setName("twoEleven");
        twoTwelve.setName("twoTwelve");
        twoThirteen.setName("twoThirteen");
        twoFourteen.setName("twoFourteen");
        twoFifteen.setName("twoFifteen");
        twoSixteen.setName("twoSixteen");
        twoSeventeen.setName("twoSeventeen");
        twoEighteen.setName("twoEighteen");
        twoNineteen.setName("twoNineteen");
        threeZero.setName("threeZero");
        threeOne.setName("threeOne");
        threeTwo.setName("threeTwo");
        threeThree.setName("threeThree");
        threeFour.setName("threeFour");
        threeFive.setName("threeFive");
        threeSix.setName("threeSix");
        threeSeven.setName("threeSeven");
        threeEight.setName("threeEight");
        threeNine.setName("threeNine");
        threeTen.setName("threeTen");
        threeEleven.setName("threeEleven");
        threeTwelve.setName("threeTwelve");
        threeThirteen.setName("threeThirteen");
        threeFourteen.setName("threeFourteen");
        threeFifteen.setName("threeFifteen");
        threeSixteen.setName("threeSixteen");
        threeSeventeen.setName("threeSeventeen");
        threeEighteen.setName("threeEighteen");
        threeNineteen.setName("threeNineteen");
        fourZero.setName("fourZero");
        fourOne.setName("fourOne");
        fourTwo.setName("fourTwo");
        fourThree.setName("fourThree");
        fourFour.setName("fourFour");
        fourFive.setName("fourFive");
        fourSix.setName("fourSix");
        fourSeven.setName("fourSeven");
        fourEight.setName("fourEight");
        fourNine.setName("fourNine");
        fourTen.setName("fourTen");
        fourEleven.setName("fourEleven");
        fourTwelve.setName("fourTwelve");
        fourThirteen.setName("fourThirteen");
        fourFourteen.setName("fourFourteen");
        fourFifteen.setName("fourFifteen");
        fourSixteen.setName("fourSixteen");
        fourSeventeen.setName("fourSeventeen");
        fourEighteen.setName("fourEighteen");
        fourNineteen.setName("fourNineteen");
        fiveZero.setName("fiveZero");
        fiveOne.setName("fiveOne");
        fiveTwo.setName("fiveTwo");
        fiveThree.setName("fiveThree");
        fiveFour.setName("fiveFour");
        fiveFive.setName("fiveFive");
        fiveSix.setName("fiveSix");
        fiveSeven.setName("fiveSeven");
        fiveEight.setName("fiveEight");
        fiveNine.setName("fiveNine");
        fiveTen.setName("fiveTen");
        fiveEleven.setName("fiveEleven");
        fiveTwelve.setName("fiveTwelve");
        fiveThirteen.setName("fiveThirteen");
        fiveFourteen.setName("fiveFourteen");
        fiveFifteen.setName("fiveFifteen");
        fiveSixteen.setName("fiveSixteen");
        fiveSeventeen.setName("fiveSeventeen");
        fiveEighteen.setName("fiveEighteen");
        fiveNineteen.setName("fiveNineteen");
        sixZero.setName("sixZero");
        sixOne.setName("sixOne");
        sixTwo.setName("sixTwo");
        sixThree.setName("sixThree");
        sixFour.setName("sixFour");
        sixFive.setName("sixFive");
        sixSix.setName("sixSix");
        sixSeven.setName("sixSeven");
        sixEight.setName("sixEight");
        sixNine.setName("sixNine");
        sixTen.setName("sixTen");
        sixEleven.setName("sixEleven");
        sixTwelve.setName("sixTwelve");
        sixThirteen.setName("sixThirteen");
        sixFourteen.setName("sixFourteen");
        sixFifteen.setName("sixFifteen");
        sixSixteen.setName("sixSixteen");
        sixSeventeen.setName("sixSeventeen");
        sixEighteen.setName("sixEighteen");
        sixNineteen.setName("sixNineteen");
        sevenZero.setName("sevenZero");
        sevenOne.setName("sevenOne");
        sevenTwo.setName("sevenTwo");
        sevenThree.setName("sevenThree");
        sevenFour.setName("sevenFour");
        sevenFive.setName("sevenFive");
        sevenSix.setName("sevenSix");
        sevenSeven.setName("sevenSeven");
        sevenEight.setName("sevenEight");
        sevenNine.setName("sevenNine");
        sevenTen.setName("sevenTen");
        sevenEleven.setName("sevenEleven");
        sevenTwelve.setName("sevenTwelve");
        sevenThirteen.setName("sevenThirteen");
        sevenFourteen.setName("sevenFourteen");
        sevenFifteen.setName("sevenFifteen");
        sevenSixteen.setName("sevenSixteen");
        sevenSeventeen.setName("sevenSeventeen");
        sevenEighteen.setName("sevenEighteen");
        sevenNineteen.setName("sevenNineteen");
        eightZero.setName("eightZero");
        eightOne.setName("eightOne");
        eightTwo.setName("eightTwo");
        eightThree.setName("eightThree");
        eightFour.setName("eightFour");
        eightFive.setName("eightFive");
        eightSix.setName("eightSix");
        eightSeven.setName("eightSeven");
        eightEight.setName("eightEight");
        eightNine.setName("eightNine");
        eightTen.setName("eightTen");
        eightEleven.setName("eightEleven");
        eightTwelve.setName("eightTwelve");
        eightThirteen.setName("eightThirteen");
        eightFourteen.setName("eightFourteen");
        eightFifteen.setName("eightFifteen");
        eightSixteen.setName("eightSixteen");
        eightSeventeen.setName("eightSeventeen");
        eightEighteen.setName("eightEighteen");
        eightNineteen.setName("eightNineteen");
        nineZero.setName("nineZero");
        nineOne.setName("nineOne");
        nineTwo.setName("nineTwo");
        nineThree.setName("nineThree");
        nineFour.setName("nineFour");
        nineFive.setName("nineFive");
        nineSix.setName("nineSix");
        nineSeven.setName("nineSeven");
        nineEight.setName("nineEight");
        nineNine.setName("nineNine");
        nineTen.setName("nineTen");
        nineEleven.setName("nineEleven");
        nineTwelve.setName("nineTwelve");
        nineThirteen.setName("nineThirteen");
        nineFourteen.setName("nineFourteen");
        nineFifteen.setName("nineFifteen");
        nineSixteen.setName("nineSixteen");
        nineSeventeen.setName("nineSeventeen");
        nineEighteen.setName("nineEighteen");
        nineNineteen.setName("nineNineteen");
        tenZero.setName("tenZero");
        tenOne.setName("tenOne");
        tenTwo.setName("tenTwo");
        tenThree.setName("tenThree");
        tenFour.setName("tenFour");
        tenFive.setName("tenFive");
        tenSix.setName("tenSix");
        tenSeven.setName("tenSeven");
        tenEight.setName("tenEight");
        tenNine.setName("tenNine");
        tenTen.setName("tenTen");
        tenEleven.setName("tenEleven");
        tenTwelve.setName("tenTwelve");
        tenThirteen.setName("tenThirteen");
        tenFourteen.setName("tenFourteen");
        tenFifteen.setName("tenFifteen");
        tenSixteen.setName("tenSixteen");
        tenSeventeen.setName("tenSeventeen");
        tenEighteen.setName("tenEighteen");
        tenNineteen.setName("tenNineteen");
        elevenZero.setName("elevenZero");
        elevenOne.setName("elevenOne");
        elevenTwo.setName("elevenTwo");
        elevenThree.setName("elevenThree");
        elevenFour.setName("elevenFour");
        elevenFive.setName("elevenFive");
        elevenSix.setName("elevenSix");
        elevenSeven.setName("elevenSeven");
        elevenEight.setName("elevenEight");
        elevenNine.setName("elevenNine");
        elevenTen.setName("elevenTen");
        elevenEleven.setName("elevenEleven");
        elevenTwelve.setName("elevenTwelve");
        elevenThirteen.setName("elevenThirteen");
        elevenFourteen.setName("elevenFourteen");
        elevenFifteen.setName("elevenFifteen");
        elevenSixteen.setName("elevenSixteen");
        elevenSeventeen.setName("elevenSeventeen");
        elevenEighteen.setName("elevenEighteen");
        elevenNineteen.setName("elevenNineteen");
        twelveZero.setName("twelveZero");
        twelveOne.setName("twelveOne");
        twelveTwo.setName("twelveTwo");
        twelveThree.setName("twelveThree");
        twelveFour.setName("twelveFour");
        twelveFive.setName("twelveFive");
        twelveSix.setName("twelveSix");
        twelveSeven.setName("twelveSeven");
        twelveEight.setName("twelveEight");
        twelveNine.setName("twelveNine");
        twelveTen.setName("twelveTen");
        twelveEleven.setName("twelveEleven");
        twelveTwelve.setName("twelveTwelve");
        twelveThirteen.setName("twelveThirteen");
        twelveFourteen.setName("twelveFourteen");
        twelveFifteen.setName("twelveFifteen");
        twelveSixteen.setName("twelveSixteen");
        twelveSeventeen.setName("twelveSeventeen");
        twelveEighteen.setName("twelveEighteen");
        twelveNineteen.setName("twelveNineteen");
        thirteenZero.setName("thirteenZero");
        thirteenOne.setName("thirteenOne");
        thirteenTwo.setName("thirteenTwo");
        thirteenThree.setName("thirteenThree");
        thirteenFour.setName("thirteenFour");
        thirteenFive.setName("thirteenFive");
        thirteenSix.setName("thirteenSix");
        thirteenSeven.setName("thirteenSeven");
        thirteenEight.setName("thirteenEight");
        thirteenNine.setName("thirteenNine");
        thirteenTen.setName("thirteenTen");
        thirteenEleven.setName("thirteenEleven");
        thirteenTwelve.setName("thirteenTwelve");
        thirteenThirteen.setName("thirteenThirteen");
        thirteenFourteen.setName("thirteenFourteen");
        thirteenFifteen.setName("thirteenFifteen");
        thirteenSixteen.setName("thirteenSixteen");
        thirteenSeventeen.setName("thirteenSeventeen");
        thirteenEighteen.setName("thirteenEighteen");
        thirteenNineteen.setName("thirteenNineteen");
        fourteenZero.setName("fourteenZero");
        fourteenOne.setName("fourteenOne");
        fourteenTwo.setName("fourteenTwo");
        fourteenThree.setName("fourteenThree");
        fourteenFour.setName("fourteenFour");
        fourteenFive.setName("fourteenFive");
        fourteenSix.setName("fourteenSix");
        fourteenSeven.setName("fourteenSeven");
        fourteenEight.setName("fourteenEight");
        fourteenNine.setName("fourteenNine");
        fourteenTen.setName("fourteenTen");
        fourteenEleven.setName("fourteenEleven");
        fourteenTwelve.setName("fourteenTwelve");
        fourteenThirteen.setName("fourteenThirteen");
        fourteenFourteen.setName("fourteenFourteen");
        fourteenFifteen.setName("fourteenFifteen");
        fourteenSixteen.setName("fourteenSixteen");
        fourteenSeventeen.setName("fourteenSeventeen");
        fourteenEighteen.setName("fourteenEighteen");
        fourteenNineteen.setName("fourteenNineteen");
        fifteenZero.setName("fifteenZero");
        fifteenOne.setName("fifteenOne");
        fifteenTwo.setName("fifteenTwo");
        fifteenThree.setName("fifteenThree");
        fifteenFour.setName("fifteenFour");
        fifteenFive.setName("fifteenFive");
        fifteenSix.setName("fifteenSix");
        fifteenSeven.setName("fifteenSeven");
        fifteenEight.setName("fifteenEight");
        fifteenNine.setName("fifteenNine");
        fifteenTen.setName("fifteenTen");
        fifteenEleven.setName("fifteenEleven");
        fifteenTwelve.setName("fifteenTwelve");
        fifteenThirteen.setName("fifteenThirteen");
        fifteenFourteen.setName("fifteenFourteen");
        fifteenFifteen.setName("fifteenFifteen");
        fifteenSixteen.setName("fifteenSixteen");
        fifteenSeventeen.setName("fifteenSeventeen");
        fifteenEighteen.setName("fifteenEighteen");
        fifteenNineteen.setName("fifteenNineteen");
        sixteenZero.setName("sixteenZero");
        sixteenOne.setName("sixteenOne");
        sixteenTwo.setName("sixteenTwo");
        sixteenThree.setName("sixteenThree");
        sixteenFour.setName("sixteenFour");
        sixteenFive.setName("sixteenFive");
        sixteenSix.setName("sixteenSix");
        sixteenSeven.setName("sixteenSeven");
        sixteenEight.setName("sixteenEight");
        sixteenNine.setName("sixteenNine");
        sixteenTen.setName("sixteenTen");
        sixteenEleven.setName("sixteenEleven");
        sixteenTwelve.setName("sixteenTwelve");
        sixteenThirteen.setName("sixteenThirteen");
        sixteenFourteen.setName("sixteenFourteen");
        sixteenFifteen.setName("sixteenFifteen");
        sixteenSixteen.setName("sixteenSixteen");
        sixteenSeventeen.setName("sixteenSeventeen");
        sixteenEighteen.setName("sixteenEighteen");
        sixteenNineteen.setName("sixteenNineteen");
        seventeenZero.setName("seventeenZero");
        seventeenOne.setName("seventeenOne");
        seventeenTwo.setName("seventeenTwo");
        seventeenThree.setName("seventeenThree");
        seventeenFour.setName("seventeenFour");
        seventeenFive.setName("seventeenFive");
        seventeenSix.setName("seventeenSix");
        seventeenSeven.setName("seventeenSeven");
        seventeenEight.setName("seventeenEight");
        seventeenNine.setName("seventeenNine");
        seventeenTen.setName("seventeenTen");
        seventeenEleven.setName("seventeenEleven");
        seventeenTwelve.setName("seventeenTwelve");
        seventeenThirteen.setName("seventeenThirteen");
        seventeenFourteen.setName("seventeenFourteen");
        seventeenFifteen.setName("seventeenFifteen");
        seventeenSixteen.setName("seventeenSixteen");
        seventeenSeventeen.setName("seventeenSeventeen");
        seventeenEighteen.setName("seventeenEighteen");
        seventeenNineteen.setName("seventeenNineteen");
        eighteenZero.setName("eighteenZero");
        eighteenOne.setName("eighteenOne");
        eighteenTwo.setName("eighteenTwo");
        eighteenThree.setName("eighteenThree");
        eighteenFour.setName("eighteenFour");
        eighteenFive.setName("eighteenFive");
        eighteenSix.setName("eighteenSix");
        eighteenSeven.setName("eighteenSeven");
        eighteenEight.setName("eighteenEight");
        eighteenNine.setName("eighteenNine");
        eighteenTen.setName("eighteenTen");
        eighteenEleven.setName("eighteenEleven");
        eighteenTwelve.setName("eighteenTwelve");
        eighteenThirteen.setName("eighteenThirteen");
        eighteenFourteen.setName("eighteenFourteen");
        eighteenFifteen.setName("eighteenFifteen");
        eighteenSixteen.setName("eighteenSixteen");
        eighteenSeventeen.setName("eighteenSeventeen");
        eighteenEighteen.setName("eighteenEighteen");
        eighteenNineteen.setName("eighteenNineteen");
        nineteenZero.setName("nineteenZero");
        nineteenOne.setName("nineteenOne");
        nineteenTwo.setName("nineteenTwo");
        nineteenThree.setName("nineteenThree");
        nineteenFour.setName("nineteenFour");
        nineteenFive.setName("nineteenFive");
        nineteenSix.setName("nineteenSix");
        nineteenSeven.setName("nineteenSeven");
        nineteenEight.setName("nineteenEight");
        nineteenNine.setName("nineteenNine");
        nineteenTen.setName("nineteenTen");
        nineteenEleven.setName("nineteenEleven");
        nineteenTwelve.setName("nineteenTwelve");
        nineteenThirteen.setName("nineteenThirteen");
        nineteenFourteen.setName("nineteenFourteen");
        nineteenFifteen.setName("nineteenFifteen");
        nineteenSixteen.setName("nineteenSixteen");
        nineteenSeventeen.setName("nineteenSeventeen");
        nineteenEighteen.setName("nineteenEighteen");
        nineteenNineteen.setName("nineteenNineteen");
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

           
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel eightEight;
    private javax.swing.JLabel eightEighteen;
    private javax.swing.JLabel eightEleven;
    private javax.swing.JLabel eightFifteen;
    private javax.swing.JLabel eightFive;
    private javax.swing.JLabel eightFour;
    private javax.swing.JLabel eightFourteen;
    private javax.swing.JLabel eightNine;
    private javax.swing.JLabel eightNineteen;
    private javax.swing.JLabel eightOne;
    private javax.swing.JLabel eightSeven;
    private javax.swing.JLabel eightSeventeen;
    private javax.swing.JLabel eightSix;
    private javax.swing.JLabel eightSixteen;
    private javax.swing.JLabel eightTen;
    private javax.swing.JLabel eightThirteen;
    private javax.swing.JLabel eightThree;
    private javax.swing.JLabel eightTwelve;
    private javax.swing.JLabel eightTwo;
    private javax.swing.JLabel eightZero;
    private javax.swing.JLabel eighteenEight;
    private javax.swing.JLabel eighteenEighteen;
    private javax.swing.JLabel eighteenEleven;
    private javax.swing.JLabel eighteenFifteen;
    private javax.swing.JLabel eighteenFive;
    private javax.swing.JLabel eighteenFour;
    private javax.swing.JLabel eighteenFourteen;
    private javax.swing.JLabel eighteenNine;
    private javax.swing.JLabel eighteenNineteen;
    private javax.swing.JLabel eighteenOne;
    private javax.swing.JLabel eighteenSeven;
    private javax.swing.JLabel eighteenSeventeen;
    private javax.swing.JLabel eighteenSix;
    private javax.swing.JLabel eighteenSixteen;
    private javax.swing.JLabel eighteenTen;
    private javax.swing.JLabel eighteenThirteen;
    private javax.swing.JLabel eighteenThree;
    private javax.swing.JLabel eighteenTwelve;
    private javax.swing.JLabel eighteenTwo;
    private javax.swing.JLabel eighteenZero;
    private javax.swing.JLabel elevenEight;
    private javax.swing.JLabel elevenEighteen;
    private javax.swing.JLabel elevenEleven;
    private javax.swing.JLabel elevenFifteen;
    private javax.swing.JLabel elevenFive;
    private javax.swing.JLabel elevenFour;
    private javax.swing.JLabel elevenFourteen;
    private javax.swing.JLabel elevenNine;
    private javax.swing.JLabel elevenNineteen;
    private javax.swing.JLabel elevenOne;
    private javax.swing.JLabel elevenSeven;
    private javax.swing.JLabel elevenSeventeen;
    private javax.swing.JLabel elevenSix;
    private javax.swing.JLabel elevenSixteen;
    private javax.swing.JLabel elevenTen;
    private javax.swing.JLabel elevenThirteen;
    private javax.swing.JLabel elevenThree;
    private javax.swing.JLabel elevenTwelve;
    private javax.swing.JLabel elevenTwo;
    private javax.swing.JLabel elevenZero;
    private javax.swing.JLabel fifteenEight;
    private javax.swing.JLabel fifteenEighteen;
    private javax.swing.JLabel fifteenEleven;
    private javax.swing.JLabel fifteenFifteen;
    private javax.swing.JLabel fifteenFive;
    private javax.swing.JLabel fifteenFour;
    private javax.swing.JLabel fifteenFourteen;
    private javax.swing.JLabel fifteenNine;
    private javax.swing.JLabel fifteenNineteen;
    private javax.swing.JLabel fifteenOne;
    private javax.swing.JLabel fifteenSeven;
    private javax.swing.JLabel fifteenSeventeen;
    private javax.swing.JLabel fifteenSix;
    private javax.swing.JLabel fifteenSixteen;
    private javax.swing.JLabel fifteenTen;
    private javax.swing.JLabel fifteenThirteen;
    private javax.swing.JLabel fifteenThree;
    private javax.swing.JLabel fifteenTwelve;
    private javax.swing.JLabel fifteenTwo;
    private javax.swing.JLabel fifteenZero;
    private javax.swing.JLabel fiveEight;
    private javax.swing.JLabel fiveEighteen;
    private javax.swing.JLabel fiveEleven;
    private javax.swing.JLabel fiveFifteen;
    private javax.swing.JLabel fiveFive;
    private javax.swing.JLabel fiveFour;
    private javax.swing.JLabel fiveFourteen;
    private javax.swing.JLabel fiveNine;
    private javax.swing.JLabel fiveNineteen;
    private javax.swing.JLabel fiveOne;
    private javax.swing.JLabel fiveSeven;
    private javax.swing.JLabel fiveSeventeen;
    private javax.swing.JLabel fiveSix;
    private javax.swing.JLabel fiveSixteen;
    private javax.swing.JLabel fiveTen;
    private javax.swing.JLabel fiveThirteen;
    private javax.swing.JLabel fiveThree;
    private javax.swing.JLabel fiveTwelve;
    private javax.swing.JLabel fiveTwo;
    private javax.swing.JLabel fiveZero;
    private javax.swing.JLabel fourEight;
    private javax.swing.JLabel fourEighteen;
    private javax.swing.JLabel fourEleven;
    private javax.swing.JLabel fourFifteen;
    private javax.swing.JLabel fourFive;
    private javax.swing.JLabel fourFour;
    private javax.swing.JLabel fourFourteen;
    private javax.swing.JLabel fourNine;
    private javax.swing.JLabel fourNineteen;
    private javax.swing.JLabel fourOne;
    private javax.swing.JLabel fourSeven;
    private javax.swing.JLabel fourSeventeen;
    private javax.swing.JLabel fourSix;
    private javax.swing.JLabel fourSixteen;
    private javax.swing.JLabel fourTen;
    private javax.swing.JLabel fourThirteen;
    private javax.swing.JLabel fourThree;
    private javax.swing.JLabel fourTwelve;
    private javax.swing.JLabel fourTwo;
    private javax.swing.JLabel fourZero;
    private javax.swing.JLabel fourteenEight;
    private javax.swing.JLabel fourteenEighteen;
    private javax.swing.JLabel fourteenEleven;
    private javax.swing.JLabel fourteenFifteen;
    private javax.swing.JLabel fourteenFive;
    private javax.swing.JLabel fourteenFour;
    private javax.swing.JLabel fourteenFourteen;
    private javax.swing.JLabel fourteenNine;
    private javax.swing.JLabel fourteenNineteen;
    private javax.swing.JLabel fourteenOne;
    private javax.swing.JLabel fourteenSeven;
    private javax.swing.JLabel fourteenSeventeen;
    private javax.swing.JLabel fourteenSix;
    private javax.swing.JLabel fourteenSixteen;
    private javax.swing.JLabel fourteenTen;
    private javax.swing.JLabel fourteenThirteen;
    private javax.swing.JLabel fourteenThree;
    private javax.swing.JLabel fourteenTwelve;
    private javax.swing.JLabel fourteenTwo;
    private javax.swing.JLabel fourteenZero;
    private javax.swing.JLabel nineEight;
    private javax.swing.JLabel nineEighteen;
    private javax.swing.JLabel nineEleven;
    private javax.swing.JLabel nineFifteen;
    private javax.swing.JLabel nineFive;
    private javax.swing.JLabel nineFour;
    private javax.swing.JLabel nineFourteen;
    private javax.swing.JLabel nineNine;
    private javax.swing.JLabel nineNineteen;
    private javax.swing.JLabel nineOne;
    private javax.swing.JLabel nineSeven;
    private javax.swing.JLabel nineSeventeen;
    private javax.swing.JLabel nineSix;
    private javax.swing.JLabel nineSixteen;
    private javax.swing.JLabel nineTen;
    private javax.swing.JLabel nineThirteen;
    private javax.swing.JLabel nineThree;
    private javax.swing.JLabel nineTwelve;
    private javax.swing.JLabel nineTwo;
    private javax.swing.JLabel nineZero;
    private javax.swing.JLabel nineteenEight;
    private javax.swing.JLabel nineteenEighteen;
    private javax.swing.JLabel nineteenEleven;
    private javax.swing.JLabel nineteenFifteen;
    private javax.swing.JLabel nineteenFive;
    private javax.swing.JLabel nineteenFour;
    private javax.swing.JLabel nineteenFourteen;
    private javax.swing.JLabel nineteenNine;
    private javax.swing.JLabel nineteenNineteen;
    private javax.swing.JLabel nineteenOne;
    private javax.swing.JLabel nineteenSeven;
    private javax.swing.JLabel nineteenSeventeen;
    private javax.swing.JLabel nineteenSix;
    private javax.swing.JLabel nineteenSixteen;
    private javax.swing.JLabel nineteenTen;
    private javax.swing.JLabel nineteenThirteen;
    private javax.swing.JLabel nineteenThree;
    private javax.swing.JLabel nineteenTwelve;
    private javax.swing.JLabel nineteenTwo;
    private javax.swing.JLabel nineteenZero;
    private javax.swing.JLabel oneEight;
    private javax.swing.JLabel oneEighteen;
    private javax.swing.JLabel oneEleven;
    private javax.swing.JLabel oneFifteen;
    private javax.swing.JLabel oneFive;
    private javax.swing.JLabel oneFour;
    private javax.swing.JLabel oneFourteen;
    private javax.swing.JLabel oneNine;
    private javax.swing.JLabel oneNineteen;
    private javax.swing.JLabel oneOne;
    private javax.swing.JLabel oneSeven;
    private javax.swing.JLabel oneSeventeen;
    private javax.swing.JLabel oneSix;
    private javax.swing.JLabel oneSixteen;
    private javax.swing.JLabel oneTen;
    private javax.swing.JLabel oneThirteen;
    private javax.swing.JLabel oneThree;
    private javax.swing.JLabel oneTwelve;
    private javax.swing.JLabel oneTwo;
    private javax.swing.JLabel oneZero;
    private javax.swing.JButton resetjButton1;
    private javax.swing.JLabel sevenEight;
    private javax.swing.JLabel sevenEighteen;
    private javax.swing.JLabel sevenEleven;
    private javax.swing.JLabel sevenFifteen;
    private javax.swing.JLabel sevenFive;
    private javax.swing.JLabel sevenFour;
    private javax.swing.JLabel sevenFourteen;
    private javax.swing.JLabel sevenNine;
    private javax.swing.JLabel sevenNineteen;
    private javax.swing.JLabel sevenOne;
    private javax.swing.JLabel sevenSeven;
    private javax.swing.JLabel sevenSeventeen;
    private javax.swing.JLabel sevenSix;
    private javax.swing.JLabel sevenSixteen;
    private javax.swing.JLabel sevenTen;
    private javax.swing.JLabel sevenThirteen;
    private javax.swing.JLabel sevenThree;
    private javax.swing.JLabel sevenTwelve;
    private javax.swing.JLabel sevenTwo;
    private javax.swing.JLabel sevenZero;
    private javax.swing.JLabel seventeenEight;
    private javax.swing.JLabel seventeenEighteen;
    private javax.swing.JLabel seventeenEleven;
    private javax.swing.JLabel seventeenFifteen;
    private javax.swing.JLabel seventeenFive;
    private javax.swing.JLabel seventeenFour;
    private javax.swing.JLabel seventeenFourteen;
    private javax.swing.JLabel seventeenNine;
    private javax.swing.JLabel seventeenNineteen;
    private javax.swing.JLabel seventeenOne;
    private javax.swing.JLabel seventeenSeven;
    private javax.swing.JLabel seventeenSeventeen;
    private javax.swing.JLabel seventeenSix;
    private javax.swing.JLabel seventeenSixteen;
    private javax.swing.JLabel seventeenTen;
    private javax.swing.JLabel seventeenThirteen;
    private javax.swing.JLabel seventeenThree;
    private javax.swing.JLabel seventeenTwelve;
    private javax.swing.JLabel seventeenTwo;
    private javax.swing.JLabel seventeenZero;
    private javax.swing.JLabel sixEight;
    private javax.swing.JLabel sixEighteen;
    private javax.swing.JLabel sixEleven;
    private javax.swing.JLabel sixFifteen;
    private javax.swing.JLabel sixFive;
    private javax.swing.JLabel sixFour;
    private javax.swing.JLabel sixFourteen;
    private javax.swing.JLabel sixNine;
    private javax.swing.JLabel sixNineteen;
    private javax.swing.JLabel sixOne;
    private javax.swing.JLabel sixSeven;
    private javax.swing.JLabel sixSeventeen;
    private javax.swing.JLabel sixSix;
    private javax.swing.JLabel sixSixteen;
    private javax.swing.JLabel sixTen;
    private javax.swing.JLabel sixThirteen;
    private javax.swing.JLabel sixThree;
    private javax.swing.JLabel sixTwelve;
    private javax.swing.JLabel sixTwo;
    private javax.swing.JLabel sixZero;
    private javax.swing.JLabel sixteenEight;
    private javax.swing.JLabel sixteenEighteen;
    private javax.swing.JLabel sixteenEleven;
    private javax.swing.JLabel sixteenFifteen;
    private javax.swing.JLabel sixteenFive;
    private javax.swing.JLabel sixteenFour;
    private javax.swing.JLabel sixteenFourteen;
    private javax.swing.JLabel sixteenNine;
    private javax.swing.JLabel sixteenNineteen;
    private javax.swing.JLabel sixteenOne;
    private javax.swing.JLabel sixteenSeven;
    private javax.swing.JLabel sixteenSeventeen;
    private javax.swing.JLabel sixteenSix;
    private javax.swing.JLabel sixteenSixteen;
    private javax.swing.JLabel sixteenTen;
    private javax.swing.JLabel sixteenThirteen;
    private javax.swing.JLabel sixteenThree;
    private javax.swing.JLabel sixteenTwelve;
    private javax.swing.JLabel sixteenTwo;
    private javax.swing.JLabel sixteenZero;
    private javax.swing.JButton startSimulationjButton1;
    private javax.swing.JLabel tenEight;
    private javax.swing.JLabel tenEighteen;
    private javax.swing.JLabel tenEleven;
    private javax.swing.JLabel tenFifteen;
    private javax.swing.JLabel tenFive;
    private javax.swing.JLabel tenFour;
    private javax.swing.JLabel tenFourteen;
    private javax.swing.JLabel tenNine;
    private javax.swing.JLabel tenNineteen;
    private javax.swing.JLabel tenOne;
    private javax.swing.JLabel tenSeven;
    private javax.swing.JLabel tenSeventeen;
    private javax.swing.JLabel tenSix;
    private javax.swing.JLabel tenSixteen;
    private javax.swing.JLabel tenTen;
    private javax.swing.JLabel tenThirteen;
    private javax.swing.JLabel tenThree;
    private javax.swing.JLabel tenTwelve;
    private javax.swing.JLabel tenTwo;
    private javax.swing.JLabel tenZero;
    private javax.swing.JLabel thirteenEight;
    private javax.swing.JLabel thirteenEighteen;
    private javax.swing.JLabel thirteenEleven;
    private javax.swing.JLabel thirteenFifteen;
    private javax.swing.JLabel thirteenFive;
    private javax.swing.JLabel thirteenFour;
    private javax.swing.JLabel thirteenFourteen;
    private javax.swing.JLabel thirteenNine;
    private javax.swing.JLabel thirteenNineteen;
    private javax.swing.JLabel thirteenOne;
    private javax.swing.JLabel thirteenSeven;
    private javax.swing.JLabel thirteenSeventeen;
    private javax.swing.JLabel thirteenSix;
    private javax.swing.JLabel thirteenSixteen;
    private javax.swing.JLabel thirteenTen;
    private javax.swing.JLabel thirteenThirteen;
    private javax.swing.JLabel thirteenThree;
    private javax.swing.JLabel thirteenTwelve;
    private javax.swing.JLabel thirteenTwo;
    private javax.swing.JLabel thirteenZero;
    private javax.swing.JLabel threeEight;
    private javax.swing.JLabel threeEighteen;
    private javax.swing.JLabel threeEleven;
    private javax.swing.JLabel threeFifteen;
    private javax.swing.JLabel threeFive;
    private javax.swing.JLabel threeFour;
    private javax.swing.JLabel threeFourteen;
    private javax.swing.JLabel threeNine;
    private javax.swing.JLabel threeNineteen;
    private javax.swing.JLabel threeOne;
    private javax.swing.JLabel threeSeven;
    private javax.swing.JLabel threeSeventeen;
    private javax.swing.JLabel threeSix;
    private javax.swing.JLabel threeSixteen;
    private javax.swing.JLabel threeTen;
    private javax.swing.JLabel threeThirteen;
    private javax.swing.JLabel threeThree;
    private javax.swing.JLabel threeTwelve;
    private javax.swing.JLabel threeTwo;
    private javax.swing.JLabel threeZero;
    private javax.swing.JLabel twelveEight;
    private javax.swing.JLabel twelveEighteen;
    private javax.swing.JLabel twelveEleven;
    private javax.swing.JLabel twelveFifteen;
    private javax.swing.JLabel twelveFive;
    private javax.swing.JLabel twelveFour;
    private javax.swing.JLabel twelveFourteen;
    private javax.swing.JLabel twelveNine;
    private javax.swing.JLabel twelveNineteen;
    private javax.swing.JLabel twelveOne;
    private javax.swing.JLabel twelveSeven;
    private javax.swing.JLabel twelveSeventeen;
    private javax.swing.JLabel twelveSix;
    private javax.swing.JLabel twelveSixteen;
    private javax.swing.JLabel twelveTen;
    private javax.swing.JLabel twelveThirteen;
    private javax.swing.JLabel twelveThree;
    private javax.swing.JLabel twelveTwelve;
    private javax.swing.JLabel twelveTwo;
    private javax.swing.JLabel twelveZero;
    private javax.swing.JLabel twoEight;
    private javax.swing.JLabel twoEighteen;
    private javax.swing.JLabel twoEleven;
    private javax.swing.JLabel twoFifteen;
    private javax.swing.JLabel twoFive;
    private javax.swing.JLabel twoFour;
    private javax.swing.JLabel twoFourteen;
    private javax.swing.JLabel twoNine;
    private javax.swing.JLabel twoNineteen;
    private javax.swing.JLabel twoOne;
    private javax.swing.JLabel twoSeven;
    private javax.swing.JLabel twoSeventeen;
    private javax.swing.JLabel twoSix;
    private javax.swing.JLabel twoSixteen;
    private javax.swing.JLabel twoTen;
    private javax.swing.JLabel twoThirteen;
    private javax.swing.JLabel twoThree;
    private javax.swing.JLabel twoTwelve;
    private javax.swing.JLabel twoTwo;
    private javax.swing.JLabel twoZero;
    private javax.swing.JLabel zeroEight;
    private javax.swing.JLabel zeroEighteen;
    private javax.swing.JLabel zeroEleven;
    private javax.swing.JLabel zeroFifteen;
    private javax.swing.JLabel zeroFive;
    private javax.swing.JLabel zeroFour;
    private javax.swing.JLabel zeroFourteen;
    private javax.swing.JLabel zeroNine;
    private javax.swing.JLabel zeroNineteen;
    private javax.swing.JLabel zeroOne;
    private javax.swing.JLabel zeroSeven;
    private javax.swing.JLabel zeroSeventeen;
    private javax.swing.JLabel zeroSix;
    private javax.swing.JLabel zeroSixteen;
    private javax.swing.JLabel zeroTen;
    private javax.swing.JLabel zeroThirteen;
    private javax.swing.JLabel zeroThree;
    private javax.swing.JLabel zeroTwelve;
    private javax.swing.JLabel zeroTwo;
    private javax.swing.JLabel zeroZero;
    // End of variables declaration//GEN-END:variables
}
