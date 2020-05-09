package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stanford.math.plex4.bottleneck.BottleneckDistance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TDAOneFileResponse implements Response {
    private String path;
    private LinkedHashMap<Integer, List<Interval>> intervals;
    private Integer numberOfSimplices;
    private String bettiNumbers;
    private double probability;
    private boolean patient;

    private boolean probIsComputed;

    private double distanceFromPatientMedoid;
    private double distanceFromContrMedoid;

    public TDAOneFileResponse() {
        intervals = new LinkedHashMap<>();
    }

    public void addInterval(Integer dimension, List<Interval> interval) {
        this.intervals.put(dimension, interval);
    }

    @JsonGetter("intervals")
    public LinkedHashMap<Integer, List<Interval>> getIntervals() {
        return intervals;
    }

    @JsonGetter("numberOfSimplices")
    public Integer getNumberOfSimplices() {
        return numberOfSimplices;
    }

    public void setNumberOfSimplices(Integer numberOfSimplices) {
        this.numberOfSimplices = numberOfSimplices;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonGetter("bettiNumbers")
    public String getBettiNumbers() {
        return bettiNumbers;
    }

    public void setBettiNumbers(String bettiNumbers) {
        this.bettiNumbers = bettiNumbers;
    }

    @JsonGetter("patient")
    public boolean getPatient() {
        return patient;
    }

    public void setPatient(boolean patient) {
        this.patient = patient;
    }

    @JsonGetter("probability")
    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @JsonIgnore
    public boolean getProbIsComputed() {
        return probIsComputed;
    }

    public void setProbIsComputed(boolean probIsComputed) {
        this.probIsComputed = probIsComputed;
    }

    public void calculateDistanceFromMedoids(TDAOneFileResponse medoidPatient, TDAOneFileResponse medoidContr) {
        distanceFromContrMedoid = calculateDistanceFromMedoids(medoidContr);
        distanceFromPatientMedoid = calculateDistanceFromMedoids(medoidPatient);
    }

    private double calculateDistanceFromMedoids(TDAOneFileResponse medoid) {
        double[][] intervalsCurrent = listToArray(this);
        double[][] intervalsMedoid = listToArray(medoid);

        double distance = 0;

        if (intervalsCurrent.length == 0 && intervalsMedoid.length == 0) {
            distance = 0;
        }

        try {
            distance = BottleneckDistance.computeBottleneckDistance(intervalsCurrent, intervalsMedoid);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Ошибка в библиотеке - встроенное ограничение на размер массива в алгоритме Форд-Фалкерсон, слишком большие входные данные. " +
                    "Будет установлено расстояние 0");
        }
        return distance;
    }

    @JsonIgnore
    public void isPatient(double patientsInControllerCluster, double contrInPatientsCluster, double patientsProb) {
        if (distanceFromPatientMedoid < distanceFromContrMedoid) {
            patient = true;
            probability = (1 - contrInPatientsCluster) * patientsProb / ((1 - contrInPatientsCluster) * patientsProb + contrInPatientsCluster * (1-patientsProb));
        }
        else {
            patient = false;
            probability = (1 - patientsInControllerCluster) * (1- patientsProb) / ((1 - patientsInControllerCluster) * (1-patientsProb) + patientsInControllerCluster * patientsProb);
        }
    }

    private double[][] listToArray(TDAOneFileResponse resp) {
        List<Interval> int1 = resp.getIntervals().getOrDefault(1, Collections.emptyList())
                .stream()
                .filter(el -> !el.isRightInfinite())
                .collect(Collectors.toList());

        double[][] intervals1 = new double[int1.size()][2];

        int k = 0;
        for (Interval in : int1) {
            intervals1[k][0] = in.getStart();
            intervals1[k][1] = in.getEnd();
            k++;
        }
        return intervals1;
    }

}
