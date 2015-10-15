/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package droneproject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.JLabel;


/**
 *
 * @author nirav gupta
 */
public class Test {
    

    public void generateCodinates() {
   
        DroneMain dm = new DroneMain();
        
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
        
        for (Node n: dm.NodeList){
            System.out.println(n.getXcordinate()+n.getYcordinate());
        }
    }   
    
  
//    public class checkAdjacentAnamoly extends TimerTask{
//        
//        @Override
//        public void run() {
////            deleteAnomalies();
////            dm.buildAnomalies(fullNodeList);
////            //System.out.println("in run");
////            for (Node node : fullNodeList) {
////                //System.out.println("node:" + node.getXcordinate() + node.getYcordinate() + node.isIsAnamoly());
////                if (node.isIsAnamoly()) {
////                    //System.out.println("Inside if *****");
////                    StringBuffer sb = new StringBuffer();
////                    sb.append(node.getXcordinate());
////                    sb.append(node.getYcordinate());
////                    String s = sb.toString();
////                    //System.out.println("s" + s);
////                    JLabel j = (JLabel) getComponentByName(s);
////                    j.setForeground(Color.BLUE);
////                    //deleteAnomalies();
////                }
////            }
//        deletePreviousDronePathNode();
//        
//        Node nextNode = null;
//         if(airDrone.getNextNode().getXcordinate()==null && airDrone.getNextNode().getYcordinate()==null){
//            nextNode = airDrone.getSource();
//            airDrone.setNextNode(nextNode);
//         }else{
//             nextNode = airDrone.getNextNode();
//         }
//        
//         //create adjacent node list
//         ArrayList<Node> nextNodeAdjacentList = createAdjacentNodeList(nextNode);
//        
//         //calculate which  one of the nodes to be taken 
//        Node x= calculateConfidentNode(nextNodeAdjacentList,nextNode);
//        
//        //feed all adjacent nodes of next node in air drone for reference
//        airDrone.setAdjacentNodes(nextNodeAdjacentList);
//
//                            //System.out.println("********ADJACENT ANOMALY ******");
//            //System.out.println("#########" + sourceAdjNode.getXcordinate() + sourceAdjNode.getYcordinate());
//            //All calculations of taking the next node to be done here
//            //if(!(airDrone.getNextNode().getXcordinate().equalsIgnoreCase(airDrone.getDestination().getXcordinate()) && airDrone.getNextNode().getYcordinate().equalsIgnoreCase(airDrone.getDestination().getYcordinate()) )){
//                            String a = x.getXcordinate();
//                            String b = x.getYcordinate();
//                            b = b.substring(0, 1).toUpperCase() + b.substring(1);
//                            StringBuffer sb = new StringBuffer();
//                            sb.append(a);
//                            sb.append(b);
//                            String s = sb.toString();
//                            //System.out.println("s" + s);
//                            JLabel j = (JLabel) getComponentByName(s);
//                            j.setForeground(Color.RED);
//                            //add previous node to arraylist to turn it off grom red
//                            dronePreviousNodeJLabelList.add(j);
//                           airDrone.setNextNode(x);
//            //break;
//            //}
//
//
//            
//           
////            String a= dm.getStringForInt(i);
////            String b=dm.getStringForInt(k);
////            b = b.substring(0, 1).toUpperCase() + b.substring(1);
////            StringBuffer sb = new StringBuffer();
////                    sb.append(a);
////                    sb.append(b);
////                    String s = sb.toString();
////                    System.out.println("s" + s);
////                    JLabel j = (JLabel) getComponentByName(s);
////                    j.setForeground(Color.RED);
////                    add previous node to arraylist to turn it off grom red
////                    dronePreviousNodeJLabelList.add(j);
//          
//        }
//    }
    
    
}
