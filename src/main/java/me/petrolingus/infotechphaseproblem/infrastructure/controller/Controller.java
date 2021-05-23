package me.petrolingus.infotechphaseproblem.infrastructure.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import me.petrolingus.infotechphaseproblem.domain.core.Algorithm;
import org.apache.commons.math3.analysis.function.Gaussian;

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

    public Button generateSignalButton;
    public Button startButton;
    public Button stopButton;

    public TextField number;
    private int shift = 0;

    private final Algorithm algorithm = new Algorithm();
    private boolean isRunning = false;


    public void initialize() {
        generateSignalButton.disableProperty().bind(algorithm.runningProperty());
        startButton.disableProperty().bind(algorithm.runningProperty());
        stopButton.disableProperty().bind(algorithm.runningProperty().not());
        onSignalGenerateButton();
    }

    public void onSignalGenerateButton() {

        int samplesCount = Integer.parseInt(samplesCountField.getText());

        configureAxis(samplesCount);

        double samplingRate = Double.parseDouble(samplingRateField.getText());

        // TODO: 04.04.2021 FIX THIS SHIT ////////////////////////
        algorithm.signalChart = signalChart;
        algorithm.amplitudeSpectrumChart = amplitudeSpectrumChart;
        algorithm.phaseSpectrumChart = phaseSpectrumChart;
        algorithm.samplesCount = samplesCount;
        algorithm.samplingRate = samplingRate;
        //////////////////////////////////////////////////////////

        List<Gaussian> gaussianList = new ArrayList<>();
        gaussianList.add(createGaussian(amplitudeField1, deviationField1, meanField1));
        gaussianList.add(createGaussian(amplitudeField2, deviationField2, meanField2));
        gaussianList.add(createGaussian(amplitudeField3, deviationField3, meanField3));
        gaussianList.add(createGaussian(amplitudeField4, deviationField4, meanField4));
        gaussianList.add(createGaussian(amplitudeField5, deviationField5, meanField5));

        algorithm.initialize(gaussianList);

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
        algorithm.reset();
        isRunning = false;
    }

    public void onLeftButton() {
        shift = Integer.parseInt(number.getText());
        shiftSolution();
    }

    public void onRightButton() {
        shift = -Integer.parseInt(number.getText());
        shiftSolution();
    }

    private void shiftSolution() {
        double[] signalData = getCurrantSolutionData();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < signalData.length; i++) {
            int a = shift < 0 ? i + signalData.length + shift : i + shift;
            double value = signalData[a % signalData.length];
            series.getData().add(new XYChart.Data<>(i, value));
        }

        signalChart.getData().set(1, series);
    }

    public void onInverseSolutionButton() {
        double[] signalData = getCurrantSolutionData();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < signalData.length; i++) {
            double value = signalData[signalData.length - i - 1];
            series.getData().add(new XYChart.Data<>(i, value));
        }

        signalChart.getData().set(1, series);
    }

    private double[] getCurrantSolutionData() {
        ObservableList<XYChart.Data<Number, Number>> data = signalChart.getData().get(1).getData();
        double[] signalData = new double[data.size()];
        for (int i = 0; i < signalData.length; i++) {
            signalData[i] = data.get(i).getYValue().doubleValue();
        }
        return signalData;
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

    private Gaussian createGaussian(TextField amplitude, TextField sigma, TextField mu) {
        double a = Double.parseDouble(amplitude.getText());
        double s = Double.parseDouble(sigma.getText());
        double m = Double.parseDouble(mu.getText());
        return new Gaussian(a, m, s);
    }
}
