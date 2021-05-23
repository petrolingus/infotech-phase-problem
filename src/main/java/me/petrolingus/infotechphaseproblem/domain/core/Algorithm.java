package me.petrolingus.infotechphaseproblem.domain.core;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import me.petrolingus.infotechphaseproblem.domain.generator.DiscreteSignalGenerator;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Algorithm extends Service<Void> {

    private double[] signalData;
    private double[] amplitudeSpectrumData;
    private double[] phaseSpectrumData;

    private double[] signalN;

    private int samplesCount;
    private double samplingRate;

    public AreaChart<Number, Number> signalChart;
    public AreaChart<Number, Number> amplitudeSpectrumChart;
    public AreaChart<Number, Number> phaseSpectrumChart;

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

    public double[] getSignalN() {
        return signalN;
    }

    @Override
    protected Task<Void> createTask() {

        return new Task<>() {

            @Override
            protected Void call() {

                Random random = new Random();

                // Step 1
                Complex[] fft = new Complex[amplitudeSpectrumData.length];
                for (int i = 0; i < fft.length; i++) {
                    double a = amplitudeSpectrumData[i];
                    double phi = 2 * Math.PI * Math.random();
                    double re = a * Math.cos(phi);
                    double im = a * Math.sin(phi);
                    fft[i] = new Complex(re, im);
                }

                FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);

                while (!isCancelled()) {

                    // Step 2
                    Complex[] ifft = fastFourierTransformer.transform(fft, TransformType.INVERSE);
                    //System.out.println(Arrays.toString(ifft));
                    double[] s = new double[ifft.length];
                    for (int i = 0; i < s.length; i++) {
                        double re = ifft[i].getReal();
                        s[i] = (re < 0) ? 0 : re;
                    }

                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int i = 0; i < ifft.length; i++) {
                        series.getData().add(new XYChart.Data<>(i, s[i]));
                    }

                    Platform.runLater(() -> {
                        int size = signalChart.getData().size();
                        if (size < 2) {
                            signalChart.getData().add(series);
                        } else {
                            signalChart.getData().set(1, series);
                        }
                        signalN = Arrays.copyOf(s, s.length);
                    });

                    // Step 3
                    fft = fastFourierTransformer.transform(s, TransformType.FORWARD);
                    for (int i = 0; i < fft.length; i++) {
                        double a = amplitudeSpectrumData[i];
                        double phi = fft[i].getArgument();
                        double re = a * Math.cos(phi);
                        double im = a * Math.sin(phi);
                        fft[i] = new Complex(re, im);
                    }

                }



                return null;
            }
        };
    }


}
