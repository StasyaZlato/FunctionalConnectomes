import NPArray.TwoDimensionalArray;
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.impl.ExplicitMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;
import edu.stanford.math.plex4.visualization.BarcodeVisualizer;
import learning.ReadLearningData;
import process.ProcessNpy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TDAMain {
    public static void main(String[] args) throws IOException {
        int n = 10000;
        int d = 2;
        int numLandmarkPoints = 39;
        double maxDistance = 0.0001;

        ReadLearningData r = new ReadLearningData();

        ProcessNpy processor = new ProcessNpy(
                "/Users/nastya_iva/Documents/2019-2020/courseWorkPy/Connectomes_project/data/AD_patients_corr_mats/pat_0.npy");

//        double[][] points = PointCloudExamples.getRandomSpherePoints(n, d);
        double[][] points = ((TwoDimensionalArray)(processor.getArray())).getArray();
        double max = Arrays.stream(points[0])
                .max()
                .getAsDouble();
        System.out.println(max);
        ExplicitMetricSpace metricSpace = new ExplicitMetricSpace(points);

//        LandmarkSelector<Integer> landmarkSelector = new MaxMinLandmarkSelector<>(metricSpace, numLandmarkPoints);

//        LazyWitnessStream<Integer> stream = new LazyWitnessStream<>(metricSpace, landmarkSelector, d + 1, maxDistance, 20);
//        stream.finalizeStream();


        VietorisRipsStream<Integer> stream = new VietorisRipsStream<>(metricSpace, max, 4);



        stream.finalizeStream();

        System.out.println("Number of simpleces in complex: " + stream.getSize());

        AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getModularSimplicialAlgorithm(4, 2);

        BarcodeCollection<Double> intervals = algorithm.computeIntervals(stream);

        System.out.println(intervals.getBettiNumbers());
//        System.out.println(algorithm.computeIndexIntervals(stream));

        System.out.println("\nBarcodes for " + d + "-sphere:");
        System.out.println(intervals);

        List<Interval<Double>> i = intervals.getIntervalsAtDimension(2);

        BufferedImage bi = BarcodeVisualizer.drawBarcode(i, "ffff", max);
        File outputfile = new File("saved.png");
        ImageIO.write(bi, "png", outputfile);
    }
}
