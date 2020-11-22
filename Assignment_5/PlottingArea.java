import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * PlottingArea.java - Class used for plotting the points in the panel
 * @author Ashwin Srinivasan, Srinivasan Sundar, Chandan Yadav
 * @version 1.0
 */

public class PlottingArea extends JPanel implements Observer {

    private int[][] points;
    private Collection<Integer> syncRoute1 = Collections.synchronizedCollection(new ArrayList<>());
    private Collection<Integer> syncRoute2 = Collections.synchronizedCollection(new ArrayList<>());
    private Collection<Integer> syncRoute3 = Collections.synchronizedCollection(new ArrayList<>());
    private int limit = 2;

    PlottingArea(){
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(1600, 850));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(points!=null) {
            g.setColor(Color.BLACK);
            for (int[] point : points) {
                g.drawOval(point[0], point[1], 2, 2);
                g.fillOval(point[0], point[1], 2, 2);
            }
        }
        if(points != null && limit<=points.length){
            drawRoute(syncRoute3, g, Color.GREEN);
            drawRoute(syncRoute2, g, Color.BLUE);
        }
        drawRoute(syncRoute1, g, Color.RED);
    }

    private void drawRoute(Collection<Integer> syncRoute, Graphics g, Color c) {
        if(!syncRoute.isEmpty() && points!=null) {
            g.setColor(c);
            for(int i=1;i<limit;i++){
                Integer[] r = syncRoute.toArray(new Integer[0]);
                int[] p1 = points[r[i-1]-1];
                int[] p2 = points[r[i]-1];
                g.drawLine(p1[0],p1[1],p2[0],p2[1]);
            }
        }
    }

    public void setPoints(int[][] points) {
        this.points = points;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Collection<Integer> getSyncRoute1() {
        return syncRoute1;
    }

    public void setSyncRoute1(Collection<Integer> syncRoute1) {
        this.syncRoute1 = syncRoute1;
    }

    public Collection<Integer> getSyncRoute2() {
        return syncRoute2;
    }

    public void setSyncRoute2(Collection<Integer> syncRoute2) {
        this.syncRoute2 = syncRoute2;
    }

    public Collection<Integer> getSyncRoute3() {
        return syncRoute3;
    }

    public void setSyncRoute3(Collection<Integer> syncRoute3) {
        this.syncRoute3 = syncRoute3;
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
