import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ButtonActionListener {

    private static final String INFO_TITLE = "Information";
    private DataExtractor dataExtractor;

    ButtonActionListener(){
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
            SolutionGUI.fileName = selectedFile.getPath();
        }
        String content = dataExtractor.readFile(SolutionGUI.fileName);
        dataExtractor.extractPoints(content);
        dataExtractor.normalizePoints();
        SolutionGUI.drawingPanel.setPoints(dataExtractor.getNormalizedPoints());
        SolutionGUI.limit = 2;
        SolutionGUI.computed = false;
    }

    private void onRun() {
        if(SolutionGUI.fileName==null){
            JOptionPane.showMessageDialog(null, "Open a file before running the solution.",
                    INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
            if(!SolutionGUI.computed){
                int n = Math.min(dataExtractor.getPoints().length / 10, 30);
                TSPSolution.attachThread(n, dataExtractor.getPoints());
                SolutionGUI.limit = 2;
                SolutionGUI.computed = true;
            }
            SolutionGUI.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    drawLines();
                }
            });
            SolutionGUI.thread.start();
        }
    }

    private void onSave(){
        JFileChooser fs = new JFileChooser();
        fs.setDialogTitle("Save a File");
        int result = fs.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            try {
                File fi = fs.getSelectedFile();
                FileOutputStream newFile = new FileOutputStream(fi.getPath());
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(newFile));
                int number = 1;
                for(int i = 0;i<dataExtractor.getPoints().length;i++){
                    bufferedWriter.write(number + " " + dataExtractor.getNormalizedPoints()[i][0]+ " "+
                            dataExtractor.getNormalizedPoints()[i][1]);
                    number++;
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onNew(){
        SolutionGUI.drawingPanel.setLimit(1);
        SolutionGUI.drawingPanel.setPoints(null);
        SolutionGUI.drawingPanel.setSyncRoute1(Collections.synchronizedCollection(new ArrayList<>()));
        SolutionGUI.drawingPanel.setSyncRoute2(Collections.synchronizedCollection(new ArrayList<>()));
        SolutionGUI.drawingPanel.setSyncRoute3(Collections.synchronizedCollection(new ArrayList<>()));
        for(Thread thread : TSPSolution.threadList)
            thread.stop();
        TSPSolution.threadList = new ArrayList<>();
        TSPSolution.routeList = new ArrayList<>();
        TSPSolution.costList = new ArrayList<>();
    }

    public void drawLines() {
        while(SolutionGUI.limit<=dataExtractor.getPoints().length+1) {
            SolutionGUI.drawingPanel.setLimit(SolutionGUI.limit);
            int[] min = TSPSolution.getMinThreeCost(SolutionGUI.limit);
            if(!TSPSolution.routeList.isEmpty()) {
                SolutionGUI.drawingPanel.syncRoute1 = TSPSolution.routeList.get(min[0]);
                SolutionGUI.drawingPanel.syncRoute2 = TSPSolution.routeList.get(min[1]);
                SolutionGUI.drawingPanel.syncRoute3 = TSPSolution.routeList.get(min[2]);
            }
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
