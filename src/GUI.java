import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Date;

public class GUI extends JFrame{
    
	public int titleBar=31;
	public int gapAbove=57;
	public int gapBelow=100;
	
	public int a=10;
	public int b=5;
	public int side=100;
	public int spacing=2;
	
	double startPercent=50;
	double rate=0.5;
	double accl=0.01;
	int minimumLevel=10;
	
	double coronaLevel[][]=new double[a][b];
	Date start=new Date();
	int sec=0;
	
	int mx=-100;
    int my=-100;
    public char selected;
    Image img,virus2,virus4,virus6,virus8,virus10,mask,soap,sanitiser;
    
    public GUI()
    {
        
    	this.setTitle("Corona Yoddha");
    	this.setSize(a*side+16,b*side+titleBar+gapAbove+gapBelow+7);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        
        for(int i=0;i<a;i++)
            for(int j=0;j<b;j++)
            	if(Math.random()*100>startPercent)
            	coronaLevel[i][j]=(int)(minimumLevel+Math.random()*(100-minimumLevel));
        
        selected=' ';
        
        Board board=new Board();
        this.setContentPane(board);
        
        Move move=new Move();
        this.addMouseMotionListener(move);
        
        Click click=new Click();
        this.addMouseListener(click);
        
        try
        {
        	img=null;
        	virus2=ResourceLoader.loadImage("virus2.png");
            virus4=ResourceLoader.loadImage("virus4.png");
            virus6=ResourceLoader.loadImage("virus6.png");
            virus8=ResourceLoader.loadImage("virus8.png");
            virus10=ResourceLoader.loadImage("virus10.png");
            mask=ResourceLoader.loadImage("mask.png");
            soap=ResourceLoader.loadImage("soap.png");
            sanitiser=ResourceLoader.loadImage("sanitiser.png");
            
        }
        catch(IOException e)
        {
        	e.printStackTrace();
        }
    }
    
    public class Board extends JPanel{
        @Override
        public void paintComponent(Graphics g)
        {
            //game board
        	g.setColor(Color.DARK_GRAY);
            g.fillRect(0,0,a*side,b*side+gapAbove+gapBelow);
            
            //time
            sec=(int)((new Date().getTime()-start.getTime())/1000);
            
            if(sec==1)
            {
            	start=new Date();
            	for(int i=0;i<a;i++)
                    for(int j=0;j<b;j++)
                    	if(coronaLevel[i][j]<100-rate) coronaLevel[i][j]+=rate; else coronaLevel[i][j]=100;
            	rate+=accl;
            }
            
            //divider line above
            g.setColor(Color.gray);
            g.fillRect(0,gapAbove-1,a*side,1);
            
            
            //a by b will be the grid...each box side*side pixels with 'spacing' padding within
            //cells and virus
            int padding=0;
            for(int i=0;i<a;i++)
            {
                for(int j=0;j<b;j++)
                {
                    
                	//background
                    g.setColor(new Color((int)(coronaLevel[i][j]*2.55),200-(int)(Math.abs(coronaLevel[i][j]-50)*2.55),255-(int)(coronaLevel[i][j]*2.55)));
                    
                    //background when hover
                    if(mx>=side*i+spacing && mx<side*i+side-spacing && my>=j*side+spacing+gapAbove+titleBar && my<j*side+side-spacing+gapAbove+titleBar)
                        g.setColor(Color.LIGHT_GRAY);
                    
                    g.fillRect(side*i+spacing,j*side+spacing+gapAbove, side-2*spacing, side-2*spacing);
                    
                    if(coronaLevel[i][j]>minimumLevel && coronaLevel[i][j]<=20)
	                {
                    	img=virus2;
	                    padding=40;
	                }
	                if(coronaLevel[i][j]>20 && coronaLevel[i][j]<=40)
	                {
	                   	img=virus4;
	                   	padding=30;
	                }
	                if(coronaLevel[i][j]>40 && coronaLevel[i][j]<=60)
	                {
	                   	img=virus6;
	                   	padding=20;
	                }
	                if(coronaLevel[i][j]>60 && coronaLevel[i][j]<=80)
	                {
	                   	img=virus8;
	                   	padding=10;
	                }
	                if(coronaLevel[i][j]>80 && coronaLevel[i][j]<=100)
	                {
	                   	img=virus10;
	                   	padding=0;
	                }
	                if(coronaLevel[i][j]>minimumLevel)
	                   	g.drawImage(img,side*i+spacing+padding,gapAbove+j*side+spacing+padding,null);
	                
	                //g.setColor(Color.white);
	                //g.drawString(""+coronaLevel[i][j],side*i+spacing+padding,gapAbove+j*side+spacing+padding);
                }
            }
            
            //divider line below
            g.setColor(Color.gray);
            g.fillRect(0,b*side+gapAbove,a*side,1);
            
            //tools
            img = mask;
			g.drawImage(img,5,b*side+gapAbove+5,null);
	        img = soap;
	        g.drawImage(img,250,b*side+gapAbove+5,null);
	        img = sanitiser;
	        g.drawImage(img,500,b*side+gapAbove+5,null);
			
	        //selected tool
	        if(selected!=' ')
	        {
	           	switch(selected)
	          	{
		           	case 'm':
		           		img = mask;
			           	break;
		           	case 's':
		           		img = soap;
			           	break;
		           	case 'a':
		          		img = sanitiser;
		        }
		        g.drawImage(img, mx-(img.getWidth(null)/2), my-(img.getHeight(null)/2)-titleBar, null);
	        }
        }
    }
    
    public class Move implements MouseMotionListener
    {
    	@Override
        public void mouseMoved(MouseEvent e) {
            mx=e.getX();
            my=e.getY();
        }
    	@Override
        public void mouseDragged(MouseEvent e) {
    		mx=e.getX();
            my=e.getY();
            
            kill(inBoxX(),inBoxY());
    	}
    }
    
    public class Click implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e)
        {
        	mx=e.getX();
            my=e.getY();
            
            if(my>b*side+gapAbove+titleBar)
            	select();
            
            kill(inBoxX(),inBoxY());
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
    
    void select()
    {
    	if(selected==' ')
    	{
	    	if(mx>5 && mx<5+100)
	    		selected='m';
	    	else if(mx>250 && mx<250+100)
	    		selected='s';
	    	else if(mx>500 && mx<500+100)
	    		selected='a';
	    	else
	    		selected=' ';
    	}
    	else
    	{
    		if(mx>5 && mx<5+100 && selected=='m')
	    		selected=' ';
	    	else if(mx>250 && mx<250+100 && selected=='s')
	    		selected=' ';
	    	else if(mx>500 && mx<500+100 && selected=='a')
	    		selected=' ';
    	}
    }
    
    void kill(int x,int y)
    {
        if(selected!=' ' && x!=-1 && y!=-1)
        {
        	switch(selected)
        	{
        		case 'm':
        			if(coronaLevel[x][y]>=5) coronaLevel[x][y]-=5; else coronaLevel[x][y]=0;
        			break;
        		case 's':
        			if(coronaLevel[x][y]>=10) coronaLevel[x][y]-=10; else coronaLevel[x][y]=0;
            		break;
        		case 'a':
        			if(coronaLevel[x][y]>=20) coronaLevel[x][y]-=20; else coronaLevel[x][y]=0;
        	}
        }
    }
    
    public int inBoxX(){
        for(int i=0;i<a;i++){
                for(int j=0;j<b;j++){
                    if(mx>=side*i+spacing && mx<side*i+side-spacing && my>=j*side+spacing+gapAbove+titleBar && my<j*side+side-spacing+gapAbove+titleBar)
                        return i;
                }
            }
        return -1;
    }
    public int inBoxY(){
        for(int i=0;i<a;i++){
                for(int j=0;j<b;j++){
                    if(mx>=side*i+spacing && mx<side*i+side-spacing && my>=j*side+spacing+gapAbove+titleBar && my<j*side+side-spacing+gapAbove+titleBar)
                        return j;
                }
            }
        return -1;
    }
}