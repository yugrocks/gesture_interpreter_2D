import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Point;
import java.awt.Color;
import javax.swing.JTextArea;

public class mainGUI extends JFrame{
	private static ObjectOutputStream output;
	private static DrawablePanel panel;
	private static JTextField field;
	private JButton button;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	static ArrayList<PointsContainer> dataMain;
	private static JLabel result;
	private static JTextArea status;
	private JLabel status0;
	private static String STATUS;
	public mainGUI(){
		super("Draw gesture");
		setLayout(null);
		setSize(600,600);
		panel=new DrawablePanel();
		panel.setBackground(Color.red);
		panel.setSize(200,200);
		panel.setLocation(100,250);
		add(panel);
		panel.setLayout(null);
		
		status =new JTextArea();
		status.setSize(200,200);
		status.setLocation(330,250);
		add(status);
		status.setEditable(false);
		
		status0=new JLabel("Status");
		status0.setLocation(330,216);
		status0.setSize(100,50);
		add(status0);
		status0.setForeground(Color.BLUE);
		
		
		
		
		dataMain =new ArrayList<PointsContainer>();
		loadFromFile();
		//A button to analyze the points and show result
		button=new JButton("Analyse");
		button.setSize(100,50);
		button.setLocation(100,200);
		button.addActionListener(
		new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {	
				
	         	analyse_points();
			}
	});
		add(button);
		result=new JLabel();
		result.setSize(500,50);
		result.setLocation(100,450);
		add(result);
		//Now comes a button to invoke feed mechanism
		field=new JTextField();
		field.setLocation(100,100);
		field.setSize(100,50);
		add(field);
		button2=new JButton("Feed");
		button2.setSize(100,50);
		button2.setLocation(200,100);
		button2.addActionListener(
		new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				makeDbase();
			}
	});
		add(button2);
		
		button3=new JButton("Write data to File");
		button3.setSize(200,50);
		button3.setLocation(300,100);
		button3.addActionListener(
		new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				feedToFile();
			}
	});
		add(button3);
		
		button4=new JButton("Clear");
		button4.setSize(100,50);
		button4.setLocation(200,200);
		button4.addActionListener(
		new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				panel.clear();
			}
	});
		add(button4);
		
		//panel.addMouseMotionListener((MouseMotionListener) new listener());
		/*for(int i=0;i<500;i+=10){
			for(int j=0;j<500;j+=10){
				JPanel bu=new JPanel();
				bu.setBackground(Color.red);
			  panel.add(bu);
			  bu.setSize(10,10);
			  bu.setLocation(i,j);
			  
			 
			  }
		}*/
	}//constructor ends here
	
	
	/*class listener implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent event) {
			JPanel e=(JPanel) event.getSource();
			JPanel k=(JPanel)e.getComponentAt(event.getX(),event.getY());
			
		   k.setBackground(Color.black);			
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

	
		
	}*/
	
	

public static void analyse_points(){
	ArrayList<Point> a=panel.getPoints();
	
	TreeMap<Float,String> map=new TreeMap<Float,String>(new Word());
	
	for(PointsContainer pointC:dataMain){
	
		
		ArrayList<Point> b=pointC.getArray();
	
	//Collections.sort(a,new Word());
	int minm=0,maxm=0;
	if(a.size()<b.size()){
		minm=a.size();
		maxm=b.size();
	}
	else{minm=b.size();maxm=a.size();}
	
	
	float xmean=0,x2mean=0,a_b=0,anet2 = 0,bnet2 = 0,ymean=0,y2mean=0,c_d=0,a2net2=0,b2net2=0;
	for(int g=0;g<maxm;g++){
		if(g>=minm){
			try{xmean+=a.get(g).getX();ymean+=a.get(g).getY();}catch(IndexOutOfBoundsException e){xmean+=0;ymean+=0;}
			try{x2mean+=b.get(g).getX();y2mean+=b.get(g).getY();}catch(IndexOutOfBoundsException e){x2mean+=0;y2mean+=0;}
			continue;
		}
		xmean+=a.get(g).getX();
		x2mean+=b.get(g).getX();
		ymean+=a.get(g).getY();
		y2mean+=b.get(g).getY();
	}
	xmean=xmean/maxm;
	x2mean=x2mean/maxm;
	ymean=ymean/maxm;
	y2mean=y2mean/maxm;
	for(int g=0;g<maxm;g++){
		float ai,bi,a2,b2=0;
		
		if(g>=minm){
			try{ai=(float) (a.get(g).getX()-xmean);a2=(float) (a.get(g).getY()-ymean);}catch(IndexOutOfBoundsException e){ai=(float) (0-xmean);a2=(float) (0-ymean);}
			try{bi=(float) (b.get(g).getX()-x2mean);b2=(float) (b.get(g).getY()-y2mean);}catch(IndexOutOfBoundsException e){bi=(float) (0-x2mean);b2=(float) (0-y2mean);}
			continue;
		}
		ai=(float) (a.get(g).getX()-xmean);
		bi=(float) (b.get(g).getX()-x2mean);
		a2=(float) (a.get(g).getY()-ymean);
		b2=(float) (b.get(g).getY()-y2mean);
		a_b+=ai*bi;
		c_d+=a2*b2;
		anet2+=ai*ai;bnet2+=bi*bi;
		a2net2+=a2*a2;b2net2+=b2*b2;
	}
	float numerator1=a_b;
	float denominator1=(float) Math.sqrt(anet2*bnet2);
	float numerator2=c_d;
	float denominator2=(float) Math.sqrt(a2net2*b2net2);
	
	map.put(((numerator1/denominator1)+(numerator2/denominator2))/2, pointC.getChar());
	}
	
	System.out.println(map);
	for(Entry<Float, String> entry : map.entrySet()) {
		  String value = entry.getValue();
          result.setText("The figure resambles "+value);
          System.out.println("The figure resambles  "+value);
		  break;
		}
}

@SuppressWarnings("unchecked")
public static void loadFromFile(){
	
	ObjectInputStream input = null;
	try {
		input=new ObjectInputStream(new FileInputStream("C:\\Users\\Yugal\\Documents\\JAVA\\GestureInterpreter\\src\\Database.ser"));
	} 
	catch (FileNotFoundException e) {
		System.out.println("Create the Database First");
		STATUS="Create the Database First\n";
		status.setText(STATUS);
	} catch (IOException e) {
	}

	
		try {
			dataMain=(ArrayList<PointsContainer>) input.readObject();
			STATUS="Successfully Loaded From File\n";
			status.setText(STATUS);
			
		} catch(EOFException endOfFileException){

			}
		catch(IOException e){}catch(ClassNotFoundException c){}catch(NullPointerException n){}
	
	
}//end loadFromFile



public static void feedToFile(){
	
	
	try {
		output=new ObjectOutputStream(new FileOutputStream("C:\\Users\\Yugal\\Documents\\JAVA\\GestureInterpreter\\src\\Database.ser"));
		output.writeObject(dataMain);
		output.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}//output functionality ends here
	System.out.println("Data Written to File!");
	STATUS+="Data Written to File!\n";
	status.setText(STATUS);
	
	
}




public static void makeDbase(){
	
	dataMain.add(new PointsContainer(new ArrayList<Point>(panel.getPoints()),field.getText()));
	System.out.println("Done");
	STATUS+="Done\n";
	status.setText(STATUS);
	panel.clear();
}








public static void main(String args[]){
	mainGUI frame=new mainGUI();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	
}

}//end class mainGUI


class Word implements Comparator<Float>{

	public int compare(Float a,Float b) {
		
		if( a>b){
			return -1;
		}
		else{return 1;}
		}

	
	}//end comparator class
	
	
	
	