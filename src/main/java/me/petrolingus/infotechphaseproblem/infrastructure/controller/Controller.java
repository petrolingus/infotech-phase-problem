package me.petrolingus.infotechphaseproblem.infrastructure.controller;

import javafx.collections.FXCollections;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import me.petrolingus.infotechphaseproblem.domain.generator.DiscreteSignalGenerator;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

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

    public TextField samplesCountField;
    public TextField samplingRateField;

    public void initialize() {
        onSignalGenerateButton();
    }

    public void onSignalGenerateButton() {

        int samplesCount = Integer.parseInt(samplesCountField.getText());
        axisConfigure(signalChartXAxis, samplesCount);
        axisConfigure(amplitudeSpectrumChartXAxis, samplesCount);
        axisConfigure(phaseSpectrumChartXAxis, samplesCount);

        double samplingRate = Double.parseDouble(samplingRateField.getText());

        DiscreteSignalGenerator signalGenerator = new DiscreteSignalGenerator(samplesCount, samplingRate);

        Gaussian gaussian1 = new Gaussian(4, 150, 3);
        Gaussian gaussian2 = new Gaussian(2, 380, 2);
        Gaussian gaussian3 = new Gaussian(3.5, 600, 3);
        Gaussian gaussian4 = new Gaussian(2.5, 800, 2);
        Gaussian gaussian5 = new Gaussian(3.5, 920, 3);
        List<Gaussian> gaussianList = List.of(gaussian1, gaussian2, gaussian3, gaussian4, gaussian5);


        // Creating signal data series and put it into chart
        double[] signalArray = signalGenerator.generate(gaussianList);
        XYChart.Series<Number, Number> signalSeries = arrayToSeries(signalArray);
        signalChart.getData().clear();
        signalChart.getData().add(signalSeries);


        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fft = fastFourierTransformer.transform(signalArray, TransformType.FORWARD);


        // Creating amplitude data series and put it into chart
        double[] amplitudeSpectrumData = new double[fft.length];
        for (int i = 0; i < amplitudeSpectrumData.length; i++) {
            amplitudeSpectrumData[i] = fft[i].abs();
        }
        XYChart.Series<Number, Number> amplitudeSpectrumSeries = arrayToSeries(amplitudeSpectrumData);
        amplitudeSpectrumChart.getData().clear();
        amplitudeSpectrumChart.getData().add(amplitudeSpectrumSeries);


        // Creating phase data series and put it into chart
        double[] phaseSpectrumData = new double[fft.length];
        for (int i = 0; i < phaseSpectrumData.length; i++) {
            phaseSpectrumData[i] = fft[i].getArgument();
        }
        XYChart.Series<Number, Number> phaseSpectrumSeries = arrayToSeries(phaseSpectrumData);
        phaseSpectrumChart.getData().clear();
        phaseSpectrumChart.getData().add(phaseSpectrumSeries);
    }

    private XYChart.Series<Number, Number> arrayToSeries(double[] data) {
        List<XYChart.Data<Number, Number>> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(new XYChart.Data<>(i, data[i]));
        }
        return new XYChart.Series<>(FXCollections.observableList(list));
    }

    private void axisConfigure(NumberAxis axis, int upperBound) {
        axis.setAutoRanging(false);
        axis.setLowerBound(0);
        axis.setUpperBound(upperBound);
        axis.setTickUnit(128);
    }
}
