package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.types.CensusGroup;
import cse332.types.CornerFindingResult;
import cse332.types.MapCorners;

import java.util.concurrent.RecursiveTask;

/*
   1) This class will do the corner finding from version 1 in parallel for use in versions 2, 4, and 5
   2) SEQUENTIAL_CUTOFF refers to the maximum number of census groups that should be processed by a single parallel task
   3) The compute method returns a result of a MapCorners and an Integer.
        - The MapCorners will represent the extremes/bounds/corners of the entire land mass (latitude and longitude)
        - The Integer value should represent the total population contained inside the MapCorners
 */

public class CornerFindingTask extends RecursiveTask<CornerFindingResult> {
    public final int SEQUENTIAL_CUTOFF = 10000;
    CensusGroup[] censusGroups;
    private int lo, hi;

    public CornerFindingTask(CensusGroup[] censusGroups, int lo, int hi) {
        this.censusGroups = censusGroups;
        this.lo = lo;
        this.hi = hi;
    }

    // Returns a pair of MapCorners for the grid and Integer for the total population
    // Key = grid, Value = total population
    @Override
    protected CornerFindingResult compute() {
        if (hi - lo < SEQUENTIAL_CUTOFF){
            return sequentialCornerFinding(censusGroups, lo, hi);
        }
        int mid = lo + (hi-lo) / 2;
        CornerFindingTask left = new CornerFindingTask(censusGroups, lo, mid);
        CornerFindingTask right = new CornerFindingTask(censusGroups, mid, hi);
        left.fork();
        CornerFindingResult rRes = right.compute();
        CornerFindingResult lRes = left.join();
        MapCorners rMap = rRes.getMapCorners();
        rMap = rMap.encompass(lRes.getMapCorners());
        int total = rRes.getTotalPopulation() + lRes.getTotalPopulation();
        return new CornerFindingResult(rMap, total);
    }

    private CornerFindingResult sequentialCornerFinding(CensusGroup[] censusGroups, int lo, int hi) {
        MapCorners mapCorners = new MapCorners(censusGroups[lo]);
        int total = censusGroups[lo].population;
        int i = lo+1;
        while(i < hi){
            mapCorners = mapCorners.encompass(new MapCorners(censusGroups[i]));
            total += censusGroups[i].population;
            i++;
        }
        return new CornerFindingResult(mapCorners, total);
    }
}



