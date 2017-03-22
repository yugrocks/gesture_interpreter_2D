import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;



public class PointsContainer implements Serializable{
	
	private ArrayList<Point> EPoints;
    private String a;
    public PointsContainer(ArrayList<Point> arrayList,String a){
    	this.EPoints=arrayList;
    	this.a=a;
    }
	
    public ArrayList<Point> getArray(){return EPoints;}
    
    public String getChar(){return a;}
}
