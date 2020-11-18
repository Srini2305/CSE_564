import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TSPSolution {

    protected static int[] cost;

    public static int[] nearestNeighbor(int[][] pt, int start, boolean[] visited){
        int cost = Integer.MAX_VALUE;
        int neighbor = 0;
        for(int j=0;j<pt.length;j++){
            if(j!=start && !visited[j]){
                int dist = euclideanDistance(pt[start][0], pt[start][1], pt[j][0], pt[j][1]);
                if(dist<cost){
                    cost = dist;
                    neighbor = j;
                }
            }
        }
        visited[neighbor] = true;
        return new int[]{neighbor, cost};
    }

    public static int[] runTSP(int[][] points, int start) {
        int len = points.length;
        int[] route = new int[len+1];
        boolean[] visited = new boolean[len];
        cost = new int[len+1];
        route[0] = start;
        visited[start-1] = true;
        for(int i=1;i<len;i++){
            int[] neighbor = nearestNeighbor(points, route[i-1]-1, visited);
            route[i] = neighbor[0] + 1;
            cost[i] = cost[i-1] + neighbor[1];
        }
        route[len] = start;
        cost[len] = cost[len-1] + euclideanDistance(points[start-1][0],points[start-1][1],
                points[route[len-1]][0],points[route[len-1]][1]);
        return route;
    }

    public static int euclideanDistance(float x1, float y1, float x2, float y2){
        return (int) Math.round(Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2)));
    }

}