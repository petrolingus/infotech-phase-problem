package me.petrolingus.infotechphaseproblem.domain.generator;

import javafx.scene.chart.XYChart;
import me.petrolingus.infotechphaseproblem.domain.math.Complex;

import java.util.ArrayList;
import java.util.Random;

public class SpectrumGenerator {

    Complex[] complexes;

    public SpectrumGenerator(Complex[] complexes) {
        this.complexes = complexes;
    }

    public ArrayList<XYChart.Data<Number, Number>> generateAmplitudeSpectrum() {

        ArrayList<XYChart.Data<Number, Number>> signal = new ArrayList<>();

        for (int i = 0; i < complexes.length; i++) {
            double x = i * (1.0 / SignalGenerator.SAMPLES_COUNT_MINUS_ONE);
            double value = complexes[i].abs();
            signal.add(new XYChart.Data<>(i, value));
        }

        return signal;
    }

    public ArrayList<XYChart.Data<Number, Number>> generatePhaseSpectrum() {

        ArrayList<XYChart.Data<Number, Number>> signal = new ArrayList<>();

        for (int i = 0; i < complexes.length; i++) {
            double x = i * (1.0 / SignalGenerator.SAMPLES_COUNT_MINUS_ONE);
            double value = complexes[i].phase();
            signal.add(new XYChart.Data<>(i, value));
        }

        return signal;
    }

    public static ArrayList<XYChart.Data<Number, Number>> generateRandomPhaseSpectrum() {

        ArrayList<XYChart.Data<Number, Number>> signal = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < SignalGenerator.SAMPLES_COUNT; i++) {
            double value = 2 * Math.PI * random.nextDouble() - Math.PI;
            signal.add(new XYChart.Data<>(i, value));
        }

        return signal;
    }
}
