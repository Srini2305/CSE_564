import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PlottingArea extends JPanel {

    private int[][] points;
    //Collection<Integer> route = Collections.synchronizedCollection(new ArrayList<>());
    private int[] route;
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
        if(route!=null && points!=null) {
            g.setColor(Color.RED);
            for(int i=1;i<limit;i++){
                //Integer[] r = route.toArray(new Integer[0]);
                int[] p1 = points[route[i-1]-1];
                int[] p2 = points[route[i]-1];
                g.drawLine(p1[0],p1[1],p2[0],p2[1]);
            }
        }
    }

    public void setPoints(int[][] points) {
        this.points = points;
        repaint();
    }

    public void setRoute(int[] route) {
        this.route = route;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        repaint();
    }

}
