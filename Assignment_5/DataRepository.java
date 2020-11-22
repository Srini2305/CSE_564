import java.io.*;
import java.util.*;

/**
 * DataRepository.java - Class which represents blackboard component in the BlackBoard pattern
 * used for data storage and data related operations.
 * @author Ashwin Srinivasan, Srinivasan Sundar, Chandan Yadav
 * @version 1.0
 */
public class DataRepository extends Observable {

    private  static DataRepository dataRepository;
    private int[][] points = new int[0][2];
    private int[][] normalizedPoints = new int[0][2];
    private int fileLength = 0;
    private Thread controlThread;
    private List<Thread> threadList = new ArrayList<>();
    private List<List<Integer>> routeList = new ArrayList<>();
    private List<List<Integer>> costList = new ArrayList<>();

    public static DataRepository getInstance(){
        if(dataRepository== null){
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    public String readFile(String fileName){
        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.trim().toUpperCase().startsWith("DIMENSION")){
                    String[] str = line.trim().split("\\s+");
                    setFileLength(Integer.parseInt(str[str.length-1]));
                }
                if (Character.isDigit(line.trim().charAt(0))) {
                    stringBuilder.append(line).append(" ");
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
        }
        return stringBuilder.toString();
    }

    public void saveFile(String fileName){
        try {
            FileOutputStream newFile = new FileOutputStream(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(newFile));
            int number = 1;
            for(int i = 0;i< normalizedPoints.length;i++){
                bufferedWriter.write(number + " " + normalizedPoints[i][0]+ " "+
                        normalizedPoints[i][1]);
                number++;
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extractPoints(String content){
        int[][] points = new int[getFileLength()][2];
        String[] str = content.trim().split("\\s+");
        for (int i=0, pos=0; i<str.length; i++, pos++){
            points[pos][0] = Math.round(Float.parseFloat(str[++i]));
            points[pos][1] = Math.round(Float.parseFloat(str[++i]));
        }
        Arrays.sort(points, (o1, o2) -> {
            if(o1[1]!=o2[1])
                return o1[1] - o2[1];
            else
                return o2[0]-o1[0];
        });
        this.points = points;
    }

    public void normalizePoints() {
        if(points==null)
            return;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int[][] pts = new int[points.length][2];
        for(int[] pt:points){
            if(pt[0]<minX)
                minX = pt[0];
            if(pt[1]<minY)
                minY = pt[1];
            if(pt[0]>maxX)
                maxX = pt[0];
            if(pt[1]>maxY)
                maxY = pt[1];
        }
        int rangeX = (maxX-minX);
        int rangeY = (maxY-minY);
        for(int i=0;i<pts.length;i++){
            pts[i][0] = points[i][0] - (minX - 50);
            pts[i][0] = (pts[i][0]*1250)/rangeX;
            pts[i][1] = points[i][1] - (minY - 50);
            pts[i][1] = (pts[i][1]*750)/rangeY;
        }
        setNormalizedPoints(pts);
    }

    public void attachThread(int n){
        for(int i=0;i<n;i++){
            int s = 1+(i*(normalizedPoints.length/n));
            List<Integer> route = new ArrayList<>();
            List<Integer> cost = new ArrayList<>();
            TSPKnowledgeSource tspKnowledgeSource = new TSPKnowledgeSource(normalizedPoints, s, route, cost);
            Thread thread = new Thread(tspKnowledgeSource);
            threadList.add(thread);
            routeList.add(route);
            costList.add(cost);
            thread.start();
        }
    }

    public void attachControlThread(){
        controlThread = new Thread(new Control());
        controlThread.start();
    }

    public void notifyDrawLines(){
        setChanged();
        notifyObservers();
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }

    public int[][] getNormalizedPoints() {
        return normalizedPoints;
    }

    public void setNormalizedPoints(int[][] normalizedPoints) {
        this.normalizedPoints = normalizedPoints;
        setChanged();
        notifyObservers();
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public List<Thread> getThreadList() {
        return threadList;
    }

    public void setThreadList(List<Thread> threadList) {
        this.threadList = threadList;
    }

    public List<List<Integer>> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<List<Integer>> routeList) {
        this.routeList = routeList;
    }

    public List<List<Integer>> getCostList() {
        return costList;
    }

    public void setCostList(List<List<Integer>> costList) {
        this.costList = costList;
    }

    public Thread getControlThread() {
        return controlThread;
    }
}