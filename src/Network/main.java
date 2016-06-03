package Network;
import java.util.*;
import org.graphstream.*;
import org.junit.*;


public class main{
	private int Num_nodes;
	private int[][] traffic_demand;   	//bij
	private int[][] unit_cost;			//aij
	private String student_ID;
	private Integer[] d;
	public int get_node_num(){
		return Num_nodes;
	}
	public int get_traffic_demand(int i,int j){
		return traffic_demand[i][j];
	}
	public int get_unit_cost(int i,int j){
		return unit_cost[i][j];
	}
	

	public int[][] generate_traffic_demand(){   //bij
		repeat_student_ID();
		d = new Integer[30];
		traffic_demand = new int[30][30];
		for(int i = 0; i < 30; i++){
			d[i] = Character.getNumericValue(student_ID.charAt(i)) ;
		}
		for(int i = 0; i < 30; i++){
			for(int j = 0; j < 30; j++){
				traffic_demand[i][j] = Math.abs(d[i] - d[j]);
			}
		}
		return traffic_demand;
	}
	public int[][] generate_unit_cost(int i,int j){   //aij
		int k = generate_random_k();
		return unit_cost;
	}
	
	public void repeat_student_ID(){
		Scanner user_input = new Scanner(System.in);
		System.out.println("Enter 10-digit student ID:");
		student_ID = user_input.next();
		student_ID = student_ID + student_ID + student_ID;
		user_input.close();
		
		if(student_ID.length() != 30){
			throw new RuntimeException("student ID length not correct");
		} 
		System.out.println("student ID is: " + student_ID);
	}
	
	public static int generate_random_k(){
		int k = 0;
		
		return k;
	}
	
	public static void main(String[] args){
		main LBJ = new main();
		Scanner user_input = new Scanner(System.in);
		System.out.println("App Start.");
		System.out.println("Enter Number of Nodes");
		LBJ.Num_nodes = user_input.nextInt();
		LBJ.generate_traffic_demand();
		
		user_input.close();
		
		
	}
	
}
