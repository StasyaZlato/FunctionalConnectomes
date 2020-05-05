package main;

import javafx.concurrent.Task;
import pojo.TDAResponse;
import processing.FileProcessing;

import static main.CourseWorkMain.MainLaunch.data;

public class ProcessOneFileTask extends Task<TDAResponse> {
    @Override
    public TDAResponse call() {
        FileProcessing processFile = new FileProcessing(data.getFilePath().get(0),
                data.getComplexType(),
                data.getMaxDimensions(),
                data.getMaxFiltrationValue());
        return processFile.process2DArray();
    }
}
