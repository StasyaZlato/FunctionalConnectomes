package pojo;

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

    public String getFilePath() {
        return filePath.get(0);
    }

    public boolean noFilePathGiven() {
        return filePath.isEmpty();
    }

    public void setFilePath(String filePath) {
        this.filePath.clear();
        this.filePath.add(filePath);
    }

    public boolean isOneFileAction() {
        return filePath.size() == 1;
    }

    public double getMaxFiltrationValue() {
        return maxFiltrationValue;
    }

    public int getMaxDimensions() {
        return maxDimensions;
    }

    public String getComplexType() {
        return complexType;
    }

    public void setComplexType(String complexType) {
        this.complexType = complexType;
    }

    public void setMaxDimensions(int maxDimensions) {
        this.maxDimensions = maxDimensions;
    }

    public void setMaxFiltrationValue(double maxFiltrationValue) {
        this.maxFiltrationValue = maxFiltrationValue;
    }

    public TDAResponse getResults() {
        return results;
    }

    public void setResults(TDAResponse results) {
        this.results = results;
    }
}
