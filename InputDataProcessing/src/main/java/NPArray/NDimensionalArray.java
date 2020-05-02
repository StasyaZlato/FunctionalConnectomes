package NPArray;

import java.util.ArrayList;

public abstract class NDimensionalArray {
    int dimensions;
    ArrayList<Integer> shape;

    public NDimensionalArray(ArrayList<Integer> shape) {
        this.shape = shape;
    }

    public ArrayList<Integer> getShape() {
        return shape;
    }

    public void setShape(ArrayList<Integer> shape) {
        this.shape = shape;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public abstract void print();
}
