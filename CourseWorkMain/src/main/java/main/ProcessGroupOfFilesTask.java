package main;

import javafx.concurrent.Task;
import pojo.TDAOneFileResponse;
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

            TDAOneFileResponse response = processFile.process2DArray().getOnlyTDAResponse();
            if (data.getClusters() != null) {
                response.calculateDistanceFromMedoids(data.getClusters().getPatientMedoid(), data.getClusters().getContrMedoid());
                response.isPatient(data.getClusters().getPatInContrCluster(), data.getClusters().getContrInPatientsCluster(),
                        ((double)(data.getLearningData().get("pat").getTdaResponse().size())) / (data.getLearningData().get("pat").getTdaResponse().size()
                                + data.getLearningData().get("contr").getTdaResponse().size()));
            }
            else {
                this.updateMessage("Невозможно посчитать вероятность, так как кластеризация с текущими настройками не была проведена.");
            }
            tdaResponse.addElement(response);

            i++;
            this.updateProgress(i, data.getFilePath().size());
        }
        return tdaResponse;
    }
}

