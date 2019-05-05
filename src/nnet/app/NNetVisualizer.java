package nnet.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import nnet.NN;

@SuppressWarnings("serial")
public class NNetVisualizer extends java.awt.Frame implements ItemListener, ActionListener, MouseMotionListener, MouseListener, WindowListener{  
    
  public NNetVisualizer(){	
	setTitle("-> NeuralNet Visualizer <-");
  	setSize(FRAMEX, FRAMEY);
	setLocation(LOCX, LOCY);
	
	setLayout(null);
	
	setBackground(Color.lightGray);		
	addMouseMotionListener(this);                           	
	addMouseListener(this);		
	addWindowListener(this);
	initColors();	
  }    
  
  private void initColors(){
  	colpal = new ArrayList<Color>(COLORS);
  	boolean[] red   = new boolean[256];
  	boolean[] green = new boolean[256];
  	boolean[] blue  = new boolean[256];  	
  	colpal.add( Color.white );  	  
  	red[255] = true; green[255] = true; blue[255] = true;
  	for(int i=0;i<COLORS-1;i++){
  	  int r = (int)(Math.random()*256);
  	  int g = (int)(Math.random()*256);
  	  int b = (int)(Math.random()*256);  	  
  	  if(red[r]&&green[g]&&blue[b]){  	    
	    i--;  	      	      	    
  	  }
  	  else{
  	    colpal.add( new Color(r,g,b) );
  	    red[r]   = true;
  	    green[g] = true;
  	    blue[b]  = true;  	    
   	  }   	    	  
  	}  	
  }
  
  public void windowActivated(WindowEvent e){
  }  
  public void windowClosed(WindowEvent e){
  }
  public void windowClosing(WindowEvent e){
  	System.exit(0);
  }
  public void windowDeactivated(WindowEvent e){
  }
  public void windowDeiconified(WindowEvent e){
  }
  public void windowIconified(WindowEvent e){
  }
  public void windowOpened(WindowEvent e){
  }  
  
  public void mousePressed(MouseEvent e){           		  	
  	grappedNeuron = grapNeuron( e.getX(), e.getY() );
  }
  
  public void mouseReleased(MouseEvent e){           		
    grappedNeuron = -1;  	     
  }
  
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){} 
  public void mouseMoved(MouseEvent e){}  
    
  public void mouseDragged(MouseEvent e){  
	if (grappedNeuron > -1){
	  neuronPos[grappedNeuron][0] = e.getX();
	  neuronPos[grappedNeuron][1] = e.getY();
	  repaint();
	}
  }

  public void itemStateChanged(ItemEvent event){
  	
  }

  public void actionPerformed(ActionEvent e){	
  	
  }  


  
  public void visualize(NN net, int mode){
  	this.net = net;
  	int neuronCount = net.neuronCount();
	neuronPos = new int[ neuronCount ][2];
		
	
	int layer = 0;
	int xoff  = (FRAMEX / 3) - 50;
	int yoff  = FRAMEY / 10;
    for(int i=0;i<neuronCount;i++){	  
	  neuronPos[i][0] = xoff;
	  neuronPos[i][1] = yoff+(layer*120);
	  if( (i<neuronCount-1) && net.connected(i, i+1) ){	// carriage return
	    layer++;		
		xoff = (FRAMEX / 3) - 50;
	  }else{
	  	xoff+=120;
	  }
	}
	
    repaint();
  }
  
  
  public int grapNeuron(int x, int y){
  	int n = -1;
    for(int i=0;i<net.neuronCount();i++){	  
	  if( (x >= neuronPos[i][0]) && (x <= neuronPos[i][0]+NEURONSIZE) && (y >= neuronPos[i][1]) && (y <= neuronPos[i][1]+NEURONSIZE) ){
	    n = i;
		break;
	  }
    }	
	return n;
  }
  
  public void paint(Graphics g){
    
	if (net == null)
	{
		return;
	}
          	
	int neuronCount = net.neuronCount();
	for(int i=0;i<neuronCount;i++){
	for(int j=0;j<neuronCount;j++){
 	  if(net.connected(i,j)){
	    int dx = neuronPos[j][0] - neuronPos[i][0];
		int dy = neuronPos[j][1] - neuronPos[i][1];
	    g.drawLine( neuronPos[i][0]+NEURONSIZE/2, neuronPos[i][1]+NEURONSIZE/2, neuronPos[j][0]+NEURONSIZE/2, neuronPos[j][1]+NEURONSIZE/2 );
		Float w     	= new Float( net.getWeight(i,j) );
	 	String wstr  	= w.toString();
		int comma 		= wstr.indexOf('.');
		String wstrDis = "NaN";
		if(comma>=0)
		  wstrDis  = wstr.substring(0,comma) + wstr.substring(comma,comma+2);
		
		g.drawString( ""+wstrDis, neuronPos[i][0]+NEURONSIZE/2+(3*dx/4), neuronPos[i][1]+NEURONSIZE/2+(3*dy/4) );
	  }	
	}
	}
	
	Color c;
	for(int i=0;i<neuronCount;i++){
	  if(net.isInputNeuron(i)){
	    c = Color.green;
	  }else{
	    c = Color.white;
	  }	  	  
	  g.setColor(c);	  
	  g.fillOval(neuronPos[i][0], neuronPos[i][1], NEURONSIZE, NEURONSIZE);
	  g.setColor(Color.black);
	  g.drawOval(neuronPos[i][0], neuronPos[i][1], NEURONSIZE, NEURONSIZE);
	}
  }      
  
  
  public void update(Graphics g){
  	if(offscreen == null)  offscreen = createImage(this.getSize().width,this.getSize().height);  	  
	Graphics dbGraphics = offscreen.getGraphics();	
	dbGraphics.setColor( getBackground() );
	dbGraphics.fillRect( 0,0,this.getSize().width,this.getSize().height );
	dbGraphics.setColor( getForeground() );
	paint(dbGraphics);
	g.drawImage(offscreen,0,0,this);
  }    
  
  
  
  public static void main(String[] args){
  	int[] layers = { 2, 2, 3, 1	};
  	NN net = new nnet.LayeredNet(8, layers);
	net.randomlyInitWeights(-1, 1);
	NNetVisualizer vis = new NNetVisualizer();
	vis.setVisible(true);
	vis.visualize(net, LAYERED);	
  }
  
  
  private final int FRAMEX   = 400;
  private final int FRAMEY   = 700;
  private final int LOCX     = 50;
  private final int LOCY     = 25;  
  private final int COLORS   = 400;			      
  
  private final int NEURONSIZE = 30;			      
  
  public static final int LAYERED = 10;
  public static final int CIRCLED = 11;
  
  final String[] BUTTONLABELS = {   				   
   				   "exit"
   				 };
   	
 				 				
  private ArrayList<Color> colpal;    	        	          
  private NN net; 
  
  private int[][] neuronPos;
  
  private Image offscreen;  
  
  private int grappedNeuron;
}