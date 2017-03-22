import javax.swing.JPanel;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;


public class DrawablePanel extends JPanel{
    private int altPointCount=1;
	private int pointCount=0;
	private ArrayList<Point> points=new ArrayList<Point>();
	
	public DrawablePanel(){
		addMouseMotionListener(
				
				new MouseMotionAdapter(){
					
					public void mouseDragged(MouseEvent event){
						if (pointCount<=points.size()){
							points.add(event.getPoint());
							pointCount++;
							repaint();
							//System.out.println(event.getPoint());
						}
					}
				}
				);	//end inner class
	}//end constructor
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
			if(altPointCount!=0){
			for(int i=0;i<pointCount;i++){
			g.fillOval(points.get(i).x, points.get(i).y,4,4);	
		}
			}
		
	}//end paintComponent
	
	public void clear(){
		altPointCount=0;
		points.clear();
		paintComponent(this.getGraphics());
		altPointCount=1;
		pointCount=0;
	}
	
	
	public ArrayList<Point> getPoints(){
		return points;	
	}
}
