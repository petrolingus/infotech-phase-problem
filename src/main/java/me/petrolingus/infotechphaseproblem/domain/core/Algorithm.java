package me.petrolingus.infotechphaseproblem.domain.core;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Algorithm extends Service<Void> {

    @Override
    protected Task<Void> createTask() {

        return new Task<Void>() {

            @Override
            protected Void call() {
                return null;
            }
        };
    }
}
