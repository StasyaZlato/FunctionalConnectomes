package processing;

import NPArray.NDimensionalArray;
import NPArray.TwoDimensionalArray;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;
import process.ProcessNpy;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class FileProcessing {
    public final String path;

    NDimensionalArray array;

    private String complexType = "Vietoris-Rips";
    private int maxDimensions = 3;
    private double maxFiltrationValue = 1;

    public FileProcessing(String path) {
        this.path = path;
        ProcessNpy process = new ProcessNpy(path);

        array = process.getArray();
    }

    public FileProcessing(String path, String complexType, int maxDimensions, double maxFiltrationValue) {
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
            TwoDArrayVietorisRipsProcessing vietorisRips = new TwoDArrayVietorisRipsProcessing(maxFiltrationValue, maxDimensions);
            vietorisRips.setArray(array);

            vietorisRips.computeIntervals();

            for (int i = 0; i < maxDimensions; i++) {
                List<pojo.Interval> pInterval = vietorisRips.intervals.getIntervalsAtDimension(i).stream().map(el -> {
                    if (el.isRightInfinite()) {
                        return new pojo.Interval(el.getStart(), Double.POSITIVE_INFINITY);
                    } else {
                        return new pojo.Interval(el.getStart(), el.getEnd());
                    }
                }).collect(Collectors.toList());

                currentResponse.addInterval(i, pInterval);
            }
            currentResponse.setPath(this.path);
            currentResponse.setNumberOfSimplices(vietorisRips.numberOfSimplexes);
            currentResponse.setBettiNumbers(vietorisRips.getBettiNumbers());
            return new TDAResponse(currentResponse);
        } else {
            return null;
        }
    }

    public TDAResponse process2DArray(boolean isPatient) {
        TDAOneFileResponse currentResponse = process2DArray().getOnlyTDAResponse();
        currentResponse.setPatient(isPatient);
        currentResponse.setProbability(1);
        currentResponse.setProbIsComputed(true);

        return new TDAResponse(currentResponse);
    }
}
