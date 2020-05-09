package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {
    private double maxFiltrationValue = 1;
    private int maxDimensions = 3;
    private String complexType = "Vietoris-Rips";
    private List<String> filePath;
    private String learningDataFolder;

    private TDAResponse results;

    private Map<String, TDAResponse> learningData;

    private ClusterizedTDAResponse clusters;

    public AppData() {
        results = new TDAResponse();
        filePath = new ArrayList<>();
    }

    @JsonGetter("filePath")
    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.clear();
        this.filePath.add(filePath);
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    public boolean resultsCorrespondPaths() {
        if (filePath.size() != results.getTdaResponse().size()) {
            return false;
        }
        for (int i = 0; i < filePath.size(); i++) {
            if (!results.getTdaResponse().get(i).getPath().equals(filePath.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean noFilePathGiven() {
        return filePath.isEmpty();
    }

    public boolean isOneFileAction() {
        return filePath.size() == 1;
    }

    @JsonGetter("maxFiltrationValue")
    public double getMaxFiltrationValue() {
        return maxFiltrationValue;
    }

    public void setMaxFiltrationValue(double maxFiltrationValue) {
        this.maxFiltrationValue = maxFiltrationValue;
    }

    @JsonGetter("maxDimensions")
    public int getMaxDimensions() {
        return maxDimensions;
    }

    public void setMaxDimensions(int maxDimensions) {
        this.maxDimensions = maxDimensions;
    }

    @JsonGetter("complexType")
    public String getComplexType() {
        return complexType;
    }

    public void setComplexType(String complexType) {
        this.complexType = complexType;
    }

    @JsonGetter("results")
    public TDAResponse getResults() {
        return results;
    }

    public void setResults(TDAResponse results) {
        this.results = results;
    }

    public void setLearningDataFolder(String learningDataFolder) {
        this.learningDataFolder = learningDataFolder;
    }

    public String getLearningDataFolder() {
        return learningDataFolder;
    }

    public void setLearningData(Map<String, Response> learningData) {
        this.learningData = new HashMap<>();
        this.learningData.put("contr", (TDAResponse)learningData.get("contr"));
        this.learningData.put("pat", (TDAResponse)learningData.get("pat"));
        this.learningData.put("contrIntime", (TDAResponse)learningData.get("contrIntime"));
        this.learningData.put("patIntime", (TDAResponse)learningData.get("patIntime"));

        this.clusters = (ClusterizedTDAResponse)learningData.get("clusters");
    }

    public void setLearningData(Map<String, TDAResponse> learningData, boolean noClusters) {
        this.learningData = learningData;
    }

    @JsonIgnore
    public Map<String, TDAResponse> getLearningData() {
        return learningData;
    }

    @JsonIgnore
    public ClusterizedTDAResponse getClusters() {
        return clusters;
    }

    public void setClusters(ClusterizedTDAResponse clusters) {
        this.clusters = clusters;
    }
}
