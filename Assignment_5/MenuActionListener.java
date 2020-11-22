import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * MenuActionListener.java - Class used to invoke action listeners of menu items on mouse click
 * @author Ashwin Srinivasan, Srinivasan Sundar
 * @version 1.0
 */
public class MenuActionListener {

    private static final String INFO_TITLE = "Team Information";

    MenuActionListener(){
        DataRepository.getInstance().addObserver(SolutionView.getDrawingPanel());
    }

    public void onOpen() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            SolutionView.setFileName(selectedFile.getPath());
        }
        String content = DataRepository.getInstance().readFile(SolutionView.getFileName());
        DataRepository.getInstance().extractPoints(content);
        DataRepository.getInstance().normalizePoints();
        SolutionView.getDrawingPanel().setPoints(DataRepository.getInstance().getNormalizedPoints());
        SolutionView.setLimit(2);
        SolutionView.setComputed(false);
    }

    public void onRun() {
        if(!SolutionView.isComputed()){
            int n = Math.min(DataRepository.getInstance().getNormalizedPoints().length / 10, 30);
            DataRepository.getInstance().attachThread(n);
            SolutionView.setLimit(2);
            SolutionView.setComputed(true);
        }
        DataRepository.getInstance().attachControlThread();
    }

    public void onSave(){
        JFileChooser fs = new JFileChooser();
        fs.setDialogTitle("Save a File");
        int result = fs.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File fi = fs.getSelectedFile();
            String fileName = fi.getPath();
            DataRepository.getInstance().saveFile(fileName);
        }
    }

    public void onNew(){
        PlottingArea plottingArea = SolutionView.getDrawingPanel();
        plottingArea.setLimit(1);
        plottingArea.setPoints(null);
        plottingArea.setSyncRoute1(Collections.synchronizedCollection(new ArrayList<>()));
        plottingArea.setSyncRoute2(Collections.synchronizedCollection(new ArrayList<>()));
        plottingArea.setSyncRoute3(Collections.synchronizedCollection(new ArrayList<>()));
        for(Thread thread : DataRepository.getInstance().getThreadList())
            thread.stop();
        DataRepository.getInstance().setThreadList(new ArrayList<>());
        DataRepository.getInstance().setRouteList(new ArrayList<>());
        DataRepository.getInstance().setCostList(new ArrayList<>());
        DataRepository.getInstance().setNormalizedPoints(new int[0][2]);
        DataRepository.getInstance().setPoints(new int[0][2]);
    }

    public void onStop(){
        if(DataRepository.getInstance().getControlThread()!=null)
            DataRepository.getInstance().getControlThread().stop();
    }

    public void onAboutTeam(){
        JOptionPane.showMessageDialog(null,
                "Ashwin Srinivasan\nChandan Yadav\nSrinivasan Sundar",
                INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

}
