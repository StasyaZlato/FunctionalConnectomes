package processing;

import NPArray.*;
import process.ProcessNpy;

public class ProcessFile {
    public final String path;

    NDimensionalArray array;

    public ProcessFile(String path) {
        this.path = path;
        ProcessNpy process = new ProcessNpy(path);

        array = process.getArray();
    }

    public TwoDimensionalArray get2DArray() {
        return (TwoDimensionalArray) array;
    }
}
