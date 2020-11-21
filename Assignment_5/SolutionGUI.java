import javax.swing.*;

public class SolutionGUI extends JFrame {

    private MenuActionListener menuActionListener = new MenuActionListener();
    static JMenuItem runMenuItem = new JMenuItem("Run");
    static JMenuItem openMenuItem = new JMenuItem("Open");
    static JMenuItem stopMenuItem = new JMenuItem("Stop");
    static JMenuItem aboutMenuItem = new JMenuItem("About Team");
    static JMenuItem saveMenuItem = new JMenuItem("Save");
    static JMenuItem newMenuItem = new JMenuItem("New");
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
        fileMenu.add(saveMenuItem);
        JMenu ProjectMenu = new JMenu("Project");
        menuBar.add(ProjectMenu);
        ProjectMenu.add(newMenuItem);
        ProjectMenu.add(runMenuItem);
        ProjectMenu.add(stopMenuItem);
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
    }

    private void addButtonActionListener() {
        menuActionListener.openActionPerformed();
        menuActionListener.runActionPerformed();
        menuActionListener.newActionPerformed();
        menuActionListener.saveActionPerformed();
        menuActionListener.stopActionPerformed();
        menuActionListener.aboutActionPerformed();
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        SolutionGUI.fileName = fileName;
    }

    public static Thread getThread() {
        return thread;
    }

    public static void setThread(Thread thread) {
        SolutionGUI.thread = thread;
    }

    public static boolean isComputed() {
        return computed;
    }

    public static void setComputed(boolean computed) {
        SolutionGUI.computed = computed;
    }

    public static int getLimit() {
        return limit;
    }

    public static void setLimit(int limit) {
        SolutionGUI.limit = limit;
    }
}
