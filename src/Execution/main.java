package Execution;

import java.util.*;
import org.graphstream.*;
import org.junit.*;
//import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.apache.logging.log4j.*;
import shortestPath.*;
import Network.*;

public class main{
	private int Num_nodes;
	private int k;
	private int[][] traffic_demand;   	//bij, traffic demand from i to j and from j to i will be the same
	private int[][] unit_cost;			//aij
	private int[][] optCost;
	private String student_ID;
	private Graph graph;
	private Vertex[] vertices;
	private DijkstraSP dijkstraList;
	private Integer[] d;
	
	
	public int getNodeNum(){
		System.out.println("Node Number is: " + Num_nodes);
		return Num_nodes;
	}
	public String getStudentID(){
		System.out.println("student ID is: " + student_ID);
		return student_ID;
	}
	public int getTrafficDemand(int i,int j){
		return traffic_demand[i][j];
	}
	public void showTrafficDemand(){
		for(int i = 0; i < Num_nodes; i++){
			for(int j = 0; j < Num_nodes;j++){
				System.out.print(traffic_demand[i][j] + "	");
			}
			System.out.println("");
		}
	}
	public int getUnitCost(int i,int j){
		return unit_cost[i][j];
	}
	public void showUnitCost(){
		for(int i = 0; i < Num_nodes; i++){
			for(int j = 0; j < Num_nodes;j++){
				System.out.print(unit_cost[i][j] + "	");
			}
			System.out.println("");
		}
	}
	

	public int[][] setTrafficDemand(){   //bij
		repeatStudentID();
		d = new Integer[Num_nodes];
		traffic_demand = new int[Num_nodes][Num_nodes];
		for(int i = 0; i < Num_nodes; i++){
			d[i] = Character.getNumericValue(student_ID.charAt(i)) ;
		}
		for(int i = 0; i < Num_nodes; i++){
			for(int j = 0; j < Num_nodes; j++){
				traffic_demand[i][j] = Math.abs(d[i] - d[j]);
			}
		}
		return traffic_demand;
	}
	public int[][] setUnitCost(){   //aij   k = 3,4,5...15
		//////!!!!!!!
		int k = this.k;
		unit_cost = new int[Num_nodes][Num_nodes];
		for(int i = 0; i < Num_nodes; i++){
			int[] j = setUniqueRandomIndices(i,k);
			int j_index = 0;
			for(int index = 0; index < Num_nodes; index++){
				if(j_index < k && j[j_index] == index){
					j_index++;
					unit_cost[i][index] = 1;
				}
				else if(i != index){
					unit_cost[i][index] = 300;
				}
				else if(i == index)
					unit_cost[i][index] = 0; 
			}
		}
		return unit_cost;
	}
	
	public void repeatStudentID(){
		/*Scanner user_input = new Scanner(System.in);
		System.out.println("Enter 10-digit student ID:");
		student_ID = user_input.next();
		student_ID = student_ID + student_ID + student_ID;
		user_input.close();
		*/
		student_ID = "2021221137";
		student_ID = student_ID + student_ID + student_ID;
		if(student_ID.length() != 30){
			throw new RuntimeException("student ID length not correct");
		} 
	}
	
	public int[] setUniqueRandomIndices(int i,int k){
		List<Integer> table = new ArrayList<Integer>();
		int index = 0;
		do{
			int temp = (int)(Math.random() * Num_nodes);
			if(!table.contains(temp) && !(temp == i)){
				table.add(temp);
				index++;
			}
		}
		while(index < k);
		  //set is in increasing order
		Collections.sort(table);
		//System.out.println("min outging edges: ");
		//System.out.println(i +" : " + table.toString());
		int[] m = new int[k];
		for(int j = 0; j < k; j++){
			m[j] = table.get(j);
		}
		return m;
	}
	public void setNumOfNodes(){
		Scanner user_input = new Scanner(System.in);
		System.out.println("Enter Number of Nodes: ");
		this.Num_nodes = user_input.nextInt();
	}
		
	public void setK(){
		Scanner user_input = new Scanner(System.in);
		System.out.println("Enter Value of k ");
		this.k = user_input.nextInt();
	}
	//Generate Z[][], minimum cost matrix for individual vertex
	public int[][] setSinglePathCost(int vertex){
	    setDijkstra(vertex);		
	    for(int j = 0; j < this.Num_nodes; j++){
	    	this.optCost[vertex][j] = this.traffic_demand[vertex][j] * this.dijkstraList.getDistanceTo(vertices[j].getLabel());
	    }
	    return this.optCost;
	}
	
	public void initOptCost(){
		if(this.optCost == null)
			this.optCost = new int[this.Num_nodes][this.Num_nodes];
	}
	public int[][] setTotalCost(){
		initOptCost();
		for(int i = 0; i < 1; i++){
			setSinglePathCost(i);
		}
		return this.optCost;
	}
//	public void initDijkstraList(){
//		this.dijkstra = new List<DijkstraSP>();
//	}
	
	public void setDijkstra(int vertex){
		//if(this.dijkstra[vertex] == null)
		//if(vertices[vertex] != null)
			//System.out.println(vertices[vertex].getLabel());
		if(dijkstraList == null){
			this.dijkstraList = new DijkstraSP(graph,vertices[vertex].getLabel());
		}
		//this.dijkstraList.add(temp);

	}
	
	//Generate graph, vertices, edges
	public void setGraph(){
		this.graph = new Graph();
		vertices = new Vertex[this.Num_nodes];
	    for(int i = 0; i < vertices.length; i++){
	    	vertices[i] = new Vertex(i + "");
	    	graph.addVertex(vertices[i], true);
	    }
	    for(int i = 0; i < vertices.length; i++){
	    	for(int j = 0; j < vertices.length; j++){
	    		if(this.unit_cost[i][j] != 0){
	    			Edge temp = new Edge(vertices[i],vertices[j],this.unit_cost[i][j]);
	    			graph.addEdge(temp.getOne(), temp.getTwo(),temp.getWeight());
	    		}
	    	}
	    }
	}
	
	
	public static void main(String[] args){
		main LBJ = new main();
		//LBJ.inputNumOfNodes();
		LBJ.Num_nodes = 30;
		LBJ.setK();
		LBJ.setTrafficDemand();
		LBJ.setUnitCost();
		LBJ.getNodeNum();
		LBJ.showTrafficDemand();
		LBJ.showUnitCost();
	    LBJ.setGraph();
	    
	    LBJ.setTotalCost();
	    
	    //List<Vertex> Z1 = dijkstra0.getPathTo(vertices[5].getLabel());
	    //int minCost = LBJ.traffic_demand[0][0] * dijkstra0.getDistanceTo(vertices[0].getLabel());
	    //System.out.println("minCost :" + minCost);
	    /*	    Set<Edge> temp = graph.getEdges();
	    int totalWeight = 0;
	    for(int i = 0; i < Z.size() - 1; i ++){
	    	for(Edge temp2: temp){
	    		if(temp2.getOne() == Z.get(i) && temp2.getTwo() == Z.get(i + 1)){
	    			totalWeight +=temp2.getWeight();
	    		}
	    	}
	    }
*/
	    //System.out.println(dijkstra0.getDistanceTo(vertices[6].getLabel()));
	    //System.out.println(dijkstra0.getPathTo(vertices[6].getLabel()));
	}						
		
	
	
}
