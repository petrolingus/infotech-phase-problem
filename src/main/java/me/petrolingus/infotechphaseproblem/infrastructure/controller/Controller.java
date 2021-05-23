package me.petrolingus.infotechphaseproblem.infrastructure.controller;

import javafx.collections.FXCollections;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import me.petrolingus.infotechphaseproblem.domain.core.Algorithm;

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

    public TextField number;

    public Button startButton;

    private Algorithm algorithm;

    private boolean isRunning = false;

    private int shift = 0;

    public void initialize() {
        onSignalGenerateButton();
    }

    public void onSignalGenerateButton() {

        int samplesCount = Integer.parseInt(samplesCountField.getText());

        configureAxis(samplesCount);

        double samplingRate = Double.parseDouble(samplingRateField.getText());

        algorithm = new Algorithm(samplesCount, samplingRate);
        // TODO: 04.04.2021 FIX THIS SHIT ////////////////////////
        algorithm.signalChart = signalChart;
        algorithm.amplitudeSpectrumChart = amplitudeSpectrumChart;
        algorithm.phaseSpectrumChart = phaseSpectrumChart;
        //////////////////////////////////////////////////////////
        algorithm.initialize();

        // Creating signal data series and put it into chart
        double[] signalArray = algorithm.getSignalData();
        XYChart.Series<Number, Number> signalSeries = arrayToSeries(signalArray);
        signalChart.getData().clear();
        signalChart.getData().add(signalSeries);

        // Creating amplitude data series and put it into chart
        double[] amplitudeSpectrumData = algorithm.getAmplitudeSpectrumData();
        XYChart.Series<Number, Number> amplitudeSpectrumSeries = arrayToSeries(amplitudeSpectrumData);
        amplitudeSpectrumChart.getData().clear();
        amplitudeSpectrumChart.getData().add(amplitudeSpectrumSeries);

        // Creating phase data series and put it into chart
        double[] phaseSpectrumData = algorithm.getPhaseSpectrumData();
        XYChart.Series<Number, Number> phaseSpectrumSeries = arrayToSeries(phaseSpectrumData);
        phaseSpectrumChart.getData().clear();
        phaseSpectrumChart.getData().add(phaseSpectrumSeries);
    }

    public void onRunAlgorithmButton() {
        System.out.println(algorithm.getState());
        if (!isRunning) {
            algorithm.start();
            isRunning = true;
        }
    }

    public void onStopAlgorithmButton() {
        algorithm.cancel();
    }

    public void onLeftButton() {

        int n = Integer.parseInt(number.getText());

        shift += n;

        double[] signalData = algorithm.getSignalN();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < signalData.length; i++) {
            double value = signalData[(i + shift) % signalData.length];
            series.getData().add(new XYChart.Data<>(i, value));
        }

        int size = signalChart.getData().size();
        if (size < 2) {
            signalChart.getData().add(series);
        } else {
            signalChart.getData().set(1, series);
        }
    }

    private XYChart.Series<Number, Number> arrayToSeries(double[] data) {
        List<XYChart.Data<Number, Number>> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(new XYChart.Data<>(i, data[i]));
        }
        return new XYChart.Series<>(FXCollections.observableList(list));
    }

    private void configureAxis(int upperBound) {
        axisConfigure(signalChartXAxis, upperBound);
        axisConfigure(amplitudeSpectrumChartXAxis, upperBound);
        axisConfigure(phaseSpectrumChartXAxis, upperBound);
    }

    private void axisConfigure(NumberAxis axis, int upperBound) {
        axis.setAutoRanging(false);
        axis.setLowerBound(0);
        axis.setUpperBound(upperBound);
        axis.setTickUnit(128);
    }
}
