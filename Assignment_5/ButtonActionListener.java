import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        SolutionGUI.stopMenuItem.addActionListener(e -> { SolutionGUI.thread.stop(); });
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
        SolutionGUI.drawingPanel.setRoute(null);
        SolutionGUI.drawingPanel.setPoints(DataExtractor.normalizedPoints);
        SolutionGUI.limit = 2;
        SolutionGUI.computed = false;
    }

    private static void onRun() {
        if(SolutionGUI.fileName==null){
            JOptionPane.showMessageDialog(null, "Open a file before running the solution.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else if(validStartingPoint()){
            int start = Integer.parseInt(SolutionGUI.startingPoint.getText());
            if(start!=SolutionGUI.start || !SolutionGUI.computed){
                int[] route = TSPSolution.runTSP(DataExtractor.points, start);
                SolutionGUI.start = start;
                SolutionGUI.drawingPanel.setRoute(route);
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
        SolutionGUI.drawingPanel.setLimit(2);
        SolutionGUI.drawingPanel.setPoints(null);
    }

    public static boolean validStartingPoint(){
        int start = 1;
        try {
            start = Integer.parseInt(SolutionGUI.startingPoint.getText());
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter a valid number as Starting point.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if(start<1 || start>DataExtractor.points.length){
            JOptionPane.showMessageDialog(null,
                    "Enter a valid number from 1-"+DataExtractor.points.length+" as Starting point.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public static void drawLines() {
        while(SolutionGUI.limit<=DataExtractor.points.length+1) {
            SolutionGUI.drawingPanel.setLimit(SolutionGUI.limit);
            SolutionGUI.distance.setText(""+TSPSolution.cost[SolutionGUI.limit-1]);
            SolutionGUI.iteration.setText(""+(SolutionGUI.limit-1));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
            SolutionGUI.limit++;
        }
        SolutionGUI.limit = 2;
    }

}
