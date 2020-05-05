package main;

import javafx.concurrent.Task;
import pojo.TDAResponse;
import processing.FileProcessing;

import static main.CourseWorkMain.MainLaunch.data;

public class ProcessGroupOfFilesTask extends Task<TDAResponse> {
    @Override
    public TDAResponse call() {
        TDAResponse tdaResponse = new TDAResponse();
        this.updateProgress(0, data.getFilePath().size());
        int i = 0;
        for (String path : data.getFilePath()) {
            this.updateMessage(path);

            FileProcessing processFile = new FileProcessing(path,
                    data.getComplexType(),
                    data.getMaxDimensions(),
                    data.getMaxFiltrationValue());

            tdaResponse.addElement(processFile.process2DArray().getOnlyTDAResponse());

            i++;
            this.updateProgress(i, data.getFilePath().size());
        }
        return tdaResponse;
    }
}

