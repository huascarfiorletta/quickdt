package quickdt.predictiveModels;

import quickdt.data.AbstractInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PredictiveModelBuilder that supports adding data to predictive models
 * The builder will keep track of the number of times the model has been built, and if the rebuild threshold is passed the model will be rebuilt
 * If the split node threshold is passed the leaves will be rebuilt
 */
public class WrappedUpdatablePredictiveModelBuilder<PM extends PredictiveModel> implements UpdatablePredictiveModelBuilder<PM> {
    protected List<AbstractInstance> trainingData;
    protected PM predictiveModel;
    private final UpdatablePredictiveModelBuilder updatablePredictiveModelBuilder;
    protected Integer rebuildThreshold;
    protected Integer splitNodeThreshold;
    protected int buildCount = 0;

    public WrappedUpdatablePredictiveModelBuilder(UpdatablePredictiveModelBuilder updatablePredictiveModelBuilder) {
        this(updatablePredictiveModelBuilder, null);
    }

    public WrappedUpdatablePredictiveModelBuilder(UpdatablePredictiveModelBuilder updatablePredictiveModelBuilder, PM predictiveModel) {
        this.updatablePredictiveModelBuilder = updatablePredictiveModelBuilder;
        this.predictiveModel = predictiveModel;
        updatablePredictiveModelBuilder.updatable(true);
    }

    public WrappedUpdatablePredictiveModelBuilder rebuildThreshold(Integer rebuildThreshold) {
        this.rebuildThreshold = rebuildThreshold;
        return this;
    }

    public WrappedUpdatablePredictiveModelBuilder splitNodeThreshold(Integer splitNodeThreshold) {
        this.splitNodeThreshold = splitNodeThreshold;
        return this;
    }

    public WrappedUpdatablePredictiveModelBuilder updatable(boolean updatable) {
        return this;
    }

    public PM buildPredictiveModel(Iterable<? extends AbstractInstance> newData) {
        if (rebuildThreshold != null || splitNodeThreshold != null) {
            buildCount++;
        }

        if (trainingData == null) {
            trainingData = new CopyOnWriteArrayList<>();
        }
        appendTrainingData(newData);

        //check if we want to build a new predictive model or update existing
        if (predictiveModel == null || (rebuildThreshold != null && rebuildThreshold != 0 && buildCount > rebuildThreshold)) {
            buildCount = 1;
            predictiveModel = (PM) buildUpdatablePredictiveModel(trainingData);
        } else {
            boolean splitNodes = splitNodeThreshold != null && splitNodeThreshold != 0 && buildCount % splitNodeThreshold == 0;
            updatePredictiveModel(predictiveModel, newData, trainingData, splitNodes);
        }

        return predictiveModel;
    }

    private PredictiveModel buildUpdatablePredictiveModel(List<AbstractInstance> trainingData) {
        return updatablePredictiveModelBuilder.buildPredictiveModel(trainingData);
    }

    private void appendTrainingData(Iterable<? extends AbstractInstance> newTrainingData) {
        int index = trainingData.size();
        List<AbstractInstance> dataList = new ArrayList<>();
        for(AbstractInstance data : newTrainingData) {
            data.index = index;
            index++;
            dataList.add(data);
        }
        //writing is expensive, do it all at once
        trainingData.addAll(dataList);
    }

    @Override
    public void updatePredictiveModel(PM predictiveModel, Iterable<? extends AbstractInstance> newData, List<? extends AbstractInstance> trainingData, boolean splitNodes) {
        updatablePredictiveModelBuilder.updatePredictiveModel(predictiveModel, newData, trainingData, splitNodes);
    }

    @Override
    public void stripData(PM predictiveModel) {
        updatablePredictiveModelBuilder.stripData(predictiveModel);
    }
}