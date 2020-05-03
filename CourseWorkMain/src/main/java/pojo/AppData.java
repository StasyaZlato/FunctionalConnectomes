package pojo;

public class AppData {
    private double maxFiltrationValue;
    private int maxDimensions;
    private String complexType;

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
}
