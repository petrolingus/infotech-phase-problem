package me.petrolingus.infotechphaseproblem.domain;

import org.apache.commons.math3.analysis.function.Gaussian;

import java.util.List;

public class DiscreteSignalGenerator {

    private final int samplesCount;

    private final double samplingRate;

    public DiscreteSignalGenerator(int samplesCount, double samplingRate) {
        this.samplesCount = samplesCount;
        this.samplingRate = samplingRate;
    }

    public double[] generate(List<Gaussian> gaussianList) {

        double[] signal = new double[samplesCount];

        for (int i = 0; i < samplesCount; i++) {
            double x = samplingRate * i;
            for (Gaussian gaussian : gaussianList) {
                signal[i] += gaussian.value(x);
            }
        }

        return signal;
    }
}
