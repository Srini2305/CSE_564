import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ButtonActionListener {

    private static final String INFO_TITLE = "Information";

    public static void openActionPerformed() {
        SolutionGUI.openMenuItem.addActionListener(e -> onOpen());
    }

    public static void runActionPerformed() {
        SolutionGUI.runMenuItem.addActionListener(e -> { onRun(); });
    }

    public static void stopActionPerformed() {
        SolutionGUI.stopMenuItem.addActionListener(e -> { SolutionGUI.thread.stop(); });
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
        SolutionGUI.limit = 2;
    }

}
