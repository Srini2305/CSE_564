import javax.swing.*;

/**
 *Main.java - Main class for view creation
 * @author Srinivasan Sundar
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        SolutionView solutionView = new SolutionView();
        solutionView.setVisible(true);
        solutionView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
