package uk.ac.shef.dcs.jate.feature;

import org.apache.log4j.Logger;
import uk.ac.shef.dcs.jate.JATEException;
import uk.ac.shef.dcs.jate.JATEProperties;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by - on 25/02/2016.
 */
public class TermComponentIndexFBMaster extends AbstractFeatureBuilder {
    private static final Logger LOG = Logger.getLogger(TermComponentIndexFBMaster.class.getName());
    private List<String> candidates;
    public TermComponentIndexFBMaster(JATEProperties properties, List<String> candidates) {
        super(null, properties);
        this.candidates=candidates;
    }

    @Override
    public AbstractFeature build() throws JATEException {
        TermComponentIndex feature;

        int cores = properties.getMaxCPUCores();
        cores = cores == 0 ? 1 : cores;
        int maxPerThread = candidates.size() / cores;
        maxPerThread = getMaxPerThread(maxPerThread);

        LOG.info("Beginning building features (TermComponentIndex). Total terms=" + candidates.size() + ", cpu cores=" +
                cores + ", max per core=" + maxPerThread);
        TermComponentIndexFBWorker worker = new
                TermComponentIndexFBWorker(candidates, maxPerThread
                );
        ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
        feature = forkJoinPool.invoke(worker);
        StringBuilder sb = new StringBuilder("Complete building features. Total processed terms = " + feature.getComponentIndex().size());
        LOG.info(sb.toString());

        return feature;
    }

    private int getMaxPerThread(int maxPerThread) {
        if (maxPerThread < MIN_SEQUENTIAL_THRESHOLD) {
            maxPerThread = MIN_SEQUENTIAL_THRESHOLD;
        } else if (maxPerThread > MAX_SEQUENTIAL_THRESHOLD) {
            maxPerThread = MAX_SEQUENTIAL_THRESHOLD;
        }
        return maxPerThread;
    }
}
