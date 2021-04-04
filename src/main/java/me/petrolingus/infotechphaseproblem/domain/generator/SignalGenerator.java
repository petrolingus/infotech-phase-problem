package me.petrolingus.infotechphaseproblem.domain.generator;

import javafx.scene.chart.XYChart;
import me.petrolingus.infotechphaseproblem.domain.math.FastFourierTransform;

import java.util.ArrayList;

public class SignalGenerator {

    public final static int SAMPLES_COUNT = 1024;

    public final static int SAMPLES_COUNT_MINUS_ONE = 1024;

    public final static double SIGNAL_BEGIN = 0;

    public final static double SIGNAL_END = 1024;

    public final static double SIGNAL_TICK_UNIT = (SIGNAL_END - SIGNAL_BEGIN) / 16;

    private final GaussianDome[] domes;

    public SignalGenerator() {
        GaussianDome dome1 = new GaussianDome(4.0, 150, 3.0);
        GaussianDome dome2 = new GaussianDome(2.0, 380, 2.0);
        GaussianDome dome3 = new GaussianDome(3.5, 600, 3.0);
        GaussianDome dome4 = new GaussianDome(2.5, 800, 2.0);
        GaussianDome dome5 = new GaussianDome(3.5, 920, 3.0);
        domes = new GaussianDome[]{dome1, dome2, dome3, dome4, dome5};
    }

    public ArrayList<XYChart.Data<Number, Number>> generate() {

        ArrayList<XYChart.Data<Number, Number>> signal = new ArrayList<>();

        for (int i = 0; i < SAMPLES_COUNT; i++) {
            double value = 0;
            double x = i * ((SIGNAL_END - SIGNAL_BEGIN) / SAMPLES_COUNT_MINUS_ONE);
            for (int j = 0; j < 5; j++) {
                double a = domes[j].amplitude;
                double d = domes[j].deviation;
                double m = domes[j].mean;
                value += a * Math.exp(-((x - m) * (x - m)) / (2 * d * d));
            }
            signal.add(new XYChart.Data<>(i, value));
        }

        return signal;
    }
}
