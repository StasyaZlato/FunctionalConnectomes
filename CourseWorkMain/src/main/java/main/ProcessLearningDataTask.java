package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import learning.ReadLearningData;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;
import processing.FileProcessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static main.CourseWorkMain.MainLaunch.data;

public class ProcessLearningDataTask extends Task<Map<String, TDAResponse>> {
    @Override
    protected Map<String, TDAResponse> call() throws IOException {
        String path = data.getLearningDataFolder();
        ReadLearningData reader = new ReadLearningData(path);

        Map<String, TDAResponse> map = new HashMap<>();

        int maxNum = reader.getSize();

        this.updateProgress(0, maxNum);

        TDAResponse contr = new TDAResponse();
        TDAResponse pat = new TDAResponse();
        TDAResponse contrIn = new TDAResponse();
        TDAResponse patIn = new TDAResponse();

        int i = 0;

        for (String readPath : reader.getControls()) {
            messageConstructor(readPath);

            process(contr, readPath, false);

            i++;
            this.updateProgress(i, maxNum);
        }

        for (String readPath : reader.getPatients()) {
            messageConstructor(readPath);

            process(pat, readPath, true);

            i++;
            this.updateProgress(i, maxNum);
        }

        for (String readPath : reader.getControlsIntime()) {
            messageConstructor(readPath);
            contrIn.addElement(new TDAOneFileResponse());
            i++;
            this.updateProgress(i, maxNum);
        }
        for (String readPath : reader.getPatientsIntime()) {
            messageConstructor(readPath);
            patIn.addElement(new TDAOneFileResponse());

            i++;
            this.updateProgress(i, maxNum);
        }

        byte[] json_contr = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(contr).getBytes();

        byte[] json_contrIn = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(contrIn).getBytes();

        byte[] json_pat = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(pat).getBytes();

        byte[] json_patIn = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(patIn).getBytes();

        Path pathToLearningData = Paths.get(path).resolve("learning");
        Path pathToSettings = pathToLearningData.resolve(String.format("%s_%d_%s",
                data.getComplexType(),
                data.getMaxDimensions(),
                String.valueOf(data.getMaxFiltrationValue()).replace(".", "_")));

        Files.createDirectories(pathToSettings);

        Files.write(pathToSettings.resolve("contr.json"), json_contr);
        Files.write(pathToSettings.resolve("pat.json"), json_pat);
        Files.write(pathToSettings.resolve("contrIntime.json"), json_contrIn);
        Files.write(pathToSettings.resolve("patIntime.json"), json_patIn);

        map.put("contr", contr);
        map.put("contrIntime", contrIn);
        map.put("pat", pat);
        map.put("patIntime", contrIn);

        this.updateMessage(pathToLearningData.toString());

        return map;
    }


    protected void messageConstructor(String message) {
        super.updateMessage(String.format("Обработка файла %s", message));
    }

    private void process(TDAResponse resp, String readPath, boolean isPatient) {
        this.updateMessage(readPath);

        FileProcessing processFile = new FileProcessing(readPath,
                data.getComplexType(),
                data.getMaxDimensions(),
                data.getMaxFiltrationValue());
        TDAOneFileResponse response = processFile.process2DArray().getOnlyTDAResponse();
        response.setProbIsComputed(true);
        response.setProbability(1);
        response.setPatient(isPatient);
        resp.addElement(response);
    }
}
