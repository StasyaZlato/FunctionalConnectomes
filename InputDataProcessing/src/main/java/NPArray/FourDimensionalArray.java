package NPArray;

import java.util.ArrayList;

public class FourDimensionalArray extends NDimensionalArray {
    double[][][][] array;

    public FourDimensionalArray(ArrayList<Integer> shape) throws ArrayShapeException {
        super(shape);
        dimensions = 4;
        if (shape.size() != 4) {
            throw new ArrayShapeException("Параметр shape массива не соответствует размерности");
        }

        array = new double[shape.get(0)][shape.get(1)][shape.get(2)][shape.get(3)];
    }

    public double[][][][] getArray() {
        return array;
    }

    public void setArray(double[][][][] array) {
        this.array = array;
    }

    public void setArrayCell(int i, int j, int k, int l, double value) {
        if (checkIndexes(i, j, k, l)) {
            array[i][j][k][l] = value;
        } else {
            throw new IndexOutOfBoundsException("Выход за пределы массива");
        }
    }

    private boolean checkIndexes(int i, int j, int k, int l) {
        return i >= 0 && i < shape.get(0) &&
                j >= 0 && j < shape.get(1) &&
                k >= 0 && k < shape.get(2) &&
                l >= 0 && l < shape.get(3);
    }

    @Override
    public void print() {
        // остановись подумой оно дофига долго выводится
        System.out.println("4-dimensional array");
        System.out.print("[");
        for (int i = 0; i < shape.get(0); i++) {
            System.out.print("[");
            for (int j = 0; j < shape.get(1); j++) {
                System.out.print("[");
                for (int k = 0; k < shape.get(2); k++) {
                    System.out.print("[");
                    for (int l = 0; l < shape.get(3); l++) {
                        System.out.printf("%.5f\t", array[i][j][k][l]);
                    }
                    System.out.print("]");
                    System.out.println();

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
