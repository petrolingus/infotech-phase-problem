package me.petrolingus.infotechphaseproblem.infrastructure.controller;

import javafx.collections.FXCollections;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import me.petrolingus.infotechphaseproblem.domain.DiscreteSignalGenerator;
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

    public void initialize() {
        onSignalGenerateButton();
    }

    public void onSignalGenerateButton() {

        int samplesCount = Integer.parseInt(samplesCountField.getText());
        axisConfigure(signalChartXAxis, samplesCount);

        double samplingRate = Double.parseDouble(samplingRateField.getText());

        DiscreteSignalGenerator signalGenerator = new DiscreteSignalGenerator(samplesCount, samplingRate);

        Gaussian gaussian1 = new Gaussian(4, 150, 3);
        Gaussian gaussian2 = new Gaussian(2, 380, 2);
        Gaussian gaussian3 = new Gaussian(3.5, 600, 3);
        Gaussian gaussian4 = new Gaussian(2.5, 800, 2);
        Gaussian gaussian5 = new Gaussian(3.5, 920, 3);
        List<Gaussian> gaussianList = List.of(gaussian1, gaussian2, gaussian3, gaussian4, gaussian5);

        double[] signalArray = signalGenerator.generate(gaussianList);

        // Creating signal data and put it into chart
        List<XYChart.Data<Number, Number>> signalArrayList = new ArrayList<>();
        for (int i = 0; i < signalArray.length; i++) {
            signalArrayList.add(new XYChart.Data<>(i, signalArray[i]));
        }
        XYChart.Series<Number, Number> signalSeries = new XYChart.Series<>(
                FXCollections.observableList(signalArrayList)
        );
        signalChart.getData().clear();
        signalChart.getData().add(signalSeries);
    }

    private void axisConfigure(NumberAxis axis, int upperBound) {
        axis.setAutoRanging(false);
        axis.setLowerBound(0);
        axis.setUpperBound(upperBound);
        axis.setTickUnit(128);
    }
}
