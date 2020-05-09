package learning;

import edu.stanford.math.plex4.bottleneck.BottleneckDistance;
import pojo.ClusterizedTDAResponse;
import pojo.Interval;
import pojo.TDAOneFileResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KMedoidsClusterization {
    private static final Random random = new Random();
    List<List<Integer>> clusters;
    private List<TDAOneFileResponse> diagrams;
    private double[][] distances;
    private int maxDimensions;
    private int maxIterations;
    private int clusterD;
    private int[] medoidIds;
    private int[] assignment;
    private double currentDissimilarities;
    public String message;

    public KMedoidsClusterization(List<TDAOneFileResponse> diagrams, int maxDimensions, int maxIterations) {
        this.diagrams = diagrams;
        Collections.shuffle(this.diagrams);

        distances = new double[diagrams.size()][diagrams.size()];

        this.maxIterations = maxIterations;

        this.maxDimensions = maxDimensions;

        this.clusterD = 1;

        clusters = new ArrayList<>(2);
        clusters.add(new ArrayList<>());
        clusters.add(new ArrayList<>());

        medoidIds = new int[2];

        assignment = new int[diagrams.size()];

        for (int i = 0; i < diagrams.size(); i++) {
            for (int j = 0; j < diagrams.size(); j++) {
                distances[i][j] = -1;
            }
        }
    }

    private int getDimension() {
        int[] dimensionsCount = new int[maxDimensions];
        for (TDAOneFileResponse resp : diagrams) {
            int max = 0;
            int dim = 0;
            for (int i = 0; i < maxDimensions; i++) {
                int count = resp.getIntervals().getOrDefault(i, new ArrayList<>()).size();
                if (count > max) {
                    max = count;
                    dim = i;
                }
            }
            dimensionsCount[dim]++;
        }
        return getIndexOfMaxValue(dimensionsCount);
    }

    private int getIndexOfMaxValue(int[] ar) {
        int max = 0;
        int id = 0;
        for (int i = 0; i < ar.length; i++) {
            if (ar[i] >= max) {
                max = ar[i];
                id = i;
            }
        }
        return id;
    }

    private double[][] listToArray(int i) {
        List<Interval> int1 = diagrams.get(i).getIntervals().getOrDefault(clusterD, Collections.emptyList())
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

    public void computeDistances() {
        for (int i = 0; i < diagrams.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                    continue;
                }

                double[][] intervals1 = listToArray(i);
                double[][] intervals2 = listToArray(j);

                if (intervals1.length == 0 && intervals2.length == 0) {
                    distances[i][j] = 0;
                    continue;
                }

                double dist = 0;
                try {
                    dist = BottleneckDistance.computeBottleneckDistance(intervals1, intervals2);
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Ошибка в библиотеке - встроенное ограничение на размер массива в алгоритме Форд-Фалкерсон, слишком большие входные данные. " +
                            "Будет установлено расстояние 0");
                }
                distances[i][j] = dist;
                distances[j][i] = dist;

                message = String.format("Выполняется подсчет расстояний для кластеризации. Текущая позиция: i = %d, j = %d, dist = %f", i, j, dist);
                System.out.println(String.format("Compute d: i = %d, j = %d, dist = %f", i, j, dist));
            }
        }
    }

    public ClusterizedTDAResponse kMedoid() {
        // choose medoids randomly
        medoidIds[0] = random.nextInt(diagrams.size());
        medoidIds[1] = random.nextInt(diagrams.size());

        while (medoidIds[0] == medoidIds[1]) {
            medoidIds[1] = random.nextInt(diagrams.size());
        }

        boolean changed = true;

        int count = 0;
        while (changed && count < maxIterations) {
            count++;

            assignDataToMedoids();

            changed = recalculateMedoids();
        }

        return getClusterizedResponse();
    }

    private ClusterizedTDAResponse getClusterizedResponse() {
        ClusterizedTDAResponse response = new ClusterizedTDAResponse();

        int cluster1Patients = 0;
        int cluster1Contr = 0;

        int cluster2Patients = 0;
        int cluster2Contr = 0;

        for (int i = 0; i < diagrams.size(); i++) {
            if (assignment[i] == 0 && diagrams.get(i).getPatient()) {
                cluster1Patients++;
            }
            else if (assignment[i] == 0 && !diagrams.get(i).getPatient()) {
                cluster1Contr++;
            }
            else if (diagrams.get(i).getPatient()) {
                cluster2Patients++;
            }
            else {
                cluster2Contr++;
            }
        }

        if (cluster1Patients > cluster1Contr) {
            for (int i = 0; i < diagrams.size(); i++) {
                if (assignment[i] == 0) {
                    response.addElement(diagrams.get(i), true);
                } else {
                    response.addElement(diagrams.get(i), false);
                }
            }
            response.setContrInPatientsCluster((double)cluster1Contr / (cluster1Contr + cluster1Patients));
            response.setPatInContrCluster((double)cluster2Patients / (cluster2Contr + cluster2Patients));

            response.setPatientMedoid(diagrams.get(medoidIds[0]));
            response.setContrMedoid(diagrams.get(medoidIds[1]));

        }

        else {
            for (int i = 0; i < diagrams.size(); i++) {
                if (assignment[i] == 0) {
                    response.addElement(diagrams.get(i), false);
                } else {
                    response.addElement(diagrams.get(i), true);
                }
            }

            response.setContrInPatientsCluster((double)cluster2Contr / (cluster2Contr + cluster2Patients));
            response.setPatInContrCluster((double)cluster1Patients / (cluster1Contr + cluster1Patients));

            response.setPatientMedoid(diagrams.get(medoidIds[1]));
            response.setContrMedoid(diagrams.get(medoidIds[0]));
        }
        return response;
    }

    private void assignDataToMedoids() {
        currentDissimilarities = 0;

        for (int i = 0; i < diagrams.size(); i++) {
            double dist1, dist2;

            dist1 = computeDistance(medoidIds[0], i);
            dist2 = computeDistance(medoidIds[1], i);

            if (dist1 < dist2) {
                assignment[i] = 0;
                currentDissimilarities += dist1;
            } else {
                assignment[i] = 1;
                currentDissimilarities += dist2;
            }
            System.out.println(String.format("Assign: i = %d, dist1 = %f, dist2 = %f, medoid1 = %d, medoid2 = %d",
                    i, dist1, dist2, medoidIds[0], medoidIds[1]));
        }


    }

    private double computeDistance(int i, int j) {
        double[][] intervals1 = listToArray(i);
        double[][] intervals2 = listToArray(j);

        if (i == j) {
            distances[i][j] = 0;
            return 0;
        }

        if (intervals1.length == 0 && intervals2.length == 0) {
            distances[medoidIds[0]][i] = distances[i][medoidIds[0]] = 0;
            return 0;
        } else {
            double dist = distances[i][j];

            if (dist == -1) {
                dist = BottleneckDistance.computeBottleneckDistance(intervals1, intervals2);
                distances[j][i] = distances[i][j] = dist;
            }

            return dist;
        }
    }

    private double computeDissimilarityOnly() {
        double dissimilarities = 0;

        for (int i = 0; i < diagrams.size(); i++) {
            double dist1 = computeDistance(i, medoidIds[0]);
            double dist2 = computeDistance(i, medoidIds[1]);


            dissimilarities += Math.min(dist1, dist2);
        }

        return dissimilarities;
    }

    private boolean recalculateMedoids() {
        boolean changed = false;
        clusters.set(0, new ArrayList<>());
        clusters.set(1, new ArrayList<>());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < diagrams.size(); j++) {
                if (assignment[j] == i) {
                    clusters.get(i).add(j);
                }
            }

            for (int j = 0; j < clusters.get(i).size(); j++) {
                int oldMedoid = medoidIds[i];
                medoidIds[i] = clusters.get(i).get(j);
                double newDissimilarities = computeDissimilarityOnly();

                System.out.println(String.format("Recalculation: medoidId = %d, newMedoid = %d, oldMedoid = %d, newDiss = %f, oldDiss = %f", i, medoidIds[i], oldMedoid, newDissimilarities, currentDissimilarities));

                if (newDissimilarities < currentDissimilarities) {
                    currentDissimilarities = newDissimilarities;
                    changed = true;
                } else {
                    medoidIds[i] = oldMedoid;
                }
            }
        }
        return changed;
    }
}
