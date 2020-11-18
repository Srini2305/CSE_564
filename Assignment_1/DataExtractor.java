import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataExtractor {

    private static int fileLength = 0;

    public static int[] parseData(String fileName){
        if(fileName.endsWith(".atsp")){
            String content = readFile("Asymmetric/" + fileName);
            return parseAsymmetric(content);
        } else if(fileName.endsWith(".tsp")){
            String content = readFile("Symmetric/" + fileName);
            return parseSymmetric(content);
        } else{
            System.out.println("File extension not supported!");
        }
        return new int[0];
    }

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

    public static int[] parseAsymmetric(String content){
        int[] tsp = new int[getFileLength() * getFileLength()];
        String[] str = content.trim().split("\\s+");
        for (int i=0;i<tsp.length && i<str.length;i++) {
            tsp[i] = Integer.parseInt(str[i]);
        }
        return tsp;
    }

    public static int[] parseSymmetric(String content){
        int[] tsp = new int[getFileLength() * getFileLength()];
        float[][] points = new float[getFileLength()][2];
        int pos = 0;
        String[] str = content.trim().split("\\s+");
        for (int i=0;i<tsp.length && i<str.length;i++) {
            points[pos][0] = Float.parseFloat(str[++i]);
            points[pos][1] = Float.parseFloat(str[++i]);
            pos++;
        }
        pos = 0;
        for(int i = 0; i< getFileLength(); i++){
            for(int j = 0; j< getFileLength(); j++){
                if(j==i) {
                    tsp[pos++] = Integer.MAX_VALUE;
                } else {
                    tsp[pos++] = euclideanDistance(points[i][0], points[i][1], points[j][0], points[j][1]);
                }
            }
        }
        return tsp;
    }

    public static int euclideanDistance(float x1, float y1, float x2, float y2){
        return (int) Math.round(Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2)));
    }

    public static int getFileLength() {
        return fileLength;
    }

    public static void setFileLength(int fileLength) {
        DataExtractor.fileLength = fileLength;
    }
}