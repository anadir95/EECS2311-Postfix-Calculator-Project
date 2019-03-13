/**     
 *  ========================================================================================= 
 *  author :  Yari Yousefian
 *  This class is one particular type button layout, creates and places button certain way  
 *  ======================================================================================= 
 */



import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;


public class ButtonLayoutOne implements ButtonLayout
{
	
    
    private final Color BRONZE = new Color(201,99,51);
    private final Color ORANGE = new Color(255,69,0);
    private final Color GREENISH = new Color(169,219,128);
    private final Color BACKGROUND = new Color(102,108,63,200);
    private JPanel topPanel, trigonoPanel,  innerPanel;
    private FlowLayout f2 = new FlowLayout(FlowLayout.CENTER,0,0);
    private JButton[][] button;
    
    
	public JPanel[] designButtonLayout()
	{
	     JPanel[] panels = new JPanel[2] ;
	    
	     
	     topPanel = new JPanel(); 
	     topPanel.setLayout(f2); 
    	     topPanel.setBackground(BACKGROUND);   
    	     Box holder = Box.createVerticalBox();
	     
	     Box[] box = new Box[3];
	     
	     box[0] = Box.createHorizontalBox(); // for basic op buttons
	     box[0].setBorder(BorderFactory.createEmptyBorder(7,0,5,0) );
	     for(int i = 0; i < button[1].length; i++)  {  box[0].add(button[1][i]);  box[0].add(Box.createRigidArea(new Dimension(2, 0)));}
	     
	     
	     box[1] = Box.createHorizontalBox(); // for extra butttons
	     box[1].setBorder(BorderFactory.createEmptyBorder(4,0,3,0) );
	     for(int i = 0; i < button[2].length; i++)  {  box[1].add(button[2][i]);  box[1].add(Box.createRigidArea(new Dimension(3, 7)));}
	     box[1].add(button[5][0]);
	     
	    
	     box[2] = Box.createVerticalBox();   // for instruction buttons
	     box[2].setBorder(BorderFactory.createEmptyBorder(1,2,1,2) );
	     for(int i = 0; i <button[3].length; i++)    box[2].add(button[3][i]); //  box[2].add(Box.createRigidArea(new Dimension(0, 1)));}
	        
	
	
	     innerPanel = new JPanel(); //  for digit buttons
	     innerPanel.setBackground(BACKGROUND);
	     GridLayout grid = new GridLayout(2,5);  
	     innerPanel.setLayout(grid);
	     for(int i = 0; i < button[0].length; i++)   innerPanel.add(button[0][i]);  // adding number buttonss
	     innerPanel.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2), new CustomBorder(7,7) ));
	           
	      
	     holder.add(innerPanel); 
	     holder.add(box[0]);  
	     holder.add(box[1]);
	   
       	 topPanel.add(holder);
        
         topPanel.add(box[2]);
             
         trigonoPanel = new JPanel();
         
   	     trigonoPanel.setBackground(BACKGROUND);  
   	     
  
   	     for(int i = 0; i < button[4].length; i++)   trigonoPanel.add(button[4][i]);
     
         panels[0] = topPanel;  
	     panels[1] = trigonoPanel;    
	       
	     return panels ;
	}
	
	
	public void createButtons(String[][] ragged )
	{	 
		
		    button = new JButton[ragged.length][];
		    Dimension[] buttonDimension = new Dimension[6];
		     buttonDimension[3] = new Dimension(90, 43);  // instructions
		     buttonDimension[0] = new Dimension(45, 47);  //  digit
		     buttonDimension[1] = new Dimension(46, 47); //   op
		     buttonDimension[2] = new Dimension(52, 47); // extra
		     buttonDimension[4] = new Dimension(77, 47);  // trigonometric buttons
		     buttonDimension[5] = new Dimension(80, 47);  // DOT button
	        Font buttonFont =  new Font(Font.SANS_SERIF, Font.BOLD, 15) ; 
	        
	        for(int b = 0; b < ragged.length; b++)
	        {
	           button[b] = new JButton[ragged[b].length];
	          for(int i = 0; i <ragged[b].length; i++) 
	          {
	        	    
	        	 button[b][i] = new JButton();
	            (button[b][i]).setText(ragged[b][i]);
	            (button[b][i]).setFont(buttonFont);
	           
	            (button[b][i]).setBackground(BRONZE);
		    
		        (button[b][i]).setMinimumSize(buttonDimension[b]); 
		        (button[b][i]).setMaximumSize(buttonDimension[b]);
		       
		       
		         button[b][i].setForeground(new Color(0,0,0));  
		      
	           
	          }
	         }
	        
	        (button[ragged.length-1][0]).setBackground(ORANGE);
	        (button[ragged.length-1][0]).setForeground(GREENISH);
	         
	    
	}      
	
	public JButton[] getButtons(int i)
	{
		return button[i] ;
	}
}