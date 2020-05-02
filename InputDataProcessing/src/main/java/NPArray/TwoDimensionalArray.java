package NPArray;

import java.util.ArrayList;

public class TwoDimensionalArray extends NDimensionalArray {

    private double[][] array;

    public TwoDimensionalArray(ArrayList<Integer> shape) throws ArrayShapeException {
        super(shape);
        dimensions = 2;
        if (shape.size() != 2) {
            throw new ArrayShapeException("Параметр shape массива не соответствует размерности");
        }

        array = new double[shape.get(0)][shape.get(1)];
    }

    public void setArray(double[][] array) {
        this.array = array;
    }

    public double[][] getArray() {
        return array;
    }

    public void setArrayCell(int i, int j, double value) {
        if (checkIndexes(i, j)) {
            array[i][j] = value;
        }
        else {
            throw new IndexOutOfBoundsException("Выход за пределы массива");
        }
    }

    private boolean checkIndexes(int i, int j) {
        return i >= 0 && i < shape.get(0) &&
                j >= 0 && j < shape.get(1);
    }

    @Override
    public void print() {
        System.out.println("2-dimensional array");
        System.out.print("[");
        for (int i = 0; i < shape.get(0); i++) {
            System.out.print("[");
            for (int j = 0; j < shape.get(1); j++) {
                System.out.printf("%.5f\t", array[i][j]);
            }
            System.out.print("]");
            System.out.println();
        }
        System.out.print("]");

    }
}
