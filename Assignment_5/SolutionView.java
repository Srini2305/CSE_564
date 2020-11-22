import javax.swing.*;

public class SolutionView extends JFrame {

    private final MenuActionListener menuActionListener;
    private final JMenuItem runMenuItem;
    private final JMenuItem openMenuItem;
    private final JMenuItem stopMenuItem;
    private final JMenuItem aboutMenuItem;
    private final JMenuItem saveMenuItem;
    private final JMenuItem newMenuItem;
    private static PlottingArea drawingPanel = new PlottingArea();
    private static String fileName;
    private static Thread thread;
    private static boolean computed;
    private static int limit = 2;

    public SolutionView() {
        runMenuItem = new JMenuItem("Run");
        openMenuItem = new JMenuItem("Open");
        stopMenuItem = new JMenuItem("Stop");
        aboutMenuItem = new JMenuItem("About Team");
        saveMenuItem = new JMenuItem("Save");
        newMenuItem = new JMenuItem("New");
        addPanels();
        this.menuActionListener = new MenuActionListener();
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
        JMenu projectMenu = new JMenu("Project");
        menuBar.add(projectMenu);
        projectMenu.add(newMenuItem);
        projectMenu.add(runMenuItem);
        projectMenu.add(stopMenuItem);
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
    }

    private void addButtonActionListener() {
        openMenuItem.addActionListener(e -> menuActionListener.onOpen());
        saveMenuItem.addActionListener(e -> menuActionListener.onSave());
        newMenuItem.addActionListener(e -> menuActionListener.onNew());
        runMenuItem.addActionListener(e -> menuActionListener.onRun());
        stopMenuItem.addActionListener(e -> menuActionListener.onStop());
        aboutMenuItem.addActionListener(e -> menuActionListener.onAboutTeam());
        drawingPanel.addMouseListener(new DrawingAreaMouseListener());
    }

    public static String getFileName() {
        return SolutionView.fileName;
    }

    public static void setFileName(String fileName) {
        SolutionView.fileName = fileName;
    }

    public static Thread getThread() {
        return SolutionView.thread;
    }

    public static void setThread(Thread thread) {
        SolutionView.thread = thread;
    }

    public static boolean isComputed() {
        return SolutionView.computed;
    }

    public static void setComputed(boolean computed) {
        SolutionView.computed = computed;
    }

    public static int getLimit() {
        return SolutionView.limit;
    }

    public static void setLimit(int limit) {
        SolutionView.limit = limit;
    }

    public static PlottingArea getDrawingPanel() {
        return drawingPanel;
    }
}
