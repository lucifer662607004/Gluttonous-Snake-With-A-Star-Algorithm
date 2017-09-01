/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakefighting;
import java.util.ArrayList;
import java.awt.Dimension;
/**
 *
 * @author Virtual
 */
public class AStarNavigation {
    ArrayList<StarNode> open=new ArrayList<>();
    ArrayList<StarNode> close=new ArrayList<>();
    ArrayList<StarNode> obstacles=new ArrayList<>();
    ArrayList<StarNode> path=new ArrayList<>();
    ArrayList<Dimension> snakeheads=new ArrayList<>();
    StarNode coordinate[][];
    StarNode start;
    StarNode destination;
    
    class StarNode{
        Dimension location;
        StarNode father;
        int X;
        int Y;
        int F;//sum of G and H
        int G;//the distance from start node to this node
        int H;//the estimate distance from this node to destination
        float P;//posibility of another snakes blocking this node
        public StarNode(int x, int y){
            location=new Dimension(x,y);
            X=x;
            Y=y;
            P=0;
        }
        
        public void SetFather(StarNode f){
            father=f;
        }        
        public void SetF(){
            F=G+H;
        }
        public void SetH(int h){
            H=h;
        }
        public void SetG(int g){
            G=g;
        }
        public void SetP(float p){
            P=p;
        }
        
        @Override
        public String toString(){
            return "("+X+", "+Y+")";
        }
        public Dimension GetDimension(){
            return location;
        }
    }
    //add all obstacle coordinate to obstacle arraylist
    public void LoadMap(Snakecanvas sc,Snake s, int endx, int endy){
        int startx=s.snakex;
        int starty=s.snakey;
        coordinate=new StarNode[Snakecanvas.x/Node.length][Snakecanvas.x/Node.width];
        for (int n=0;n<Snakecanvas.x/Node.length;n++){
            for (int m=0;m<Snakecanvas.x/Node.width;m++){
                coordinate[n][m]=new StarNode(n,m);
            }
        }
        start=coordinate[startx][starty];
        destination=coordinate[endx][endy];
        ArrayList<Dimension> al=new ArrayList<>();
        al.addAll(sc.obstacles);
        al.addAll(sc.snakelocations);
        //identify all snakeheads for posibility calculation
        if (sc.snakelocations.size()>1){
            snakeheads.addAll(sc.snakelocations);
            snakeheads.remove(s.gethead());
        }
        
        //Prediction the next move of other snake        
        al.addAll(sc.potential);
        Dimension t=s.Nextmove(s.snakedirection);
        if(sc.potential.contains(t)){
            al.remove(t);//remove self prediction
        }        
        //Advanced prediction of enemy snake tail move
        //Remove the safe obstacles
        ArrayList<Dimension> safeobstacles=Prediction.EvaluateObstacles(sc, s);
        al.removeAll(safeobstacles);
        
        al.stream().forEach((Dimension d) -> {
            obstacles.add(coordinate[d.width][d.height]);
        });
        obstacles.remove(start);
    }
    //check when the target is reached if the snake is safe to escape
    public boolean Checktarget(Snakecanvas SC,Snake S){
        coordinate=new StarNode[Snakecanvas.x/Node.length][Snakecanvas.x/Node.width];
        for (int n=0;n<Snakecanvas.x/Node.length;n++){
            for (int m=0;m<Snakecanvas.x/Node.width;m++){
                coordinate[n][m]=new StarNode(n,m);
            }
        }
        start=coordinate[SC.food.xy.width][SC.food.xy.height];
        destination=coordinate[S.gettail().width][S.gettail().height];
        ArrayList<Dimension> al=new ArrayList<>();
        al.addAll(SC.obstacles);
        al.addAll(SC.snakelocations);
        al.remove(S.gettail());
        al.stream().forEach((Dimension d) -> {
            obstacles.add(coordinate[d.width][d.height]);
        });        
        ArrayList<Dimension> escapepath=new ArrayList<>();
        escapepath=Initial();
        if (escapepath.isEmpty()){
            //if the trap is identified, return false
            return false;
        }        
        else{
            //if the target is safe, return true
            return true;
        }
    }
    //navigate with a star method
    public ArrayList<Dimension> Initial(){        
        start.SetG(0);
        start.SetH(Math.abs(start.X-destination.X)*10+Math.abs(start.Y-destination.Y)*10);
        start.SetF();
        open.add(start);        
        Boolean b=Navigation(start);
        ArrayList<Dimension> ds=new ArrayList<>();
        if (b){
            RevealPath(destination);                
            for (StarNode sn:path){
                ds.add(sn.GetDimension());            
            }
        }
        return ds;        
    }
    private Boolean Navigation(StarNode sn){       
        //seek the left coordinate
        if (sn.Y!=0){
        StarNode up=coordinate[sn.X][sn.Y-1];        
        if (!obstacles.contains(up)&&!close.contains(up)){
            //if the target is not in open list, add it to open list
            if (!open.contains(up)){
                up.SetFather(sn);
                up.SetG(sn.G+10);
                up.SetH(Math.abs(up.X-destination.X)*10+Math.abs(up.Y-destination.Y)*10);
                up.SetF();
                
                //posibility estimation formula: (1/3)^distance
                if (!snakeheads.isEmpty()){
                    float p=0;
                    for(Dimension d:snakeheads){
                        p=(float)Math.pow(0.33,(Math.abs(up.X-d.width)+Math.abs(up.Y-d.height)))+p;
                    }
                    up.SetP(p);
                }
                
                open.add(up);
            }
            //if the target already exists in open list, check if this path is more efficient. if so refresh the path
            else{
                if (sn.G+10<up.G){
                    up.SetFather(sn);
                    up.SetG(sn.G+10);
                    up.SetF();
                }
            }
        }
        }
        //seek the right coordinate
        if (sn.Y!=19){
        StarNode down=coordinate[sn.X][sn.Y+1];
        if (!obstacles.contains(down)&&!close.contains(down)){
            if (!open.contains(down)){
                down.SetFather(sn);
                down.SetG(sn.G+10);
                down.SetH(Math.abs(down.X-destination.X)*10+Math.abs(down.Y-destination.Y)*10);
                down.SetF();
                
                //posibility estimation formula: (1/3)^distance
                if (!snakeheads.isEmpty()){
                    float p=0;
                    for(Dimension d:snakeheads){
                        p=(float)Math.pow(0.33,(Math.abs(down.X-d.width)+Math.abs(down.Y-d.height)))+p;
                    }
                    down.SetP(p);
                }
                
                open.add(down);
            }
            else{
                if (sn.G+10<down.G){
                    down.SetFather(sn);
                    down.SetG(sn.G+10);
                    down.SetF();
                }
            }
        }
        }
        //seek the right coordinate
        if (sn.X!=19){
        StarNode right=coordinate[sn.X+1][sn.Y];
        if (!obstacles.contains(right)&&!close.contains(right)){
            if (!open.contains(right)){
                right.SetFather(sn);
                right.SetG(sn.G+10);
                right.SetH(Math.abs(right.X-destination.X)*10+Math.abs(right.Y-destination.Y)*10);
                right.SetF();
                
                //posibility estimation formula: (1/3)^distance
                if (!snakeheads.isEmpty()){
                    float p=0;
                    for(Dimension d:snakeheads){
                        p=(float)Math.pow(0.33,(Math.abs(right.X-d.width)+Math.abs(right.Y-d.height)))+p;
                    }
                    right.SetP(p);
                }
                
                open.add(right);
            }
            else{
                if (sn.G+10<right.G){
                    right.SetFather(sn);
                    right.SetG(sn.G+10);
                    right.SetF();
                }
            }
        }
        }
        //seek the left coordinate
        if (sn.X!=0){
        StarNode left=coordinate[sn.X-1][sn.Y];
        if (!obstacles.contains(left)&&!close.contains(left)){
            if (!open.contains(left)){
                left.SetFather(sn);
                left.SetG(sn.G+10);
                left.SetH(Math.abs(left.X-destination.X)*10+Math.abs(left.Y-destination.Y)*10);
                left.SetF();
                
                //posibility estimation formula: (1/3)^distance
                if (!snakeheads.isEmpty()){
                    float p=0;
                    for(Dimension d:snakeheads){
                        p=(float)Math.pow(0.33,(Math.abs(left.X-d.width)+Math.abs(left.Y-d.height)))+p;
                    }
                    left.SetP(p);
                }
                
                open.add(left);
            }
            else{
                if (sn.G+10<left.G){
                    left.SetFather(sn);
                    left.SetG(sn.G+10);
                    left.SetF();
                }
            }
        }
        }
        
        open.remove(sn);
        close.add(sn);
        //get the lowest F value
        if (open.isEmpty()){
            return false;
        }
        else{
            StarNode temp=open.get(0);
            for(int n=1;n<open.size();n++){
                if ((open.get(n).F+open.get(n).P)<=(temp.F+temp.P)){
                    temp=open.get(n);
                }
            }
            if (temp!=destination){
                Boolean b=Navigation(temp);
                return b;
            }
            else{
                open.remove(temp);
                close.add(temp);
                return true;
            }
        }       
    }
    private void RevealPath(StarNode sn){
        path.add(0,sn);
        if (sn!=start){
            RevealPath(sn.father);
        }
        
    }
    //for test purpose code
    /*
    public static void main(String args[]){
        AStarNavigation asn=new AStarNavigation();
        //asn.LoadMap();
        ArrayList<StarNode> al=asn.Initial();
        for(int i=0;i<al.size();i++){
            System.out.println(al.get(i));
        }
    }
    */
    
}
