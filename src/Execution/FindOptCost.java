package Execution;

import java.util.*;
import java.lang.Exception;
import org.graphstream.*;
import org.junit.*;
import org.apache.commons.collections15.Transformer;
//import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.apache.logging.log4j.*;
import shortestPath.*;
import Network.*;
import Network.Graph;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.shortestpath.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.graph.*;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.JFrame;





public class FindOptCost{
	private int Num_nodes;
	private int k;
	private int[][] traffic_demand;   	//bij, traffic demand from i to j and from j to i will be the same
	private int[][] unit_cost;			//aij
	private int[][] optCost;
	private String student_ID;
	private int OptTotalCost;
	private int numDirectedEdge;
	private Network.Graph graph;
	private edu.uci.ics.jung.graph.Graph<Vertex, Edge> graph0;
	edu.uci.ics.jung.graph.UndirectedGraph<Vertex, Edge> graphToShow;
	private DijkstraShortestPath<Vertex,Edge>[] DPath;
	private Vertex[] vertices;
	//private V[] vertices0;
	private DijkstraSP[] dijkstraList;
	private Integer[] d;
	
	public FindOptCost(int Nums){
		this.Num_nodes = Nums;
		this.dijkstraList = new DijkstraSP[this.Num_nodes];
		this.DPath = new DijkstraShortestPath[this.Num_nodes];
		this.OptTotalCost = 0;
		this.numDirectedEdge = 0;
	}
	
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
		System.out.println("Traffic Demand Matrix b: ");
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
		System.out.println("Unit Cost Matrix a: ");
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
		int k = this.k;
		this.numDirectedEdge = 0;
		unit_cost = new int[Num_nodes][Num_nodes];
		for(int i = 0; i < Num_nodes; i++){
			int[] j = setUniqueRandomIndices(i,k);
			int j_index = 0;
			for(int index = 0; index < Num_nodes; index++){
				if(j_index < k && j[j_index] == index){
					j_index++;
					unit_cost[i][index] = 3;
					this.numDirectedEdge++;
				}
				else if(i != index){
					unit_cost[i][index] = 300;
					//this.numDirectedEdge++;
				}
				else if(i == index)
					unit_cost[i][index] = 0; 
			}
		}
		return unit_cost;
	}
	
	public void repeatStudentID(){
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
		
	public void setK(int k){
		this.graphToShow = new UndirectedSparseGraph();
		//Scanner user_input = new Scanner(System.in);
		//System.out.println("Enter Value of k ");
		//this.k = user_input.nextInt();
		if(k >= this.Num_nodes)
			throw new ArithmeticException("k value can not be equal or larger than number of node");
		this.k = k;
		System.out.println("=======  For k = " + k + "  =======");
	}
	
	public void initOptCost(){
		if(this.optCost == null)
			this.optCost = new int[this.Num_nodes][this.Num_nodes];
	}
	
	public void showOptCostMatrix(){
		System.out.println("Optimal Cost Matrix Z: ");
		for(int i = 0; i < this.Num_nodes; i++){
			for(int j = 0; j < this.Num_nodes; j++){
				String temp = String.format("%8s", this.optCost[i][j]);
				//System.out.print(this.optCost[i][j] + "");
				System.out.print(temp);
			}
			System.out.println("");
		}
	}
	
	public void setDijkstra(int vertex){
		if(dijkstraList[vertex] == null){
			this.dijkstraList[vertex] = new DijkstraSP(graph,vertices[vertex].getLabel());
		}
	    Transformer<Edge,Number> weightTransformer = new Transformer<Edge,Number>(){
	    	public Number transform(Edge i){
	    		return i.getWeight();
	    	}
	    };
		this.DPath[vertex] = new DijkstraShortestPath(graph0,weightTransformer);
	}
	
	//Generate Z[vertex][], minimum cost matrix for individual vertex
	public int[][] setSinglePathCost(int vertex){
	    setDijkstra(vertex);		
	    for(int j = 0; j < this.Num_nodes; j++){
	    	//optCost would be 0 is there is no traffic demand
	    	this.optCost[vertex][j] = this.traffic_demand[vertex][j] * this.dijkstraList[vertex].getDistanceTo(vertices[j].getLabel());
	    	System.out.println("Opt Distance from " + vertex + " to " + j + " is :" + this.dijkstraList[vertex].getDistanceTo(vertices[j].getLabel()));
			List<Vertex> temp = this.dijkstraList[vertex].getPathTo(vertices[j].getLabel());
			System.out.println("Opt Path from " + vertex + " to " + j + " is :" + temp);
	    	this.OptTotalCost = this.OptTotalCost + this.optCost[vertex][j];
	    }
	    return this.optCost;
	}
	public int[][] setTotalCostMatrix(){
		initOptCost();
		for(int i = 0; i < this.Num_nodes; i++){
			setSinglePathCost(i);
		}
		setOptGraph();
		return this.optCost;
	}
	
	public List<Vertex> getPath(int from, int to){
		List<Vertex> temp = this.dijkstraList[from].getPathTo(vertices[to].getLabel());
		System.out.println("Opt Path from " + from + " to " + to + " is :" + temp);
		return temp;
	}
	
	public int getDistance(int from, int to){
		int temp = this.dijkstraList[from].getDistanceTo(vertices[to].getLabel());
		System.out.println("Opt Distance from " + from + " to " + to + " is :" + temp);
		return temp;
	}
	
	public void setOptGraph(){
		for(int i = 0; i < this.Num_nodes; i++){
			for(int j = i + 1; j < this.Num_nodes;j++){
		    	List<Vertex> tempPath = this.dijkstraList[i].getPathTo(vertices[j].getLabel());
		    	for(int index = 0; index < tempPath.size() - 1; index++)
		    	{	Edge tempEdge = new Edge(tempPath.get(index), tempPath.get(index + 1),this.getUnitCost(tempPath.get(index).getIndex(), tempPath.get(index + 1).getIndex()));
		    		if(!this.graphToShow.containsEdge(tempEdge))
		    		{
		    			this.graphToShow.addEdge(tempEdge, tempPath.get(index), tempPath.get(index + 1), EdgeType.UNDIRECTED);
		    		}
		    	}
			}
		}
	}
	
	public int getTotalCost(){
		int total = 0;
		return total;
	}
	
	//Generate graph, vertices, edges
	public void setGraph(){
		this.graph = new Graph();
		vertices = new Vertex[this.Num_nodes];
	    for(int i = 0; i < vertices.length; i++){
	    	vertices[i] = new Vertex(i + "");
	    	graph.addVertex(vertices[i], true);
	    	//graph0.addVertex(vertices[i]);
	    	this.graphToShow.addVertex(vertices[i]);
	    }
	    for(int i = 0; i < vertices.length; i++){
	    	for(int j = 0; j < vertices.length; j++){
	    		if(this.unit_cost[i][j] != 0){
	    			Edge temp = new Edge(vertices[i],vertices[j],this.unit_cost[i][j]);
	    			graph.addEdge(temp.getOne(), temp.getTwo(),temp.getWeight());
	    			//graph0.a
	    			//if(temp.getWeight() < 300)	
	    			//	this.graphToShow.addEdge(temp, temp.getOne(), temp.getTwo(), EdgeType.UNDIRECTED);
	    		}
	    	}
	    }
	}
	
	public int getOptTotalCost(){
		System.out.println("Optimal Total Cost is : " + this.OptTotalCost);
		return this.OptTotalCost;
	}
	
	public float getDensity(){
		float density = 0;
		density = this.numDirectedEdge/this.Num_nodes * (this.Num_nodes - 1);
		System.out.println("Density is : " + density);
		return density;
	}
		
	public void getOptGraph(){
		CircleLayout temp = new CircleLayout(this.graphToShow);
		temp.setRadius(380);
		BasicVisualizationServer vs = new BasicVisualizationServer(temp,new Dimension(1000,800));
	    
	    Transformer<Vertex,Paint> vertexToPaint = new Transformer<Vertex,Paint>(){
	    	public Paint transform(Vertex i){
	    		return Color.red;
	    	}
	    };
	    
	    Transformer<Edge,Paint> EdgeToPaint = new Transformer<Edge,Paint>(){
	    	public Paint transform(Edge i){
	    		return Color.green;
	    	}
	    };
	    Transformer<Vertex,String> vertexToLabel = new Transformer<Vertex,String>(){
	    	public String transform(Vertex i){
	    		return i.getLabel();
	    	}
	    };
	    Transformer<Edge,String> edgeToLabel = new Transformer<Edge,String>(){
	    	public String transform(Edge i){
	    		return i.getWeight() + "";
	    	}
	    };
	    
	    vs.getRenderContext().setVertexDrawPaintTransformer(vertexToPaint);
	    //vs.getRenderContext().setEdgeFillPaintTransformer(EdgeToPaint);
	    vs.getRenderContext().setVertexLabelTransformer(vertexToLabel);
	    vs.getRenderContext().setEdgeLabelTransformer(edgeToLabel);
	    vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
	    
	    JFrame frame = new JFrame("Optimal Path Topology for k = " + this.k + " Total Cost: " + this.OptTotalCost + " Density: " + this.getDensity());
	    frame.getContentPane().add(vs);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	public void Run(int k){
		this.setK(k);
		this.setTrafficDemand();  //bij
		this.setUnitCost();		//aij
		//this.showTrafficDemand();
		//this.showUnitCost();
	    this.setGraph();
	    this.setTotalCostMatrix();
	    //this.showOptCostMatrix();
	    this.getOptTotalCost();
	    this.getDensity();
	    /*
	    Scanner user_input = new Scanner(System.in);
	    System.out.println("Enter Any to continue: ");
	    String temp = user_input.nextLine();
	    */
	}
	
	public static void main(String[] args){
		FindOptCost LBJ = new FindOptCost(5);
		
		LBJ.Run(2);
		LBJ.showTrafficDemand();
		LBJ.showUnitCost();
		LBJ.showOptCostMatrix();
		LBJ.getOptGraph();
		LBJ.getPath(0, 1);
		LBJ.getDistance(0, 1);
		/*
		LBJ.Run(4);
	    LBJ.Run(5);
	    LBJ.Run(6);
	    LBJ.Run(7);
	    LBJ.Run(8);
	    LBJ.Run(9);
		LBJ.showTrafficDemand();
		LBJ.showUnitCost();
		LBJ.showOptCostMatrix();
		LBJ.getOptGraph();
	    LBJ.Run(10);
	    LBJ.Run(11);
	    LBJ.Run(12);
	    LBJ.Run(13);
	    LBJ.Run(14);
	    LBJ.Run(15);
		LBJ.showTrafficDemand();
		LBJ.showUnitCost();
		LBJ.showOptCostMatrix();
		LBJ.getOptGraph();
		*/
	}									
}
