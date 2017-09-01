/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakefighting;
import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
/**
 *
 * @author Virtual
 */
public class SnakeFighting extends JFrame implements ActionListener,Runnable{
    Snakecanvas sc;
    JButton start;
    ImageFilter cropfilter,cropfilter3;
    JLayeredPane layeredPane;
    Image background,background2,blur_background,dim_background;
    JLabel jl,jl2,title,scoreboard,sb_bg,blur_bg,frame;
    JComboBox ais;
    int location;
    boolean animation=true;
    int players;
    int x=800,y=600;
    int x2=680,y2=500;
    public SnakeFighting(){
        layeredPane=new JLayeredPane(); 
        location=0;
        players=3;
        //backgroud
        background=new ImageIcon(getClass().getResource("/snakefighting/bg.jpg")).getImage();
        cropfilter=new CropImageFilter(0,0,x,y);
        ImageIcon ii= new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(background.getSource(),cropfilter))); 
        jl=new JLabel();
        jl.setIcon(ii);
        jl.setBounds(0, 0, x, y);
        jl.setDoubleBuffered(true);
        layeredPane.add(jl,JLayeredPane.DEFAULT_LAYER); 
        
        //background2
        background2=new ImageIcon(getClass().getResource("/snakefighting/theme.gif")).getImage();
        ImageFilter cropfilter2=new CropImageFilter(0,0,x2,y2);
        ImageIcon iii= new ImageIcon(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(background2.getSource(),cropfilter)));
        jl2=new JLabel();
        jl2.setIcon(iii);
        jl2.setBounds(0, 0, x2, y2);       
        
        //start button                
        start=new JButton("Start");
        start.setFont(new Font("Lucida Handwriting", 1, 24));
        start.addActionListener(this);
        start.setActionCommand("start");
        start.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);        
        start.setBounds(350, 410, 100, 40);
        start.setIgnoreRepaint(true);
        layeredPane.add(start,JLayeredPane.PALETTE_LAYER); 
        start.requestFocus();
        
        //combobox choosing ai (unchecked or unsafe operations)
        String[] list={"0 AI","1 AI","2 AIs","3 AIs"};
        ais=new JComboBox(list);
        ais.setSelectedIndex(3);
        ais.setFont(new Font("Lucida Handwriting", 1, 18));
        ais.addActionListener(this);
        ais.setIgnoreRepaint(true);
        ais.setBounds(350,460,100,60);
        layeredPane.add(ais,JLayeredPane.PALETTE_LAYER);
        
        //blur area
        blur_bg=new JLabel();
        blur_bg.setDoubleBuffered(true);
        blur_bg.setBounds(250,380,300,160);
        blur_background=new ImageIcon(getClass().getResource("/snakefighting/bg_blur.jpg")).getImage();
        Image blur_background2=Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(blur_background.getSource(),cropfilter));
        Rectangle r=blur_bg.getBounds();
        cropfilter3=new CropImageFilter(r.x,r.y,r.width,r.height);
        ImageIcon blur_bg_final=new ImageIcon(Toolkit.getDefaultToolkit().
                createImage(new FilteredImageSource(blur_background2.getSource(),cropfilter3)));
        blur_bg.setIcon(blur_bg_final);
        layeredPane.add(blur_bg,new Integer(50));
        
        //blur frame
        frame=new JLabel();
        frame.setIgnoreRepaint(true);
        frame.setIcon(new ImageIcon(getClass().getResource("/snakefighting/frame.png")));
        frame.setBounds(blur_bg.getBounds());
        layeredPane.add(frame,new Integer(75));
        
        //title
        title=new JLabel();
        title.setIcon(new ImageIcon(getClass().getResource("/snakefighting/Title.gif")));
        title.setBounds(200, 50, 400, 250);
        title.setIgnoreRepaint(true);
        layeredPane.add(title,JLayeredPane.PALETTE_LAYER);
        
        //scoreboard 
        String scoreinfo="<html><p><font size=+2><b>Scoreboard</b></font></p><p> </p><p> </p>"
                + "<p><font size=+1>Your score: 0</font></p></html>";                
        scoreboard=new JLabel(scoreinfo);
        scoreboard.setDoubleBuffered(true);
        scoreboard.setFocusable(false);
        scoreboard.setOpaque(false);
        scoreboard.setVerticalAlignment(SwingConstants.TOP);
        scoreboard.setBounds(440, 20, 220, 400);
        sb_bg=new JLabel();
        sb_bg.setBounds(scoreboard.getBounds());
        dim_background=new ImageIcon(getClass().getResource("/snakefighting/theme3.gif")).getImage();
        CropImageFilter dim_cropfilter=new CropImageFilter(sb_bg.getBounds().
                x,sb_bg.getBounds().y,sb_bg.getBounds().width,sb_bg.getBounds().height);
        Image im=Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(dim_background.getSource(),dim_cropfilter));
        sb_bg.setIcon(new ImageIcon(im));
        
        //main frame
        this.setLayeredPane(layeredPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(x,y);
        setLocationByPlatform(rootPaneCheckingEnabled);
        setLayout(null);  
        this.setLocationRelativeTo(null);
        
    }
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand()=="start"){            
            animation=false;               
            sc=new Snakecanvas(players,scoreboard);
            sc.setBounds(20, 20, 400, 400);                
            layeredPane.remove(start);
            layeredPane.remove(jl);
            layeredPane.remove(title);
            layeredPane.remove(frame);
            layeredPane.remove(blur_bg);
            layeredPane.add(sc,JLayeredPane.MODAL_LAYER);
            layeredPane.add(scoreboard,JLayeredPane.MODAL_LAYER);
            layeredPane.add(sb_bg,JLayeredPane.PALETTE_LAYER);
            layeredPane.add(jl2,JLayeredPane.DEFAULT_LAYER);
            this.setSize(x2, y2);//(*,420) is the lower edge
            repaint();                
            sc.requestFocus();
            sc.startgame();
        }
        else if(e.getSource()==ais){
            players=ais.getSelectedIndex();
        }
    }
    public void run(){
        while(animation){
            if (location<=880){
                cropfilter=new CropImageFilter(location,0,800,600);
                location++;
            }
            else if(location>880&&location<=1330){
                cropfilter=new CropImageFilter(880,location-880,800,600);
            }
            else if(location>1330&&location<=2210){
                cropfilter=new CropImageFilter(880-(location-1330),450,800,600);
            }
            else if(location>2210&&location<2660){
                cropfilter=new CropImageFilter(0,450-(location-2210),800,600);
            }
            else{
                location=0;
                cropfilter=new CropImageFilter(location,0,800,600);
            }
            location++;
            ImageIcon ii= new ImageIcon(Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(background.getSource(),cropfilter)));
            jl.setIcon(ii);
            Image temp= Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(blur_background.getSource(),cropfilter));
            ImageIcon iii= new ImageIcon(Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(temp.getSource(),cropfilter3)));
            blur_bg.setIcon(iii);
            try{
                Thread.sleep(50);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SnakeFighting sf=new SnakeFighting();
        sf.setVisible(true);
        sf.run();
    }    
}

class Snakecanvas extends Canvas implements KeyListener{   
    ArrayList<Snake> snakelist=new ArrayList<>();
    Snake mysnake;
    int snakenum;//snake num does include self snake
    int chosennum;
    String status;
    boolean controllable=true;
    static final int x=400;
    static final int y=400;
    BufferedImage offsetimage;
    ArrayList<Dimension> obstacles=new ArrayList<>();
    ArrayList<Dimension> snakelocations=new ArrayList<>();
    ArrayList<Dimension> border=new ArrayList<>();
    ArrayList<Dimension> potential=new ArrayList<>();
    Food food;
    int snakeeatfood=-1;
    Thread gamerun;
    JLabel scorelabel;
    int globalspeed=260;
    Image im;
    public Snakecanvas(int n, JLabel scoreboard){        
        super();       
        setSize(x,y);
        chosennum=n;
        snakenum=n;
        //score
        scorelabel=scoreboard;
        //game area background
        Image dim_background=new ImageIcon(getClass().getResource("/snakefighting/theme3.gif")).getImage();
        CropImageFilter dim_cropfilter=new CropImageFilter(getBounds().x,getBounds().y,getBounds().height,getBounds().height);
        im=Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(dim_background.getSource(),dim_cropfilter));
        //set up border
        for(int i=0;i<=19;i++){
            border.add(new Dimension(i,-1));
            border.add(new Dimension(i,x/20));
            border.add(new Dimension(-1,i));
            border.add(new Dimension(x/20,i));
        }
        border.add(new Dimension(-1,-1));
        border.add(new Dimension(x/20,-1));
        border.add(new Dimension(-1,x/20));
        border.add(new Dimension(x/20,x/20));
        //create my snake
        mysnake=new Snake(10,1,5,"r",Color.black);
        
        //add all snakes to snake list
        //add the number of snakes: need to modify the scoreboard initial and the scoreboard update
        snakelist.add(mysnake);
        if (snakenum>0){
            Snake snake1=new Snake(10,5,5,"l",new Color(170,40,98));
            snake1.AIControl=true;
            snakelist.add(snake1);            
        }
        if (snakenum>1){
            Snake snake2=new Snake(10,9,5,"r",new Color(129,40,170));
            snake2.AIControl=true;
            snakelist.add(snake2);
        }
        if (snakenum>2){
            Snake snake3=new Snake(10,13,5,"l",new Color(40,55,170));
            snake3.AIControl=true;
            snakelist.add(snake3);
        }
        
        //control my snake
        addKeyListener(this);
        setFocusable(true);
        
        //identify the obstacles in the map for mysnake;
        Getobstacle();
                
        //food
        dropfood();
        
        //buildup scoreboard
        String scoreinfo="<html><p><font size=+2 face=\"Lucida Handwriting\"><b>Scoreboard</b></font></p><p> </p><p> </p>"
                + "<p><font size=+1 face=\"Lucida Handwriting\">Your score: 0</font></p></html>";       
        for(int i=1;i<=snakenum;i++){
            Snake s=snakelist.get(i);
            scoreinfo=scoreinfo.substring(0, scoreinfo.indexOf("</html>"))+
                    "<p> </p><p> </p><p><font size=+1 color=rgb("
                    +s.color.getRed()+","+s.color.getGreen()+","+s.color.getBlue()
                    +") face=\"Lucida Handwriting\">Player"+i+" score: 0</font></p></html>";
        }
        scorelabel.setText(scoreinfo);
    }
    //obstacle does not include snakehead;
    //desinged for analysising the situation for AI;
    private void Getobstacle(){
        ArrayList<Dimension> a=new ArrayList<Dimension>();
        ArrayList<Dimension> b=new ArrayList<Dimension>();
        ArrayList<Dimension> c=new ArrayList<Dimension>();
        for (Snake s : snakelist) {
            a.addAll(s.Getdimensions());
            b.add(s.gethead());
            
            Dimension t=s.Nextmove(s.snakedirection);
            if ((!obstacles.contains(t))&&(!snakelocations.contains(t))){
                if ((t.height<20&&t.height>-1)&&(t.width>-1&&t.width<20)){                    
                    c.add(t);
                }
            }
            
        }
        obstacles=a;
        snakelocations=b;
        potential=c;
    }
    public boolean Hitobstacle(Snake s1){        
        //Dimension d=new Dimension(s1.snakex,s1.snakey);
        ArrayList<Dimension> occupied=new ArrayList<Dimension>();
        occupied.addAll(obstacles);
        occupied.addAll(snakelocations);
        occupied.remove(s1.gethead());
        /*
        for (Snake s:snakelist){
            if(s!=s1){
                occupied.add(s.gethead());
            }
        }
        */
        return occupied.contains(s1.gethead());
    }
    @Override
    public void paint(Graphics g){
        
        offsetimage=new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d=offsetimage.createGraphics(); 
        //Image image=new ImageIcon(getClass().getResource("/snakefighting/Title.gif")).getImage();
        //g2d.drawImage(image, 10, 10, this);                     
        g2d.drawImage(new ImageIcon(im).getImage(), 0, 0, this);        
        for (Snake s:snakelist){
            s.draw(g2d);                        
        }        
        food.draw(g2d);
        g2d.dispose();
        g.drawImage(offsetimage,0,0,this);
        g.dispose();
    }
    //move the snakes
    public void snakemove(){
        for (Snake s:snakelist){
            if(s.AIControl){
                s.Readmapandmove(this);
            }
        }
        for (int i=0;i<=snakenum;i++){
            Snake s=(Snake)snakelist.get(i);            
            if (i==snakeeatfood){                
                s.update(false);
            }
            else{
                s.update(true);
            }            
        }
        snakeeatfood=-1;
        
        Getobstacle();
    }
    //drop another food on map
    private void dropfood(){
        food=new Food("normal");
        int a;
        int b;
        Dimension c;
        boolean d;
        ArrayList<Dimension> occupied=new ArrayList<Dimension>();
        occupied.addAll(obstacles);
        occupied.addAll(snakelocations);
        /*
        for (Snake s:snakelist){
            occupied.add(s.gethead());
        }
        */
        do{
            a=(int)(Math.random()*20);
            b=(int)(Math.random()*20);
            c=new Dimension(a,b);
                      
        }while(occupied.contains(c));
        food.setlocation(c);
    }
    
    //including the judgement of win or lose;
    class Threadupdate implements Runnable{               
        public void run(){
            while ("run".equals(status)){
                try{
                    //update the scoreboard
                    Thread updateinfo=new Thread(new Updateinfo());
                    updateinfo.start();
                    updateinfo.join();
                    repaint();                    
                    Thread.sleep(globalspeed);
                    //make snake move
                    snakemove();
                    //see what's going on
                    snakelist.stream().forEach((s) -> {
                        judgement(s);
                    });
                    for (int i=0;i<=snakenum;i++){
                        Snake s=(Snake)snakelist.get(i);
                        if ("dead".equals(s.status)){                            
                            if (s==mysnake){
                                System.out.println("You Lose");
                            }
                            //when a snake die
                            snakelist.remove(i);
                            i--;
                            snakenum--;
                            Getobstacle();                             
                        }                        
                    }
                    repaint();
                    //if no snake alive finish the thread
                    if (snakelist.isEmpty()){
                        System.out.println("End");
                        break;                        
                    }                                                                               
                    //eating food;
                    for (int i=0;i<=snakenum;i++){
                        Snake s=(Snake)snakelist.get(i);
                        if (s.gethead().height==food.getlocation().height&&s.gethead().width==food.getlocation().width){
                            dropfood();
                            if(globalspeed>=50){
                                globalspeed-=5;
                            }
                            snakeeatfood=i;
                        }
                    }                
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            if ("end".equals(status)){
                System.out.println("End");
            }
        }
    }
    class Updateinfo implements Runnable{
        public void run(){
            String scoreinfo="<html><p><font size=+2 face=\"Lucida Handwriting\">"
                    + "<b>Scoreboard</b></font></p><p> </p><p> </p></html>";
            if (mysnake.status!="dead"){
                scoreinfo=scoreinfo.substring(0, scoreinfo.indexOf("</html>"))
                        +"<p><font size=+1 face=\"Lucida Handwriting\">Your score: "
                        +mysnake.score+"</font></p></html>";  
            }
            int n=1;
            for(Snake s:snakelist){
                if(s!=mysnake){
                    scoreinfo=scoreinfo.substring(0, scoreinfo.indexOf("</html>"))+
                        "<p> </p><p> </p><p><font size=+1 color=rgb("
                        +s.color.getRed()+","+s.color.getGreen()+","+s.color.getBlue()
                        +") face=\"Lucida Handwriting\">Player"+n+" score: "
                            +s.score+"</font></p></html>";
                    n++;
                }            
            }
            scorelabel.setText(scoreinfo);
            //scorelabel.repaint();
        }
        
    }
    public void judgement(Snake s2){
        if ((s2.snakex*20>=x||s2.snakey*20>=y)||(s2.snakex<0||s2.snakey<0)){
            s2.status="dead";
        }
        else if(Hitobstacle(s2)){
            s2.status="dead";
        }
        else{
            
        }        
    }
    public void startgame(){
        status="run";
        //new Thread(new Threadupdate()).start();
        gamerun=new Thread(new Threadupdate());
        gamerun.start();
    }    
    //Following part is for game controlling.
    public void Setcontrollable(boolean b){
        controllable=b;
    }
    @Override
    public void keyPressed(KeyEvent e){
        int code=e.getKeyCode();
        //System.out.println(code);
        if (controllable){
            switch (code){
                case KeyEvent.VK_UP:
                    mysnake.Setdirection("u");
                    break;
                case KeyEvent.VK_DOWN:
                    mysnake.Setdirection("d");
                    break;
                case KeyEvent.VK_LEFT:
                    mysnake.Setdirection("l");
                    break;
                case KeyEvent.VK_RIGHT:
                    mysnake.Setdirection("r");
                    break;
                case KeyEvent.VK_SPACE:
                    if (status=="run"){
                        status="pause"; 
                        System.out.println("pause");
                    }
                    else if (status =="pause"){
                        status="run";
                        gamerun=new Thread(new Threadupdate());
                        gamerun.start();
                        System.out.println("unpause");
                        
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    Reset();
                    break;
            }
        }
    }
    public void Reset(){
        status="finished";
        try{
            gamerun.join();
            snakelist=new ArrayList<>();
            mysnake=new Snake(10,1,5,"r",Color.black);
            snakenum=chosennum;
            //add all snakes to snake list
            //add the number of snakes: need to modify the scoreboard initial and the scoreboard update
            snakelist.add(mysnake);
            if (snakenum>0){
                Snake snake1=new Snake(10,5,5,"l",new Color(170,40,98));
                snake1.AIControl=true;
            snakelist.add(snake1);            
            }
            if (snakenum>1){
                Snake snake2=new Snake(10,9,5,"r",new Color(129,40,170));
                snake2.AIControl=true;
                snakelist.add(snake2);
            }
            if (snakenum>2){
                Snake snake3=new Snake(10,13,5,"l",new Color(40,55,170));
                snake3.AIControl=true;
                snakelist.add(snake3);
            }            
            status="run";
            globalspeed=260;
            gamerun=new Thread(new Threadupdate());
            gamerun.start();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        //System.out.println(e.getKeyChar());
    }
    @Override
    public void keyTyped(KeyEvent e){
        //System.out.println(e.getKeyChar());
    }    
}


class Node{
    Color color;
    int nodex;
    int nodey;
    Dimension xy;
    static int length=20;
    static int width=20;
    public Node(Color c,int x, int y){
        color=c;
        nodex=20*x;
        nodey=20*y;
        xy=new Dimension(x,y);       
    }
    public void draw(Graphics g2d){
        g2d.setColor(color);
        g2d.fill3DRect(nodex, nodey, 20, 20, true);
    }
    public void setxy(int x, int y){
        nodex=20*x;
        nodey=20*y;
        xy=new Dimension(x,y);
    }
}
class Food{
    String type;
    Color color;
    int foodx=0;
    int foody=0;
    Dimension xy;
    public Food(String s){
        type=s;
        switch(type){
            case "normal":
                color=new Color(40,170,160);
                break;
        }        
    }
    public void setlocation(Dimension d){
        foodx=20*d.width;
        foody=20*d.height;       
        xy=d;             
    }
    public Dimension getlocation(){
        return xy;
    }
    public void draw(Graphics g2d){        
        g2d.setColor(color);
        g2d.fill3DRect(foodx, foody, 20, 20, true);
    }
}

class Snake{
    int snakelength;
    int snakex;
    int snakey;
    float snakespeed;
    String snakedirection;
    private String snakegoing;
    Node head;
    List<Node> snakebody;
    Color color;
    String status;
    int score;
    //for AI purpose variables
    AStarNavigation ASN;
    boolean AIControl;
    boolean foodavailable=true;
    int loopcounter=0;
    
    public Snake(int x,int y,int length,String direction, Color c){
        status="alive";
        score=0;
        snakelength=length;
        snakex=x;
        snakey=y;
        color=c;
        snakebody=new ArrayList<Node>();
        head=new Node(color,x,y);
        snakebody.add(head);
        Node body;                
        snakespeed=1;
        snakedirection=direction;
        snakegoing=direction;

        if("r".equals(snakedirection)){
            for (int n=0;n<=snakelength-1;n++){
                body=new Node(color,x-n-1,y);
                snakebody.add(body);          
            }       
        }
        else{
            for (int n=0;n<snakelength-1;n++){
                body=new Node(color,x+1+n,y);
                snakebody.add(body);
            }
        }
    }
    public void draw(Graphics g2d){
        Node s;        
        for(int n=0;n<snakelength;n++){
            s=snakebody.get(n);
            s.draw(g2d);
        }    
    }
    public void update(boolean b){
        snakedirection=snakegoing;
        switch (snakedirection) {
            case "r":
                snakex=snakex+1;
                break;
            case "l":
                snakex=snakex-1;
                break;
            case "u":
                snakey=snakey-1;
                break;
            case "d":
                snakey=snakey+1;
                break;
        }
        
        head=new Node(color,snakex,snakey);
        snakebody.add(0, head);
        //when the food is eaten;
        if (b){
            snakebody.remove(snakelength);
        }
        else{
            snakelength+=1;
            score+=100;
            loopcounter=0;
        }
        
    }
    //limited control when direction is set
    public void Setdirection(String D){
        switch (snakedirection){
            case "r":
                if (!"l".equals(D)){
                    snakegoing=D;
                }
                break;
            case "l":
                if (!"r".equals(D)){
                    snakegoing=D;
                }
                break;
            case "u":
                if (!"d".equals(D)){
                    snakegoing=D;
                }
                break;
            case "d":
                if (!"u".equals(D)){
                    snakegoing=D;
                }
                break;           
        }
    }
    //get the body of snake as obstacles
    public ArrayList<Dimension> Getdimensions(){
        ArrayList<Dimension> demensions=new ArrayList<Dimension>();
        Node obstacle;
        for (int n=1;n<snakelength;n++){
            obstacle=(Node)snakebody.get(n);
            demensions.add(obstacle.xy);            
        }
        return demensions;
    }
    //get head position
    public Dimension gethead(){
        Dimension d=new Dimension(snakex,snakey);
        return d;
    }
    //get tail position
    public Dimension gettail(){
        Node n=snakebody.get(snakelength-1);
        return n.xy;
    }
    //read the map and decide next step;
    public void Readmapandmove(Snakecanvas SC){
        ArrayList<Dimension> path;
        ASN=new AStarNavigation();
        boolean check=ASN.Checktarget(SC, this);
        if (foodavailable!=check){
            loopcounter++;
        }
        foodavailable=check;
        //if the target is a trap, try to follow the tail of snake to seek an opportunity
        //if this action cause a endless loop, decline
        if ((!foodavailable)&&(loopcounter<6)){
            ASN=new AStarNavigation();        
            ASN.LoadMap(SC,this, gettail().width, gettail().height);
            //System.out.println("trap");
            path=ASN.Initial();
        }
        //else calculating the optimized path to the food
        else{
            ASN=new AStarNavigation();        
            ASN.LoadMap(SC, this,SC.food.xy.width,SC.food.xy.height);
            path=ASN.Initial();
        //if path is not found snake will try to chase its tail
            if (path.isEmpty()){
                ASN=new AStarNavigation();        
                ASN.LoadMap(SC,this, gettail().width, gettail().height);
                //System.out.println("no path");
                path=ASN.Initial();
            }
        //if self tail is not found snake try to seek others' tail 
        //this action is at a cost of losing advantage position for food so it has a lower priority
            if (path.isEmpty()){
            for (Snake s:SC.snakelist){
                if (s!=this){
                    ASN=new AStarNavigation();        
                    ASN.LoadMap(SC,this, gettail().width, gettail().height);
                    //System.out.println("other tail");
                    path=ASN.Initial();
                    if (!path.isEmpty()){
                        break;
                    }
                }
            }
            }
        }        
        if (!path.isEmpty()){            
            Dimension d=path.get(1);
            if (d.width>snakex){
                Setdirection("r");
            }
            else if(d.width<snakex){
                Setdirection("l");
            }
            else if(d.height>snakey){
                Setdirection("d");
            }
            else if(d.height<snakey){
                Setdirection("u");
            }
        }
        //if all targets are not found, try to avoid obstacles on random direction
        else{            
            Selfprotection(SC);
        }
        
        
    }
    //Self stress reaction for danger, enable when AI is on.
    private void Selfprotection(Snakecanvas sc){
        ArrayList<Dimension> danger=new ArrayList<>();
        danger.addAll(sc.obstacles);
        danger.addAll(sc.snakelocations);
        danger.addAll(sc.border);
        if (danger.contains(Nextmove(snakegoing))){
            //System.out.println(true);
            String[] direct={"r","l","u","d"};
            ArrayList<String> al=new ArrayList<>(Arrays.asList(direct));
            while (!al.isEmpty()){
                int index=(int)(Math.random()*al.size());
                if (!danger.contains(Nextmove(al.get(index)))){
                    Setdirection(al.get(index));
                    //System.out.println("turn");
                    break;
                }
                al.remove(index);
            }
        }                
    }
    //return the next dimension the snake will move if the direction is set as string "s"    
    public Dimension Nextmove(String s){
        Dimension d;
        switch (s){
            case "r":
                return d=new Dimension(snakex+1,snakey);
            case "l":
                return d=new Dimension(snakex-1,snakey);
            case "u":
                return d=new Dimension(snakex,snakey-1);
            default:
                return d=new Dimension(snakex,snakey+1);          
        }
    }
}