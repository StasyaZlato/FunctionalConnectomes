import com.fasterxml.jackson.databind.ObjectMapper;
import learning.KMedoidsClusterization;
import pojo.ClusterizedTDAResponse;
import pojo.TDAOneFileResponse;
import pojo.TDAResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TDAMain {
    public static void main(String[] args) throws IOException {
//        int n = 10000;
//        int d = 2;
//        int numLandmarkPoints = 39;
//        double maxDistance = 0.0001;

//        ReadLearningData r = new ReadLearningData();
//
//        ProcessNpy processor = new ProcessNpy(
//                "/Users/nastya_iva/Documents/2019-2020/courseWorkPy/Connectomes_project/data/AD_controls_corr_mats/ctrl_8.npy");
//
//        System.out.println(Paths
//                .get("/Users/nastya_iva/Documents/2019-2020/courseWorkPy/Connectomes_project/data/AD_controls_corr_mats/ctrl_8.npy")
//                .getParent());
//
//        System.out.println(Paths.get("/Users/nastya_iva/Documents/2019-2020/courseWorkPy/Connectomes_project/data/AD_controls_corr_mats/ctrl_8.npy").getParent().resolve("learning"));
//
//        double[][] points = ((TwoDimensionalArray) (processor.getArray())).getArray();
//        ExplicitMetricSpace metricSpace = new ExplicitMetricSpace(points);
//
//        VietorisRipsStream<Integer> stream = new VietorisRipsStream<>(metricSpace, 1, 4);
//
//        stream.finalizeStream();
//
//        System.out.println("Number of simpleces in complex: " + stream.getSize());
//
//        AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getDefaultSimplicialAlgorithm(4);
//
//        BarcodeCollection<Double> intervals = algorithm.computeIntervals(stream);
//
//        System.out.println(intervals.getBettiNumbers());
//
//        System.out.println("\nBarcodes for " + d + "-sphere:");
//        System.out.println(intervals);
//
//        List<Interval<Double>> i = intervals.getIntervalsAtDimension(2);
//
//        BufferedImage bi = BarcodeVisualizer.drawBarcode(i, "ffff", 1);
//        File outputfile = new File("saved.png");
//        ImageIO.write(bi, "png", outputfile);

        String pathToData = "/Users/nastya_iva/Documents/2019-2020/data1/learning/Vietoris-Rips_3_1_0";

        List<String> files = Arrays.stream(Objects.requireNonNull(new File(pathToData).listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());

//        Map<String, TDAResponse> map = new HashMap<>();
        List<TDAOneFileResponse> response = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();


        for (String f : files) {
            String json = new String(Files.readAllBytes(Paths.get(f)));

            if (f.contains("contr.json")) {
                TDAResponse contr = objectMapper.readValue(json, TDAResponse.class);
                response.addAll(contr.getTdaResponse());
            } else if (f.contains("pat.json")) {
                TDAResponse pat = objectMapper.readValue(json, TDAResponse.class);
                response.addAll(pat.getTdaResponse());
            }
//            }
//            else if (f.contains("contrIntime.json")){
//                TDAResponse contrIntime = objectMapper.readValue(json, TDAResponse.class);
//                map.put("contrIntime", contrIntime);
//            }
//            else if (f.contains("patIntime.json")) {
//                TDAResponse patIntime = objectMapper.readValue(json, TDAResponse.class);
//                map.put("patIntime", patIntime);
//            }
        }

        KMedoidsClusterization clusterization = new KMedoidsClusterization(response, 3, 3);
        clusterization.computeDistances();

        ClusterizedTDAResponse resp = clusterization.kMedoid();

        System.out.println();

    }
}
