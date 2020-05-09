package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import learning.KMedoidsClusterization;
import pojo.ClusterizedTDAResponse;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static main.CourseWorkMain.MainLaunch.data;

public class ClusterizationTask extends Task<ClusterizedTDAResponse> {


    @Override
    protected ClusterizedTDAResponse call() throws Exception {
        KMedoidsClusterization clusterization;

        Map<String, TDAResponse> map = data.getLearningData();
        
        ClusterizedTDAResponse response;
        
        TDAResponse learningData = new TDAResponse();
        
        for (TDAOneFileResponse r : map.get("contr").getTdaResponse()) {
            learningData.addElement(r);
        }
        
        for (TDAOneFileResponse r : map.get("pat").getTdaResponse()) {
            learningData.addElement(r);
        }

        clusterization = new KMedoidsClusterization(learningData.getTdaResponse(), data.getMaxDimensions(), 20);

        this.updateMessage("Начинается подсчет расстояний между диаграммами для дальнейшей кластеризации. Это может занять длительное время...");
        clusterization.computeDistances();

        this.updateMessage("Подсчет расстояний завершен, начинается кластеризация.");
        response = clusterization.kMedoid();

        this.updateMessage("Кластеризация завершена!");

        byte[] json = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(response).getBytes();

        String path = data.getLearningDataFolder();

        Path pathToLearningData = Paths.get(path).resolve("learning");
        Path pathToSettings = pathToLearningData.resolve(String.format("%s_%d_%s",
                data.getComplexType(),
                data.getMaxDimensions(),
                String.valueOf(data.getMaxFiltrationValue()).replace(".", "_")));

//        Files.createDirectories(pathToSettings);

        Files.write(pathToSettings.resolve("clusters.json"), json);

        this.updateMessage(pathToSettings.resolve("clusters.json").toString());

        return response;
    }
}
