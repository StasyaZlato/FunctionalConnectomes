package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class ClusterizedTDAResponse implements Response {
    private List<TDAOneFileResponse> patients;
    private List<TDAOneFileResponse> contr;

    private TDAOneFileResponse patientMedoid;
    private TDAOneFileResponse contrMedoid;

    private double patInContrCluster;
    private double contrInPatientsCluster;

    private int patientsClusterSize, contrClusterSize;

    public ClusterizedTDAResponse() {
        this.patients = new ArrayList<>();
        this.contr = new ArrayList<>();
    }

    public void addElement(TDAOneFileResponse fileResponse, boolean patient) {
        if (patient) {
            patients.add(fileResponse);
        } else {
            contr.add(fileResponse);
        }
    }

    @JsonGetter("patInContrCluster")
    public double getPatInContrCluster() {
        return patInContrCluster;
    }

    public void setPatInContrCluster(double patInContrCluster) {
        this.patInContrCluster = patInContrCluster;
    }

    @JsonGetter("contrInPatientsCluster")
    public double getContrInPatientsCluster() {
        return contrInPatientsCluster;
    }

    public void setContrInPatientsCluster(double contrInPatientsCluster) {
        this.contrInPatientsCluster = contrInPatientsCluster;
    }

    @JsonGetter("patients")
    public List<TDAOneFileResponse> getPatients() {
        return patients;
    }

    @JsonGetter("contr")
    public List<TDAOneFileResponse> getContr() {
        return contr;
    }

    @JsonGetter("contrMedoid")
    public TDAOneFileResponse getContrMedoid() {
        return contrMedoid;
    }

    public void setContrMedoid(TDAOneFileResponse contrMedoid) {
        this.contrMedoid = contrMedoid;
    }

    @JsonGetter("patientMedoid")
    public TDAOneFileResponse getPatientMedoid() {
        return patientMedoid;
    }

    public void setPatientMedoid(TDAOneFileResponse patientMedoid) {
        this.patientMedoid = patientMedoid;
    }

    @JsonGetter("contrClusterSize")
    public int getContrClusterSize() {
        contrClusterSize = contr.size();
        return contrClusterSize;
    }

    @JsonGetter("patientsClusterSize")
    public int getPatientsClusterSize() {
        patientsClusterSize = patients.size();
        return patientsClusterSize;
    }
}
