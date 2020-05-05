package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    private double maxFiltrationValue = 1;
    private int maxDimensions = 3;
    private String complexType = "Vietoris-Rips";
    private List<String> filePath;

    private TDAResponse results;

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
}
