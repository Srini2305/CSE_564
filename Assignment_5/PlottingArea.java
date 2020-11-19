import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PlottingArea extends JPanel {

    private int[][] points;
    Collection<Integer> syncRoute1 = Collections.synchronizedCollection(new ArrayList<>());
    Collection<Integer> syncRoute2 = Collections.synchronizedCollection(new ArrayList<>());
    Collection<Integer> syncRoute3 = Collections.synchronizedCollection(new ArrayList<>());
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
        drawRoute(syncRoute1, g, Color.RED);
        drawRoute(syncRoute2, g, Color.BLUE);
        drawRoute(syncRoute3, g, Color.GREEN);
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
        repaint();
    }

    public void setLimit(int limit) {
        this.limit = limit;
        repaint();
    }

}
