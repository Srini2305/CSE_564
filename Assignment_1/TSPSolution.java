import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TSPSolution {

    public static void nearestNeighbor(int[] dist){
        int n = (int) Math.sqrt(dist.length);
        boolean[] visited = new boolean[n];
        String[] route = new String[n+1];
        int i=0, j=0, pos=1, cost=0, selected=0;
        int min = Integer.MAX_VALUE;
        visited[0] = true;
        route[0] = "City1";
        while(i<n && j<n){
            if(pos>=n){
                break;
            }
            if(i!=j && !visited[j] && dist[(n*i)+j]<min){
                min = dist[(n*i)+j];
                route[pos] = "City" + (j+1);
                selected = j;
            }
            j++;
            if(j==n){
                cost+=min;
                min = Integer.MAX_VALUE;
                visited[selected] = true;
                i = selected;
                j=0;
                pos++;
            }
        }
        i = selected;
        cost+=dist[(n*i)];
        route[pos] = "City1";
        displayOutput(cost, route);
    }

    public static void scheduleTask(String filename){
        final ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            final String finalFilename = filename;
            final Future<?> f = service.submit(() -> {
                long start = System.currentTimeMillis();
                int[] tsp = DataExtractor.parseData(finalFilename);
                System.out.println("Dummy Route Algorithm:");
                dummyRoute(tsp);
                System.out.println("Nearest Neighbor Algorithm:");
                nearestNeighbor(tsp);
                System.out.printf("Time taken: %d seconds %n", (System.currentTimeMillis()-start)/1000);
            });
            f.get(300, TimeUnit.SECONDS);
        } catch (final TimeoutException e) {
            service.shutdown();
            System.out.println("Time limit exceeded!");
            System.exit(1);
        } catch (final Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        } finally {
            service.shutdown();
        }
    }

    public static void dummyRoute(int[] dist){
        int n = (int) Math.sqrt(dist.length);
        int cost = 0;
        String[] route = new String[n+1];
        for(int i=0;i<n-1;i++){
            cost+=dist[(n*i)+i+1];
            route[i] = "City" + (i+1);
        }
        cost+=dist[n*(n-1)];
        route[n-1] = "City" + n;
        route[n] = "City1";
        displayOutput(cost, route);
    }

    public static void displayOutput(int cost, String[] route){
        System.out.println("Cost: " + cost);
        System.out.println("Route:");
        System.out.println(Arrays.toString(route));
    }

    public static void main(String[] args){
        String filename;
        if(args.length == 0){
            System.out.println("Enter a file name:");
            Scanner scanner = new Scanner(System.in);
            filename = scanner.next();
        } else{
            filename = args[0];
        }
        scheduleTask(filename);
    }
}