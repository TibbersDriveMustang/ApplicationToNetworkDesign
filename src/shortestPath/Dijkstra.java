package shortestPath;
import java.util.*;

//Referenced from:
// http://www.sanfoundry.com/java-program-find-shortest-path-between-two-vertices-using-dijkstras-algorithm/

//Find the shortest path between source vertex and destination vertex
public class Dijkstra {
	private int distance[];
	private Set<Integer> settled;
	private Set<Integer> unsettled;
	private int number_of_nodes;
	private int adjancyMatrix[][];
	
	//constructor
	public Dijkstra(int number_of_nodes){
		this.number_of_nodes = number_of_nodes;
		this.distance = new int[number_of_nodes + 1];
		this.settled = new HashSet<Integer>();
		this.unsettled = new HashSet<Integer>();
		this.adjancyMatrix = new int[number_of_nodes + 1][number_of_nodes + 1];
	}
	//
	public void go(int adjancy_matrix[][], int source){
		int evaluationNode;
		for(int i = 1; i <= number_of_nodes; i++){
			for(int j = 1; j <= number_of_nodes; j++){
				this.adjancyMatrix[i][j] = adjancy_matrix[i][j];
			}
		}
		
		for(int i = 1; i <= number_of_nodes; i++){
			distance[i] = Integer.MAX_VALUE;
		}
		unsettled.add(source);
		distance[source] = 0;
		while(!unsettled.isEmpty()){
			evaluationNode = getNodeWithMinDistanceFromUnsettled();
			unsettled.remove(evaluationNode);
			settled.add(evaluationNode);
			evaluateNeighbours(evaluationNode);
		}
	}
	
	private int getNodeWithMinDistanceFromUnsettled(){
		int min;
		int node = 0;
		Iterator<Integer> iterator = unsettled.iterator();
		node = iterator.next();
		min = distance[node];
		for(int i = 1; i <= distance.length; i++){
			if(unsettled.contains(i)){
				if(distance[i] <= min){
					min = distance[i];
					node = i;
				}
			}
		}
		return node;
	}
	
	private void evaluateNeighbours(int evaluationNode){
		int edgeDistance = -1;
		int newDistance = -1;
		
		for(int destinationNode = 1; destinationNode <= number_of_nodes; destinationNode++){
			if(!settled.contains(destinationNode)){
				if(adjancyMatrix[evaluationNode][destinationNode] != Integer.MAX_VALUE){
					edgeDistance = adjancyMatrix[evaluationNode][destinationNode];
					newDistance = distance[evaluationNode] + edgeDistance;
					if(newDistance < distance[destinationNode]){
						distance[destinationNode] = newDistance;
					}
					unsettled.add(destinationNode);
				}
			}
		}
	}
	
	
}
