package NPArray;

import java.util.ArrayList;

public class ThreeDimensionalArray extends NDimensionalArray {

    private double[][][] array;

    public ThreeDimensionalArray(ArrayList<Integer> shape) throws ArrayShapeException {
        super(shape);
        dimensions = 3;
        if (shape.size() != 3) {
            throw new ArrayShapeException("Параметр shape массива не соответствует размерности");
        }

        array = new double[shape.get(0)][shape.get(1)][shape.get(2)];
    }

    public double[][][] getArray() {
        return array;
    }

    public void setArray(double[][][] array) {
        this.array = array;
    }

    public void setArrayCell(int i, int j, int k, double value) {
        if (checkIndexes(i, j, k)) {
            array[i][j][k] = value;
        } else {
            throw new IndexOutOfBoundsException("Выход за пределы массива");
        }
    }

    private boolean checkIndexes(int i, int j, int k) {
        return i >= 0 && i < shape.get(0) &&
                j >= 0 && j < shape.get(1) &&
                k >= 0 && k < shape.get(2);
    }

    @Override
    public void print() {
        System.out.println("3-dimensional array");
        System.out.print("[");
        for (int i = 0; i < shape.get(0); i++) {
            System.out.print("[");
            for (int j = 0; j < shape.get(1); j++) {
                System.out.print("[");
                for (int k = 0; k < shape.get(2); k++) {
                    System.out.printf("%.5f\t", array[i][j][k]);
                }
                System.out.print("]");
                System.out.println();

            }
            System.out.print("]");
            System.out.println();

        }
        System.out.print("]");
    }
}
