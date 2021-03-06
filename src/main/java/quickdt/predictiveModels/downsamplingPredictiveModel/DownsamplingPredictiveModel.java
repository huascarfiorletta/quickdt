package quickdt.predictiveModels.downsamplingPredictiveModel;

import com.google.common.base.Preconditions;
import quickdt.data.Attributes;
import quickdt.predictiveModels.PredictiveModel;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by ian on 4/22/14.
 */
public class DownsamplingPredictiveModel implements PredictiveModel {
    private static final long serialVersionUID = -265699047882740160L;

    public final PredictiveModel wrappedPredictiveModel;
    private final Serializable majorityClassification;
    private final double dropProbability;

    public DownsamplingPredictiveModel(final PredictiveModel wrappedPredictiveModel, final Serializable majorityClassification, final double dropProbability) {
        this.wrappedPredictiveModel = wrappedPredictiveModel;
        this.majorityClassification = majorityClassification;
        this.dropProbability = dropProbability;
    }

    @Override
    public double getProbability(final Attributes attributes, final Serializable classification) {
        Preconditions.checkArgument(!classification.equals(majorityClassification), "Only requesting the probability of the minority classification is currently supported (requested %s)", classification);
        double uncorrectedProbability = wrappedPredictiveModel.getProbability(attributes, classification);
        return Utils.correctProbability(dropProbability, uncorrectedProbability);
    }

    /**
     * Unsupported at this time, will throw UnsupportedOperationException
     * @param attributes
     * @return
     */
    @Override
    public Map<Serializable, Double> getProbabilitiesByClassification(final Attributes attributes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dump(final PrintStream printStream) {
        printStream.println("Will correct for downsampling with drop probability "+dropProbability+" for majority classification "+majorityClassification);
        wrappedPredictiveModel.dump(printStream);
    }

    @Override
    public Serializable getClassificationByMaxProb(final Attributes attributes) {
        return wrappedPredictiveModel.getClassificationByMaxProb(attributes);
    }
}
