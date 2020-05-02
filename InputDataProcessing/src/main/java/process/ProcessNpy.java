package process;

import NPArray.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class ProcessNpy {
    Path fileP;
    ArrayList<Integer> shape = new ArrayList<>();
    private String magic;
    private byte majorV;
    private byte minorV;
    private NDimensionalArray array;
    private String header;

    public ProcessNpy(String path) {
        fileP = Paths.get(path);
        try {
            readByteArray();
        } catch (IOException | ArrayShapeException ex) {
            System.out.println("Sth went wrong :(");
        }
    }

    public NDimensionalArray getArray() {
        return array;
    }

    public ArrayList<Integer> getShape() {
        return shape;
    }

    public byte getMajorV() {
        return majorV;
    }

    public byte getMinorV() {
        return minorV;
    }

    public String getHeader() {
        return header;
    }

    public String getMagic() {
        return magic;
    }

    void readByteArray() throws IOException, ArrayShapeException {
        byte[] bytes = Files.readAllBytes(fileP);

        byte[] magic = new byte[6];
        int i = 0;

        for (; i < 6; i++) {
            magic[i] = bytes[i];
        }

        this.magic = new String(magic);

        majorV = bytes[i++];

        minorV = bytes[i++];

        ByteBuffer headerLengthb = ByteBuffer.allocate(2);
        headerLengthb.order(ByteOrder.LITTLE_ENDIAN);
        headerLengthb.put(bytes[i++]);
        headerLengthb.put(bytes[i++]);
        short headerLength = headerLengthb.getShort(0);

        byte[] headerb = new byte[headerLength];
        int j = i;
        for (; j < i + headerLength - 1; j++) {
            headerb[j - i] = bytes[j];
        }

        header = new String(headerb);

        header = header.replace('(', '[');
        header = header.replace(')', ']');

        JSONObject headerJ = new JSONObject(header);

        JSONArray shapeJ = (JSONArray) headerJ.get("shape");

        shapeJ.forEach(el -> shape.add((Integer) el));


        readNDimensionalArray(bytes, j + 1);

        System.out.printf("magic = %s\nmajorV = %d\nminorV = %d\nheaderLength = %d\nheader = \"%s\"\n",
                this.magic, majorV, minorV, headerLength, header);

        System.out.print("shape: ");
        for (int el : shape) {
            System.out.printf(" %d", el);
        }
    }

    double readNextDouble(byte[] bytes, int start) {

        byte[] bb = new byte[8];
        int b = 0;
        for (; b < 8; b++) {
            bb[b] = bytes[start + b];
        }
        reverse(bb);
        return ByteBuffer.wrap(bb).getDouble();
    }

    void read4DimensionalArray(byte[] bytes, int start) {
        for (int i = 0; i < shape.get(0); i++) {
            for (int j = 0; j < shape.get(1); j++) {
                for (int k = 0; k < shape.get(2); k++) {
                    for (int l = 0; l < shape.get(3); l++) {
                        double value = readNextDouble(bytes, start);
                        start += 8;
                        ((FourDimensionalArray) array).setArrayCell(i, j, k, l, value);
                    }
                }
            }
        }
    }

    void read3DimensionalArray(byte[] bytes, int start) {
        // datatype is >f8
        for (int i = 0; i < shape.get(0); i++) {
            for (int j = 0; j < shape.get(1); j++) {
                for (int k = 0; k < shape.get(2); k++) {
                    double value = readNextDouble(bytes, start);
                    start += 8;
                    ((ThreeDimensionalArray) array).setArrayCell(i, j, k, value);
                }
            }
        }
    }

    void read2DimensionalArray(byte[] bytes, int start) {
        // datatype is >f8
        for (int i = 0; i < shape.get(0); i++) {
            for (int j = 0; j < shape.get(1); j++) {
                double value = readNextDouble(bytes, start);
                start += 8;

                ((TwoDimensionalArray) array).setArrayCell(i, j, value);
            }
        }
    }

    void read1DimensionalArray(byte[] bytes, int start) {
        // datatype is >f8
        for (int i = 0; i < shape.get(0); i++) {
            double value = readNextDouble(bytes, start);
            start += 8;
            ((OneDimensionalArray) array).setArrayCell(i, value);
        }
    }

    void readNDimensionalArray(byte[] bytes, int start) throws ArrayShapeException {
        int dimensions = shape.size();

        switch (dimensions) {
            case 0:
            case 1:
                array = new OneDimensionalArray(shape);
                read1DimensionalArray(bytes, start);
                break;
            case 2:
                array = new TwoDimensionalArray(shape);
                read2DimensionalArray(bytes, start);
                break;
            case 3:
                array = new ThreeDimensionalArray(shape);
                read3DimensionalArray(bytes, start);
                break;
            case 4:
                array = new FourDimensionalArray(shape);
                read4DimensionalArray(bytes, start);
                break;
            default:
                throw new ArrayShapeException("Неверная размерность");
        }
    }


    private void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
}
