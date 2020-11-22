import java.util.List;

/**
 * Control.java - Class which represents Control component in the BlackBoard pattern
 * Class used to calculate the minimum distance amongst three traversal
 * @author Srinivasan Sundar, Chandan Yadav
 * @version 1.0
 */
public class Control implements Runnable {

    public void drawLines() {
        PlottingArea plottingArea = SolutionView.getDrawingPanel();
        while(SolutionView.getLimit()<= DataRepository.getInstance().getNormalizedPoints().length+1) {
            plottingArea.setLimit(SolutionView.getLimit());
            Control.selectRoute();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            DataRepository.getInstance().notifyDrawLines();
            SolutionView.setLimit(SolutionView.getLimit()+1);
        }
        SolutionView.setLimit(1);
    }

    public static void selectRoute(){
        int[] min = Control.getMinThreeCost(SolutionView.getLimit());
        DataRepository dataRepository = DataRepository.getInstance();
        if(!dataRepository.getRouteList().isEmpty()) {
            SolutionView.getDrawingPanel().setSyncRoute1(dataRepository.getRouteList().get(min[0]));
            SolutionView.getDrawingPanel().setSyncRoute2(dataRepository.getRouteList().get(min[1]));
            SolutionView.getDrawingPanel().setSyncRoute3(dataRepository.getRouteList().get(min[2]));
        }
    }

    public static int[] getMinThreeCost(int n){
        int firstMin = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        int[] res = new int[] {0,1,2};
        for(int k=0;k<3;k++) {
            List<List<Integer>> costList = DataRepository.getInstance().getCostList();
            for (int i = 0; i < costList.size(); i++) {
                if (costList.get(i).get(n-1) > firstMin && costList.get(i).get(n-1) < min) {
                    min = costList.get(i).get(n-1);
                    minIndex = i;
                }
            }
            res[k] = minIndex;
            firstMin = min;
            min = Integer.MAX_VALUE;
        }
        return res;
    }

    @Override
    public void run() {
        drawLines();
    }
}
