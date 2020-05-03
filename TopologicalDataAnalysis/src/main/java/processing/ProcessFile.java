package processing;

import NPArray.*;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;
import process.ProcessNpy;

import java.util.List;
import java.util.stream.Collectors;


public class ProcessFile {
    public final String path;

    NDimensionalArray array;

    private String complexType = "Vietoris-Rips";
    private int maxDimensions = 3;
    private double maxFiltrationValue = 1;

    public ProcessFile(String path) {
        this.path = path;
        ProcessNpy process = new ProcessNpy(path);

        array = process.getArray();
    }

    public ProcessFile(String path, String complexType, int maxDimensions, double maxFiltrationValue) {
        this(path);
        this.complexType = complexType;
        this.maxDimensions = maxDimensions;
        this.maxFiltrationValue = maxFiltrationValue;
    }

    public TwoDimensionalArray get2DArray() {
        return (TwoDimensionalArray) array;
    }

    public TDAResponse process2DArray() {
        TDAOneFileResponse currentResponse = new TDAOneFileResponse();

        if (complexType.equals("Vietoris-Rips")) {
            Process2DArrayVietorisRips vietorisRips = new Process2DArrayVietorisRips(maxFiltrationValue, maxDimensions);
            vietorisRips.setArray(array);

            vietorisRips.computeIntervals();

            for (int i = 0; i < maxDimensions; i++) {
                List<pojo.Interval> pInterval = vietorisRips.intervals.getIntervalsAtDimension(i).stream().map(el -> {
                    if (el.isRightInfinite()) {
                        return new pojo.Interval(el.getStart(), Double.POSITIVE_INFINITY);
                    }
                    else {
                        return new pojo.Interval(el.getStart(), el.getEnd());
                    }
                }).collect(Collectors.toList());

                currentResponse.addInterval(i, pInterval);
            }
            currentResponse.setPath(this.path);
            currentResponse.setNumberOfSimplices(vietorisRips.numberOfSimplexes);
            return new TDAResponse(currentResponse);
        }
        else {
            return null;
        }
    }
}
