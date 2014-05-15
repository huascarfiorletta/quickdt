package quickdt.predictiveModels.calibratedPredictiveModel;

import quickdt.data.AbstractInstance;
import quickdt.predictiveModels.UpdatablePredictiveModelBuilder;


/**
 * Created by Chris on 5/14/2014.
 */
public class UpdatablePAVCalibratedPredictiveModelBuilder extends UpdatablePredictiveModelBuilder<CalibratedPredictiveModel> {
    private final PAVCalibratedPredictiveModelBuilder pavCalibratedPredictiveModelBuilder;

    public UpdatablePAVCalibratedPredictiveModelBuilder(PAVCalibratedPredictiveModelBuilder calibratedPredictiveModelBuilder) {
        this(calibratedPredictiveModelBuilder, null);
    }

    public UpdatablePAVCalibratedPredictiveModelBuilder(PAVCalibratedPredictiveModelBuilder calibratedPredictiveModelBuilder, Integer rebuildThreshold) {
        super(rebuildThreshold);
        this.pavCalibratedPredictiveModelBuilder = calibratedPredictiveModelBuilder;
    }

    @Override
    public CalibratedPredictiveModel buildUpdatablePredictiveModel(final Iterable<? extends AbstractInstance> trainingData) {
        return pavCalibratedPredictiveModelBuilder.buildPredictiveModel(trainingData);
    }

    @Override
    public void updatePredictiveModel(CalibratedPredictiveModel predictiveModel, Iterable<? extends AbstractInstance> trainingData) {
        pavCalibratedPredictiveModelBuilder.updatePredictiveModel(predictiveModel, trainingData);
    }

}