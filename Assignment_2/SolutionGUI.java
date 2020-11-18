import javax.swing.*;
import java.awt.*;

public class SolutionGUI extends JFrame {

    static JButton runButton = new JButton("Run");
    static JButton openButton = new JButton("Open");
    static JTextField startingPoint = new JTextField("1");
    static PlottingArea drawingPanel= new PlottingArea();
    static JLabel distance = new JLabel();
    static JLabel iteration= new JLabel();
    static String fileName;
    static Thread thread;
    static boolean computed;
    static int limit = 2;
    static int start = 0;

    public SolutionGUI() {
        addPanels();
        addButtonActionListener();
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
    }

    public void addPanels(){
        JPanel contentPane = new JPanel();
        JPanel topPanel = new JPanel();
        JLabel distanceLabel = new JLabel("\tBest Distance: ");
        JLabel iterationLabel = new JLabel("\tIteration: ");
        JLabel startingLabel = new JLabel("\tStarting Point: ");
        startingPoint.setPreferredSize(new Dimension(200, 25));
        topPanel.add(openButton);
        topPanel.add(runButton);
        topPanel.add(distanceLabel);
        topPanel.add(distance);
        topPanel.add(iterationLabel);
        topPanel.add(iteration);
        topPanel.add(startingLabel);
        topPanel.add(startingPoint);
        contentPane.add(topPanel);
        contentPane.add(drawingPanel);
        setContentPane(contentPane);
    }

    private void addButtonActionListener() {
        ButtonActionListener.openButtonActionPerformed();
        ButtonActionListener.runButtonActionPerformed();
    }

}
