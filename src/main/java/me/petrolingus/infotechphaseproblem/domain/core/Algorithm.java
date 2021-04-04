package me.petrolingus.infotechphaseproblem.domain.core;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import me.petrolingus.infotechphaseproblem.domain.generator.DiscreteSignalGenerator;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.List;

public class Algorithm extends Service<XYChart.Series<Number, Number>> {

    private double[] signalData;
    private double[] amplitudeSpectrumData;
    private double[] phaseSpectrumData;

    private int samplesCount;
    private double samplingRate;

    public Algorithm(int samplesCount, double samplingRate) {
        this.samplesCount = samplesCount;
        this.samplingRate = samplingRate;
    }

    public void initialize() {

        Gaussian gaussian1 = new Gaussian(4, 150, 3);
        Gaussian gaussian2 = new Gaussian(2, 380, 2);
        Gaussian gaussian3 = new Gaussian(3.5, 600, 3);
        Gaussian gaussian4 = new Gaussian(2.5, 800, 2);
        Gaussian gaussian5 = new Gaussian(3.5, 920, 3);
        List<Gaussian> gaussianList = List.of(gaussian1, gaussian2, gaussian3, gaussian4, gaussian5);
        DiscreteSignalGenerator signalGenerator = new DiscreteSignalGenerator(samplesCount, samplingRate);
        signalData = signalGenerator.generate(gaussianList);

        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fft = fastFourierTransformer.transform(signalData, TransformType.FORWARD);

        // Creating amplitude spectrum data
        amplitudeSpectrumData = new double[fft.length];
        for (int i = 0; i < fft.length; i++) {
            amplitudeSpectrumData[i] = fft[i].abs();
        }

        // Creating phase spectrum data
        phaseSpectrumData = new double[fft.length];
        for (int i = 0; i < fft.length; i++) {
            phaseSpectrumData[i] = fft[i].getArgument();
        }
    }

    public double[] getSignalData() {
        return signalData;
    }

    public double[] getAmplitudeSpectrumData() {
        return amplitudeSpectrumData;
    }

    public double[] getPhaseSpectrumData() {
        return phaseSpectrumData;
    }

    @Override
    protected Task<XYChart.Series<Number, Number>> createTask() {

        return new Task<>() {

            @Override
            protected XYChart.Series<Number, Number> call() {

                XYChart.Series<Number, Number> series = new XYChart.Series<>();

                while (!isCancelled()) {

                }

                return series;
            }
        };
    }
}
