import java.util.ArrayList;
import java.util.List;

public class TSPSolution {

    public static List<Thread> threadList = new ArrayList<>();
    public static List<List<Integer>> routeList = new ArrayList<>();
    public static List<List<Integer>> costList = new ArrayList<>();

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

    public static void attachThread(int n){
        for(int i=0;i<n;i++){
            int s = 1+(i*(DataExtractor.points.length/n));
            List<Integer> route = new ArrayList<>();
            List<Integer> cost = new ArrayList<>();
            Thread thread = new Thread(() -> runTSP(s, route, cost));
            threadList.add(thread);
            routeList.add(route);
            costList.add(cost);
            thread.start();
        }
    }

    public static void runTSP(int start, List<Integer> syncRoute, List<Integer> cost1) {
        int[][] points = DataExtractor.points;
        int len = points.length;
        int[] route = new int[len+1];
        boolean[] visited = new boolean[len];
        cost1.add(0);
        route[0] = start;
        syncRoute.add(start);
        visited[start-1] = true;
        for(int i=1;i<len;i++){
            int[] neighbor = nearestNeighbor(points, route[i-1]-1, visited);
            route[i] = neighbor[0] + 1;
            syncRoute.add(neighbor[0]+1);
            cost1.add(cost1.get(i-1)+ neighbor[1]);
        }
        route[len] = start;
        syncRoute.add(start);
        cost1.add(cost1.get(len-1)+ euclideanDistance(points[start-1][0],points[start-1][1],
                points[route[len-1]][0],points[route[len-1]][1]));
    }

    public static int[] getMinThreeCost(int n){
        int firstMin = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        int[] res = new int[3];
        for(int k=0;k<3;k++) {
            for (int i = 0; i < costList.size(); i++) {
                if (costList.get(i).get(n) > firstMin && costList.get(i).get(n) < min) {
                    min = costList.get(i).get(n);
                    minIndex = i;
                }
            }
            res[k] = minIndex;
            firstMin = min;
            min = Integer.MAX_VALUE;
        }
        return res;
    }

    public static int euclideanDistance(float x1, float y1, float x2, float y2){
        return (int) Math.round(Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2)));
    }

}