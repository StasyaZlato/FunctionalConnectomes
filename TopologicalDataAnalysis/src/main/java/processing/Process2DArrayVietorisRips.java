package processing;

import NPArray.TwoDimensionalArray;
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.metric.impl.ExplicitMetricSpace;
import edu.stanford.math.plex4.streams.impl.VietorisRipsStream;

public class Process2DArrayVietorisRips extends ProcessNDimensionalArray {

    VietorisRipsStream<Integer> stream;

    Process2DArrayVietorisRips(double maxFiltrationValue, int maxDimensions) {
        super(maxFiltrationValue, maxDimensions);
        this.type = "Vietoris-Rips";
    }

    @Override
    public void computeIntervals() {
        if (array == null) {
            throw new NullPointerException("Массив не считан!");
        }
        double[][] correlationMatrix = ((TwoDimensionalArray)array).getArray();

        ExplicitMetricSpace metricSpace = new ExplicitMetricSpace(correlationMatrix);

        stream = new VietorisRipsStream<>(metricSpace, maxFiltrationValue, maxDimensions);
        stream.finalizeStream();

        numberOfSimplexes = stream.getSize();

        AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getDefaultSimplicialAlgorithm(maxDimensions);

        intervals = algorithm.computeIntervals(stream);
    }

    @Override
    public String getBettiNumbers() {
        if (stream == null) {
            throw new NullPointerException("Вычисление чисел бетти невозможно: стриxssм не был создан.");
        }
        if (!stream.isFinalized()) {
            throw new NullPointerException("Невозможно вычислить числа бетти на незавершенном стриме.");
        }
        return intervals.getBettiNumbers();
    }
}
