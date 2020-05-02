package NPArray;

import java.util.ArrayList;

public class OneDimensionalArray extends NDimensionalArray {
    double[] array;

    public OneDimensionalArray(ArrayList<Integer> shape) throws ArrayShapeException {
        super(shape);
        dimensions = 1;
        if (shape.size() > 1) {
            throw new ArrayShapeException("Параметр shape массива не соответствует размерности");
        }

        if (shape.size() == 0) {
            array = new double[1];
        } else {
            array = new double[shape.get(0)];
        }
    }

    public double[] getArray() {
        return array;
    }

    public void setArray(double[] array) {
        this.array = array;
    }

    public void setArrayCell(int i, double value) {
        if (checkIndexes(i)) {
            array[i] = value;
        } else {
            throw new IndexOutOfBoundsException("Выход за пределы массива");
        }
    }

    private boolean checkIndexes(int i) {
        return i >= 0 && i < shape.get(0);
    }

    @Override
    public void print() {
        System.out.println("1-dimensional array");

        System.out.print("[");
        for (int i = 0; i < shape.get(0); i++) {
            System.out.printf("%.5f\t", array[i]);
        }
        System.out.print("]");
    }
}
