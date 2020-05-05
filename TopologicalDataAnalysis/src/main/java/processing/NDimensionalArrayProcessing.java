package processing;

import NPArray.NDimensionalArray;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;

public abstract class NDimensionalArrayProcessing {
    protected String type;
    protected double maxFiltrationValue = 1;
    protected int maxDimensions = 3;
    protected BarcodeCollection<Double> intervals;
    protected NDimensionalArray array;
    int numberOfSimplexes;

    public NDimensionalArrayProcessing(double maxFiltrationValue, int maxDimensions) {
        this.maxFiltrationValue = maxFiltrationValue;
        this.maxDimensions = maxDimensions;
    }

    public abstract void computeIntervals();

    public abstract String getBettiNumbers();

    public void setArray(NDimensionalArray array) {
        this.array = array;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
