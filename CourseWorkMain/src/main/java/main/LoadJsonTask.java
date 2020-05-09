package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import pojo.ClusterizedTDAResponse;
import pojo.Response;
import pojo.TDAResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static main.CourseWorkMain.MainLaunch.data;

public class LoadJsonTask extends Task<Map<String, Response>> {

    private String pathToData;

    public LoadJsonTask(String path) {
        pathToData = path;
    }

    @Override
    protected Map<String, Response> call() throws Exception {

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(pathToData).listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());

        Map<String, Response> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();


        for (String f : files) {
            String json = new String(Files.readAllBytes(Paths.get(f)));

            if (f.contains("contr.json")) {
                TDAResponse contr = objectMapper.readValue(json, TDAResponse.class);
                map.put("contr", contr);
            }
            else if (f.contains("pat.json")) {
                TDAResponse pat = objectMapper.readValue(json, TDAResponse.class);
                map.put("pat", pat);
            }
            else if (f.contains("contrIntime.json")){
                TDAResponse contrIntime = objectMapper.readValue(json, TDAResponse.class);
                map.put("contrIntime", contrIntime);
            }
            else if (f.contains("patIntime.json")) {
                TDAResponse patIntime = objectMapper.readValue(json, TDAResponse.class);
                map.put("patIntime", patIntime);
            }
            else if (f.contains("clusters.json")) {
                ClusterizedTDAResponse clusters = objectMapper.readValue(json, ClusterizedTDAResponse.class);
                map.put("clusters", clusters);
            }
        }
        if (map.size() != 5) {
            return null;
        }
        return map;
    }
}
