package me.petrolingus.controller;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import me.petrolingus.generator.SpectrumGenerator;
import me.petrolingus.generator.SignalGenerator;
import me.petrolingus.math.Complex;
import me.petrolingus.math.FastFourierTransform;
import me.petrolingus.service.MathService;

import java.util.ArrayList;
import java.util.Arrays;

import static javafx.collections.FXCollections.observableList;

public class Controller {

    public AreaChart<Number, Number> signalChart;
    public NumberAxis signalChartXAxis;

    public AreaChart<Number, Number> amplitudeSpectrumChart;
    public NumberAxis amplitudeSpectrumChartXAxis;

    public AreaChart<Number, Number> phaseSpectrumChart;
    public NumberAxis phaseSpectrumChartXAxis;

    public TextField amplitudeField1;
    public TextField meanField1;
    public TextField deviationField1;

    public TextField amplitudeField2;
    public TextField meanField2;
    public TextField deviationField2;

    public TextField amplitudeField3;
    public TextField meanField3;
    public TextField deviationField3;

    public TextField amplitudeField4;
    public TextField meanField4;
    public TextField deviationField4;

    public TextField amplitudeField5;
    public TextField meanField5;
    public TextField deviationField5;

    private final SignalGenerator signalGenerator = new SignalGenerator();
    private SpectrumGenerator spectrumGenerator;
    private MathService mathService;

    public void initialize() {

        axisInitialize(signalChartXAxis);
        axisInitialize(amplitudeSpectrumChartXAxis);
        axisInitialize(phaseSpectrumChartXAxis);

        createSignal();

        mathService = new MathService(signalChart, amplitudeSpectrumChart, phaseSpectrumChart, spectrumGenerator);
    }

    private void axisInitialize(NumberAxis axis) {

        axis.setAutoRanging(false);
        axis.setLowerBound(SignalGenerator.SIGNAL_BEGIN);
        axis.setUpperBound(SignalGenerator.SIGNAL_END);
        axis.setTickUnit(SignalGenerator.SIGNAL_TICK_UNIT);
    }

    private void createSignal() {

        ArrayList<XYChart.Data<Number, Number>> signal = signalGenerator.generate();
        signalChart.getData().add(new XYChart.Series<>(observableList(signal)));

        Complex[] in = new Complex[SignalGenerator.SAMPLES_COUNT];
        for (int i = 0; i < SignalGenerator.SAMPLES_COUNT; i++) {
            in[i] = new Complex(signal.get(i).getYValue().doubleValue(), 0.0);
        }
        Complex[] out = FastFourierTransform.fft(in);

        spectrumGenerator = new SpectrumGenerator(out);

        var amplitudeSpectrum = spectrumGenerator.generateAmplitudeSpectrum();
        amplitudeSpectrumChart.getData().add(new XYChart.Series<>(observableList(amplitudeSpectrum)));

        var phaseSpectrum = spectrumGenerator.generatePhaseSpectrum();
        phaseSpectrumChart.getData().add(new XYChart.Series<>(observableList(phaseSpectrum)));
    }

    public void onStartAlgorithm() {

        if(!mathService.isRunning()) {
            mathService.reset();
            mathService.start();
        }
    }
}
