import javax.swing.*;
import java.io.*;

public class ButtonActionListener {

    private static final String INFO_TITLE = "Information";

    public static void openActionPerformed() {
        SolutionGUI.openMenuItem.addActionListener(e -> onOpen());
    }

    public static void newActionPerformed() {
        SolutionGUI.newMenuItem.addActionListener(e -> onNew());
    }

    public static void runActionPerformed() {
        SolutionGUI.runMenuItem.addActionListener(e -> { onRun(); });
    }

    public static void stopActionPerformed() {
        SolutionGUI.stopMenuItem.addActionListener(e -> {
            if(SolutionGUI.thread!=null)
                SolutionGUI.thread.stop();
        });
    }

    public static void saveActionPerformed(){
        SolutionGUI.saveMenuItem.addActionListener(e -> onSave());
    }

    public static void aboutActionPerformed() {
        SolutionGUI.aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "Ashwin Srinivasan\nChandan Yadav\nSrinivasan Sundar",
                INFO_TITLE, JOptionPane.INFORMATION_MESSAGE));
    }

    private static void onOpen() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            SolutionGUI.fileName = selectedFile.getPath();
        }
        String content = DataExtractor.readFile(SolutionGUI.fileName);
        DataExtractor.extractPoints(content);
        DataExtractor.normalizePoints();
        SolutionGUI.drawingPanel.setPoints(DataExtractor.normalizedPoints);
        SolutionGUI.limit = 2;
        SolutionGUI.computed = false;
    }

    private static void onRun() {
        if(SolutionGUI.fileName==null){
            JOptionPane.showMessageDialog(null, "Open a file before running the solution.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
            if(!SolutionGUI.computed){
                int n = Math.min(DataExtractor.points.length / 10, 30);
                TSPSolution.attachThread(n);
                SolutionGUI.limit = 2;
                SolutionGUI.computed = true;
            }
            SolutionGUI.thread = new Thread(ButtonActionListener::drawLines);
            SolutionGUI.thread.start();
        }
    }

    private static void onSave(){
        JFileChooser fs = new JFileChooser();
        fs.setDialogTitle("Save a File");
        int result = fs.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            try {
                File fi = fs.getSelectedFile();
                FileOutputStream newFile = new FileOutputStream(fi.getPath());
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(newFile));
                int number = 1;
                for(int i = 0;i<DataExtractor.points.length;i++){
                    bufferedWriter.write(number + " " + DataExtractor.normalizedPoints[i][0]+ " "+
                            DataExtractor.normalizedPoints[i][1]);
                    number++;
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onNew(){
        SolutionGUI.drawingPanel.setLimit(1);
        SolutionGUI.drawingPanel.setPoints(null);
    }

    public static void drawLines() {
        while(SolutionGUI.limit<=DataExtractor.points.length+1) {
            SolutionGUI.drawingPanel.setLimit(SolutionGUI.limit);
            int[] min = TSPSolution.getMinThreeCost(SolutionGUI.limit);
            SolutionGUI.drawingPanel.syncRoute1 = TSPSolution.routeList.get(min[0]);
            SolutionGUI.drawingPanel.syncRoute2 = TSPSolution.routeList.get(min[1]);
            SolutionGUI.drawingPanel.syncRoute3 = TSPSolution.routeList.get(min[2]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
            SolutionGUI.limit++;
        }
        SolutionGUI.limit = 1;
    }

}
