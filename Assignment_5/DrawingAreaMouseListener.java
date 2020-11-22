import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * DrawingAreaMouseListener.java - Class used for plotting points on mouse click.
 * @author Ashwin Srinivasan, Srinivasan Sundar, Chandan Yadav
 * @version 1.0
 */

public class DrawingAreaMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int[][] normalizedPoints = DataRepository.getInstance().getNormalizedPoints();
        int[][] points = new int[normalizedPoints.length+1][2];
        for(int i=0;i<normalizedPoints.length;i++){
            points[i][0] = normalizedPoints[i][0];
            points[i][1] = normalizedPoints[i][1];
        }
        points[points.length - 1][0] = x;
        points[points.length - 1][1] = y;
        SolutionView.getDrawingPanel().setPoints(points);
        DataRepository.getInstance().setNormalizedPoints(points);
        SolutionView.setComputed(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Does not do anything
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Does not do anything
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Does not do anything
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Does not do anything
    }
}
