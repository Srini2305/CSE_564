import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MenuActionListener {

    private static final String INFO_TITLE = "Information";
    private final DataExtractor dataExtractor;

    MenuActionListener(){
        dataExtractor = new DataExtractor();
        dataExtractor.addObserver(SolutionGUI.drawingPanel);
    }

    public void openActionPerformed() {
        SolutionGUI.openMenuItem.addActionListener(e -> onOpen());
    }

    public void newActionPerformed() {
        SolutionGUI.newMenuItem.addActionListener(e -> onNew());
    }

    public void runActionPerformed() {
        SolutionGUI.runMenuItem.addActionListener(e -> { onRun(); });
    }

    public void stopActionPerformed() {
        SolutionGUI.stopMenuItem.addActionListener(e -> {
            if(SolutionGUI.thread!=null)
                SolutionGUI.thread.stop();
        });
    }

    public void saveActionPerformed(){
        SolutionGUI.saveMenuItem.addActionListener(e -> onSave());
    }

    public void aboutActionPerformed() {
        SolutionGUI.aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "Ashwin Srinivasan\nChandan Yadav\nSrinivasan Sundar",
                INFO_TITLE, JOptionPane.INFORMATION_MESSAGE));
    }

    private void onOpen() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            SolutionGUI.setFileName(selectedFile.getPath());
        }
        String content = dataExtractor.readFile(SolutionGUI.fileName);
        dataExtractor.extractPoints(content);
        dataExtractor.normalizePoints();
        SolutionGUI.drawingPanel.setPoints(dataExtractor.getNormalizedPoints());
        SolutionGUI.setLimit(2);
        SolutionGUI.setComputed(false);
    }

    private void onRun() {
        if(SolutionGUI.fileName==null){
            JOptionPane.showMessageDialog(null, "Open a file before running the solution.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
            if(!SolutionGUI.computed){
                int n = Math.min(dataExtractor.getPoints().length / 10, 30);
                dataExtractor.attachThread(n);
                SolutionGUI.setLimit(2);
                SolutionGUI.setComputed(true);
            }
            SolutionGUI.setThread(new Thread(this::drawLines));
            SolutionGUI.thread.start();
        }
    }

    private void onSave(){
        JFileChooser fs = new JFileChooser();
        fs.setDialogTitle("Save a File");
        int result = fs.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
                File fi = fs.getSelectedFile();
                String fileName = fi.getPath();
                dataExtractor.saveFile(fileName);
        }
    }

    public void onNew(){
        SolutionGUI.drawingPanel.setLimit(1);
        SolutionGUI.drawingPanel.setPoints(null);
        SolutionGUI.drawingPanel.setSyncRoute1(Collections.synchronizedCollection(new ArrayList<>()));
        SolutionGUI.drawingPanel.setSyncRoute2(Collections.synchronizedCollection(new ArrayList<>()));
        SolutionGUI.drawingPanel.setSyncRoute3(Collections.synchronizedCollection(new ArrayList<>()));
        for(Thread thread : dataExtractor.getThreadList())
            thread.stop();
        dataExtractor.setThreadList(new ArrayList<>());
        dataExtractor.setRouteList(new ArrayList<>());
        dataExtractor.setCostList(new ArrayList<>());
    }

    public void drawLines() {
        while(SolutionGUI.limit<=dataExtractor.getPoints().length+1) {
            SolutionGUI.drawingPanel.setLimit(SolutionGUI.limit);
            int[] min = dataExtractor.getMinThreeCost(SolutionGUI.limit);
            if(!dataExtractor.getRouteList().isEmpty()) {
                SolutionGUI.drawingPanel.setSyncRoute1(dataExtractor.getRouteList().get(min[0]));
                SolutionGUI.drawingPanel.setSyncRoute2(dataExtractor.getRouteList().get(min[1]));
                SolutionGUI.drawingPanel.setSyncRoute3(dataExtractor.getRouteList().get(min[2]));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
            SolutionGUI.setLimit(SolutionGUI.getLimit()+1);
        }
        SolutionGUI.setLimit(1);
    }

}
