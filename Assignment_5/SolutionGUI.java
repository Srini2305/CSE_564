import javax.swing.*;
import java.awt.*;

public class SolutionGUI extends JFrame {

    static JMenuItem runMenuItem = new JMenuItem("Run");
    static JMenuItem openMenuItem = new JMenuItem("Open");
    static JMenuItem stopMenuItem = new JMenuItem("Stop");
    static JMenuItem aboutMenuItem = new JMenuItem("About Team");
    static PlottingArea drawingPanel= new PlottingArea();
    static String fileName;
    static Thread thread;
    static boolean computed;
    static int limit = 2;

    public SolutionGUI() {
        addPanels();
        addButtonActionListener();
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
    }

    public void addPanels(){
        JPanel contentPane = new JPanel();
        addMenu();
        contentPane.add(drawingPanel);
        setContentPane(contentPane);
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(openMenuItem);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        JMenu ProjectMenu = new JMenu("Project");
        menuBar.add(ProjectMenu);
        JMenuItem clearMenuItem = new JMenuItem("Clear");
        ProjectMenu.add(clearMenuItem);
        ProjectMenu.add(runMenuItem);
        ProjectMenu.add(stopMenuItem);
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
    }

    private void addButtonActionListener() {
        ButtonActionListener.openActionPerformed();
        ButtonActionListener.runActionPerformed();
        ButtonActionListener.stopActionPerformed();
        ButtonActionListener.aboutActionPerformed();
    }

}
