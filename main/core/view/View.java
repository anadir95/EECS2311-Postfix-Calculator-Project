/**     
 *  =================================================== 
 *  author :  Yari Yousefian
 *  This class is the user interface   
 *  =================================================== 
 */



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
// import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel ;
// import javax.swing.JButton;
import javax.swing.JFrame ;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;



public abstract class View extends JFrame implements ActionListener 
{
	
     protected int characters;
     
     protected JTextField textfield ;
     protected final Color ORANGE = new Color(255,69,0);
     protected final Color BRONZE = new Color(201,99,51);
     protected final Color GREENISH = new Color(169,219,128);
     protected final Color YELLOW = new Color(204,204,0);
     protected ButtonLayout buttons ;
     protected AppendState append;
     protected String[][] buttonLabels;
     protected String[] extraLabels;
     protected String[] trigLabels;
     protected boolean dot;
    
 	 protected final String DOT = "." ;
     
 	
     
     
    View(String lbl) 
    {
        super(lbl);       
    } 
    
   
    protected void construct(int size)
    {
    	createDisplayBox(size) ;
    	designLayout();
    	buttons.createButtons(buttonLabels) ;
    	observe();
        JPanel[] buttonPanels = buttons.designButtonLayout();
        for(int i = 0; i < buttonPanels.length; i++) add(buttonPanels[i]) ; 
    }
    
    
    protected abstract void designLayout();
    protected abstract void observe();
    public abstract void actionPerformed(ActionEvent evnt) ;
    public abstract void setMonitor(Controller vmc) ;
    public abstract void display(String outp) ;
    public abstract void changeGraph(boolean dr);
    
    protected void createDisplayBox(int size)
    {
    	 Dimension fieldDimension = new Dimension(279, 70);
    	 Font fieldFont =  new Font(Font.SANS_SERIF, Font.BOLD, 20) ;
    	 
    	 textfield = new JTextField(size);
    	 
    		textfield.setBackground(new Color(  0,99,0  ));     
    		textfield.setForeground(Color.GREEN);
    		
    		textfield.setFont(fieldFont);
    	        textfield.setEditable(false);
    	         
    	        textfield.setPreferredSize(fieldDimension);    
    	        
    	        
    	        Color c1 = new Color(86, 86, 86);
    	       
    	        Color c3 = new Color(204, 204, 204);
    	        Border b1 = new BevelBorder(EtchedBorder.RAISED, c3, c3 );  //c1, c3
    	        Border b2 = new MatteBorder(3, 3, 3, 3, GREENISH); //  base - 204 , 198 , 108 // new Color(204 , 198 , 108)
    	        Border b3 = new BevelBorder(EtchedBorder.LOWERED, c3, c1);
    	        
    	       
    	        textfield.setBorder(new CompoundBorder(new CompoundBorder( new CompoundBorder(b1, b2), b3),new EmptyBorder (0,3,0,0)  ));
    }
    
    protected void backspace() 
    {
    	
    			AbstractDocument doc = (AbstractDocument) textfield.getDocument();
    			  int length = doc.getLength() ;
    			  if(length>0)
    			  {
    		        int position = length - 1;
    		        try {  
    		        	  doc.remove(position, 1);
    		            } catch (Exception exc) 
    		                    {
    		            	      System.out.println("Calculator could not backspace !");
   	        	                  System.out.println(exc.getMessage());
   	        	                  System.out.println(  exc.getLocalizedMessage()  );
    		                    }
    		        characters--;
    		        
    			  }
    	
    }
    
    public void clear() 
    {System.out.println("clearing textfield");
    	  textfield.setText("") ;
		  characters = 0;
		 dot = false;
		 
    }
    public void setFieldState(boolean currentState)
    {
    	if(currentState)append.on();
    	else append.off();
    }
    
    
    /* protected void clear() 
    {
    	AbstractDocument doc = (AbstractDocument) textfield.getDocument();
		  int length = doc.getLength() ;
		  if(length>0)
		  {
	        int position = length - 1;
	        
	          try {
	                doc.remove(position, length);
	              } catch (Exception exc) 
	                   { 
	        	         System.out.println("Calculator could not reset !");
	        	         System.out.println(exc.getMessage());
	        	         System.out.println(  exc.getLocalizedMessage()  );
	                   } 
		  }
		  characters = 0;
    } */
   
   
}
