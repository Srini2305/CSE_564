import javax.swing.*;
import java.io.File;

public class ButtonActionListener {

    private static final String INFO_TITLE = "Information";

    public static void openButtonActionPerformed() {
        SolutionGUI.openButton.addActionListener(e -> onOpen());
    }

    public static void runButtonActionPerformed() {
        SolutionGUI.runButton.addActionListener(e -> {
            if(SolutionGUI.runButton.getText().equals("Run"))
                onRun();
            else {
                SolutionGUI.thread.stop();
                SolutionGUI.runButton.setText("Run");
            }
        });
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
            SolutionGUI.runButton.setText("Stop");
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
        SolutionGUI.runButton.setText("Run");
        SolutionGUI.limit = 2;
    }

}
