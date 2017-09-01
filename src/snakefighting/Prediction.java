/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakefighting;
import java.awt.Dimension;
import java.util.ArrayList;
/**
 *
 * @author Virtual
 */
public class Prediction {
    //evaluate all obstacle nodes and return the safe obstacles that doesn't need to be considered.
    public static ArrayList<Dimension> EvaluateObstacles(Snakecanvas SC, Snake theSnake){
        ArrayList<ObstacleNode> obstaclenode=new ArrayList<>();
        ArrayList<Dimension> safeobstacle=new ArrayList<>();
        for (int n=0;n<SC.snakelist.size();n++){
            Snake s=SC.snakelist.get(n);
            //System.out.println(n);
            if (SC.snakeeatfood!=n){
            for(int i=0;i<s.snakelength;i++){
                obstaclenode.add(new ObstacleNode(s.snakebody.get(i).xy,s.snakelength-i));//head a value=snakelength, tail a value=1;
                //test purpose
                /*
                if (n==0&&i==s.snakelength-1) {
                    System.out.println(s.snakebody.get(i).xy);
                    System.out.println(SC.snakelist.get(0).gettail());
                }
                        */
                //if(s.snakebody.get(i).xy==SC.snakelist.get(0).gettail()) System.out.println(true);
            }
            obstaclenode.add(new ObstacleNode(s.Nextmove(s.snakedirection),s.snakelength+1));//potential obstacle prediction
            }
            else{
            for(int i=0;i<s.snakelength;i++){
                obstaclenode.add(new ObstacleNode(s.snakebody.get(i).xy,s.snakelength-i+1));//head a value=snakelength, tail a value=1;                
            }
            obstaclenode.add(new ObstacleNode(s.Nextmove(s.snakedirection),s.snakelength+2));//potential obstacle prediction
            }            
        }
        for (ObstacleNode on:obstaclenode){
            int a=Math.abs(theSnake.snakex-on.x)+Math.abs(theSnake.snakey-on.y);            
            //the exist time must be less or equal than reach time so that the obstacle is marked as safe.
            if (a>=on.assessment){
                safeobstacle.add(on.location);
            }            
        }
        //System.out.println(safeobstacle.contains(SC.snakelist.get(0).gettail()));
        return safeobstacle;
    }
}
class ObstacleNode{
    int x;
    int y;
    int assessment;//it stands for how long the obstacle will exist in this place
    Dimension location;
    public ObstacleNode(Dimension D,int A){
        x=D.width;
        y=D.height;
        assessment=A;
        location=D;
    }
}
