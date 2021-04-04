package me.petrolingus.service;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import me.petrolingus.generator.SpectrumGenerator;
import me.petrolingus.math.FastFourierTransform;

import static javafx.collections.FXCollections.observableList;

public class MathService extends Service<Void> {

    public AreaChart<Number, Number> signalChart;
    public AreaChart<Number, Number> amplitudeSpectrumChart;
    public AreaChart<Number, Number> phaseSpectrumChart;

    private final SpectrumGenerator spectrumGenerator;

    public MathService(
            AreaChart<Number, Number> signalChart,
            AreaChart<Number, Number> amplitudeSpectrumChart,
            AreaChart<Number, Number> phaseSpectrumChart,
            SpectrumGenerator spectrumGenerator)
    {
        this.signalChart = signalChart;
        this.amplitudeSpectrumChart = amplitudeSpectrumChart;
        this.phaseSpectrumChart = phaseSpectrumChart;
        this.spectrumGenerator = spectrumGenerator;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {

                System.out.println("hellow");

                int i = 0;

                var randomPhaseSpectrum = SpectrumGenerator.generateRandomPhaseSpectrum();

                System.out.println("before runLater()");

                Platform.runLater(() -> {
                    phaseSpectrumChart.getData().add(new XYChart.Series<>(observableList(randomPhaseSpectrum)));
                });

                System.out.println("before while");

//                FastFourierTransform.ifft()

                while (!isCancelled()) {

                    if (i > 5) {
                        break;
                    } else {
                        System.out.println(i++);
                    }
                }

                return null;
            }
        };
    }
}
