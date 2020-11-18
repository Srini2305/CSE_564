import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class DataExtractor {
    
    static int[][] points;
    static int[][] normalizedPoints;
    private static int fileLength = 0;

    public static String readFile(String fileName){
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

    public static void extractPoints(String content){
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
        DataExtractor.points = points;
    }

    public static void normalizePoints() {
        if(DataExtractor.points==null)
            return;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int[][] pts = new int[DataExtractor.points.length][2];
        for(int[] pt:DataExtractor.points){
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
            pts[i][0] = DataExtractor.points[i][0] - (minX - 50);
            pts[i][0] = (pts[i][0]*1250)/rangeX;
            pts[i][1] = DataExtractor.points[i][1] - (minY - 50);
            pts[i][1] = (pts[i][1]*750)/rangeY;
        }
        DataExtractor.normalizedPoints = pts;
    }

    public static int getFileLength() {
        return fileLength;
    }

    public static void setFileLength(int fileLength) {
        DataExtractor.fileLength = fileLength;
    }
}