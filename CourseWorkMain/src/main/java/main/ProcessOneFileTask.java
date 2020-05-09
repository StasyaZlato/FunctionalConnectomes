package main;

import javafx.concurrent.Task;
import pojo.TDAOneFileResponse;
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
        TDAResponse response = processFile.process2DArray();
        if (data.getClusters() != null) {
            response.getOnlyTDAResponse().calculateDistanceFromMedoids(data.getClusters().getPatientMedoid(), data.getClusters().getContrMedoid());
            response.getOnlyTDAResponse().isPatient(data.getClusters().getPatInContrCluster(), data.getClusters().getContrInPatientsCluster(),
                    ((double)(data.getLearningData().get("pat").getTdaResponse().size())) / (data.getLearningData().get("pat").getTdaResponse().size()
                            + data.getLearningData().get("contr").getTdaResponse().size()));
        }
        else {
            this.updateMessage("Невозможно посчитать вероятность, так как кластеризация с текущими настройками не была проведена.");
        }
        return response;
    }
}
