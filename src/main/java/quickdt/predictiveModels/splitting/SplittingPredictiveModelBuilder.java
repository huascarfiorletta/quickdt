package quickdt.predictiveModels.splitting;

import quickdt.data.AbstractInstance;
import quickdt.predictiveModels.PredictiveModelBuilder;
import quickdt.predictiveModels.randomForest.RandomForestBuilder;

/**
 * Created by ian on 4/1/14.
 */
public class SplittingPredictiveModelBuilder implements PredictiveModelBuilder<SplittingPredictiveModel> {
    private final String attributeToSplitOn;

    public SplittingPredictiveModelBuilder(String attributeToSplitOn) {
        this(attributeToSplitOn, new RandomForestBuilder());
    }

    public SplittingPredictiveModelBuilder(String attributeToSplitOn, PredictiveModelBuilder<?> builder) {
        this.attributeToSplitOn = attributeToSplitOn;
    }

    @Override
    public SplittingPredictiveModel buildPredictiveModel(final Iterable<? extends AbstractInstance> trainingData) {
        return null;
    }
}
