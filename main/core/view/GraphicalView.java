/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class is one particular user interface   
 *  =================================================== 
 */ 

 
 import java.awt.*;

 import javax.swing.*;
 

import java.awt.event.*;
 import javax.swing.border.*;


public class GraphicalView extends View 
{
	
	   
    private final Color BACKGROUND = new Color(102,108,63,200);    
    
    private final String EMPTY = "empty" ;
    
    private JPanel topPanel,  graphingPanel, innerPanel;

    private Curve curve;
    
    
    
   
    protected Controller monitor;
    
    
    
    protected JButton[] digitButton;
    protected JButton[] instructionButton;
    protected JButton[] extraButton;
    protected JButton[] trigonometricButton;
    protected JButton[] operatorButton;
    protected JButton dotButton ;
    
    private final String[][] LABELS = {        
    		                            { "4", "3", "2", "1", "0", "9", "8", "7","6","5"},
    		                            { "+", "-", "x", "/" },
    		                            {"(-)", "!" },
    		                            {"CLEAR", "ENTER","undo", "mode", "select"},
    		                            {"rad","sin","cos","Pi"},
    		                            {DOT}
                                      };
   
    
    GraphicalView() 
    {
        super("Postfix Calculator");
        
        
        setSize(327, 420);  //  size of the frame
        setResizable(false);  // frame cannot be resized
        setDefaultCloseOperation(EXIT_ON_CLOSE);   // to exit the frame when closed
	     
	    
	    
	    append = new AppendState();
	    
	    
	    characters = 0;
        dot = false;
        buttons = new ButtonLayoutOne();
       
        buttonLabels = LABELS ;
        
        construct(16);
        textfield.setText(" 0 | Start new computation");
        setFieldState(false);
        setVisible(true);
    }
    
    
    
    protected void designLayout()
    {
    	 getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    	 
    	 setStyle();
	       
	     FlowLayout f1 = new FlowLayout(FlowLayout.CENTER,3,19);  // for top panel
	     FlowLayout f11 = new FlowLayout(FlowLayout.CENTER,0,0);  // for graphing panel
	    
    	
    	 topPanel = new JPanel(); 
	     topPanel.setBackground(BACKGROUND);  
	     topPanel.setLayout(f1);
	     topPanel.add(textfield);
	     add(topPanel);
	     
	     graphingPanel = new JPanel(); 
	     graphingPanel.setBackground(BACKGROUND);  
	     graphingPanel.setLayout(f11);
	     curve = new Curve(true);
	     
	     curve.setPreferredSize(new Dimension(279, 50));
	     
	     Color c1 = new Color(86, 86, 86);
	     Color c2 = new Color(192, 192, 192);
	     Color c3 = new Color(204, 204, 204);
	     Border b1 = new BevelBorder(EtchedBorder.RAISED, c3, c1);
	     Border b2 = new MatteBorder(2, 2, 2, 2, Color.blue);
	     Border b3 = new BevelBorder(EtchedBorder.LOWERED, c3, c1);
	     
	      curve.setBorder(     new CompoundBorder(new CompoundBorder(b1, b2), b3)    );
	     
	      graphingPanel.add(curve);
	    
	     add(graphingPanel);
	
    }
    
    
    
    private final void setStyle()
    {
        try {
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
            } catch(Exception exc) { 
        	                         System.out.println(" Could not plug look and feel ! daaamn !");  
        	                         System.out.println(exc.getMessage());
 	        	                     System.out.println(  exc.getLocalizedMessage()  );
                                   }
    }
   
    protected void observe()
    {
    	 digitButton = buttons.getButtons(0);
         instructionButton = buttons.getButtons(3);
         extraButton = buttons.getButtons(2);
         trigonometricButton = buttons.getButtons(4);
         operatorButton = buttons.getButtons(1);
         dotButton = (buttons.getButtons(5))[0];
         
         for(int i = 0; i <digitButton.length; i++)  (digitButton[i]).addActionListener(this);
	        
	     for(int i = 0; i <instructionButton.length; i++)    (instructionButton[i]).addActionListener(this);
	         
	     for(int i = 0; i <extraButton.length; i++)   (extraButton[i]).addActionListener(this);
	     
	     for(int i = 0; i <operatorButton.length; i++)  (operatorButton[i]).addActionListener(this);
	      
	     for(int i = 0; i <trigonometricButton.length; i++)    (trigonometricButton[i]).addActionListener(this);
	       	    
         dotButton.addActionListener(this); 
    }
   
    public void actionPerformed(ActionEvent evnt)
    {
    	Message msg = new Message();
        boolean found = false;
        if(evnt.getSource() == dotButton)  // dot button
            { 
        	 if(!dot)  // view ignores too many dots
        	 {
        		  
              if(append.active())textfield.setText(textfield.getText()+".");
 			  else 
 			      { 
 				    
 				    textfield.setText("."); 
 				    append.on();
 				  }
              characters++;
              dot = true;
              found = true; 
        	 }
            }
        if(!found) 
          {
        	   for(int i = 0; i < 10; i++)  //  number buttons
        	   {
        		   
        		 if (evnt.getSource() == digitButton[i])
        		 {        			 
        			 if(append.active())textfield.setText(textfield.getText()+LABELS[0][i]);
        			 else {        				    
        				    textfield.setText(LABELS[0][i]); 
        				    append.on();
        				  }
                     characters++;
                     found = true;
                     break ;
        		 }
               }
        	   
        	   
               if(!found)
               {
        		     //  other buttons
               	                 		   
               		 if( evnt.getSource() == operatorButton[0] )   // +
               		 {
               			 
                          if( characters>0 )   msg.setContent(textfield.getText()) ;
                          else  msg.setContent(EMPTY) ; 
                        	 
                          msg.setSource(LABELS[1][0]) ;
                          if (append.active()) msg.setState("append");
                          else msg.setState("replace");
                          monitor.inform(msg);
                          
               		 }
               		 
               		 else if (evnt.getSource() == operatorButton[1])  // -
              		 {
              			 
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[1][1]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
                           
              		 }
               		
               		 else if (evnt.getSource() == operatorButton[2])   // *
              		 {
              			 
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[1][2]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
                         
                         
              		 }
               		 else if ( evnt.getSource() == operatorButton[3])   // divide
              		 {
              			 
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[1][3]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
                           
              		 }
               		 else if (evnt.getSource() == instructionButton[0])   // clear  
              		 {              			                         
                       
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[3][0]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
                           
              		 }
               		 else if (evnt.getSource() == instructionButton[1])   // enter  
             		 {    
             		
             		           			                         
             		   if( characters>0 )   msg.setContent(textfield.getText()) ;
                       else  msg.setContent(EMPTY) ; 
                  	 
                        msg.setSource(LABELS[3][1]) ;
                         
                       if (append.active()) msg.setState("append");
                       else msg.setState("replace");
                       monitor.inform(msg);
                         
             		 }
               		 else if (evnt.getSource() == instructionButton[4])   // select 
             		 {              			                         
                        
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[3][4]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
                          
             		 }
               		else if (evnt.getSource() == instructionButton[3])   // mode 
           		    {              			                                           	   
              			
              			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[3][3]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == instructionButton[2])   // undo 
            		 {              			                                           	   
               			if( characters==1 ) msg.setContent("expr") ;
               			else if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                      	 
                        msg.setSource(LABELS[3][2]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
            		 }
               		else if (evnt.getSource() == trigonometricButton[1])   // sin 
           		    {              			                                           	   
              			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[4][1]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == trigonometricButton[2])   // cos 
           		    {              			                                           	   
              			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[4][2]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == trigonometricButton[0])   // radian
           		    {              			                                           	   
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[4][0]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == trigonometricButton[3])   // pi
           		    {              			                                           	   
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[4][3]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == extraButton[1])   // factorial
           		    {              			                                           	   
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
                     	 
                        msg.setSource(LABELS[2][1]) ;
                        if (append.active()) msg.setState("append");
                        else msg.setState("replace");
                        monitor.inform(msg);
           		    }
               		else if (evnt.getSource() == extraButton[0])   // +/- 
           		    {              			                         
               			if( characters>0 )   msg.setContent(textfield.getText()) ;
                        else  msg.setContent(EMPTY) ; 
              			
                     	 
                       msg.setSource("sign") ;
                       if (append.active()) msg.setState("append");
                       else msg.setState("replace");
                       monitor.inform(msg);
                      
                        
           		    }
               }		 
            
          }
    }
    
    
    public void setMonitor(Controller vmc) 
    {
    	monitor = vmc;
    	
    }
    
    public void display(String outp) 
    {
    	
    	textfield.setText(outp);
    	characters = outp.length() ;
    	append.off();
    	dot = false;
    	
    }
    
    
    
    public void sin(int x, double y)
    {  
    	curve.input(x,y,"sin");
    	 
    	curve.repaint();
    	
    	DisplayFormat f = new DisplayFormat(y);
    	display(f.format());
    }
    
    public void cos(int x, double y)
    {
    	curve.input(x,y,"cos");
    	curve.repaint();
    	 
    	
    	DisplayFormat f = new DisplayFormat(y);
    	display(f.format());
    }
    
    public void changeGraph(boolean draw)
    {
    	curve.reset(draw);
    	curve.repaint();
    }
    
   

/****************************************************************************
 * This class plots a sine/cosine curve between -2*PI and 2*PI
 ********************************************************************/

    protected class Curve extends JPanel
    {
    	private final int FOUR_PI = 720 ;
    	private boolean connect;  // whether to draw  line between dots
    	private boolean draw;
    	private String curve;
        private int lastY ;
        private int degree;
 
        
        public Curve( boolean cnct)
        {
        	super(true);
        	
        	
        	Color test = new Color(0,0,0);  
        	this.setBackground(test);
        	connect = cnct;
        	
        	lastY = 0;
        	draw = false;
        	
        }
                      
        
        public void setConnection(boolean b)
        {
        	connect = b;       
        }
        double radian (int deg)
        {
            return ((2*Math.PI)/360.0) * deg;
        }
 
        int convert (int i, int width)  // from viewport to 4pi
        {
            return (int) ((i/(double)width)*FOUR_PI);  
        }
        
        int revert(int i, int width)  // from 4pi to viewport
        {
            return (int) ((i/(double)FOUR_PI)*width);  
        }
        
        public void input(int x , double y, String curvetype)
        {
        	curve = curvetype ;
        	degree = x ;       	
        	draw = true;
        }
        
        public void paintComponent (Graphics g)
        {
           
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            
            if(draw)
            {
              g.setColor( Color.cyan);
              for (int i=0; i<width; i++)  // draw the curve
              {
               int y;
               if(curve.equalsIgnoreCase("sin"))  y = (int) Math.round( ( - Math.sin(  radian( convert(i,width-14)  ) )+1.4)*(   (height-14)/2.0      ) );  // sin curve
               else  y = (int) Math.round((-Math.cos(radian(convert(i,width-14) ))+1.4)*(height-14)/2.0);  // cos curve
               
               if (connect && i>0) g.drawLine(i-1+7, lastY, i+7, y);
               else  g.drawLine(i, y, i, y);
                
               lastY = y;
              }
            }
            
            g.setColor( Color.orange);
            int  halfX = (int) (width/2.0);
            int  halfY = (int) (height/2.0);
            int gap = 5;
            for (int i=0; i<width; i=i+gap) g.drawLine(i, halfY, i, halfY);  // draw x axis
            for (int i=0; i<height; i=i+gap) g.drawLine(halfX, i, halfX, i);  // draw y axis
             
            g.setColor( Color.red);
            
            
            halfX = (int) ((width-14)/4.0);
            
            gap = 10;
            
            for (int i=0; i<height; i=i+gap) g.drawLine(halfX+7, i, halfX+7, i);  // draw -pi    
            halfX *= 3;
            for (int i=0; i<height; i=i+gap) g.drawLine(halfX+7, i, halfX+7, i);  // draw pi  
            
            if(draw) // highlight the point location 
            {
               g.setColor( new Color( 255 , 156 , 0 ));  
               
               int x = revert((360 + degree), width-14)+7;
               
               int y;
               if(curve.equalsIgnoreCase("sin"))  y = (int) Math.round( ( - Math.sin(  radian( degree  ) )+1.4)*(height-14)/2.0 );  // sin curve
               else  y = (int) Math.round((-Math.cos(radian( degree ))+1.4)*(height-14)/2.0);
           	
               g.drawOval(x-1,  y-2,   4,  4) ;
               
            }
            draw = false;
        }
        
        public void reset(boolean dr)
        {
        	draw = dr;
        }
        
    }
    
  
    
}
