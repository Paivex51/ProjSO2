package com.jetbrains;

public class Storage extends Sujeito {

    int iteration;
    double timeToSolution;

    public Storage(int maxC) {
        super(maxC);
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public double getTimeToSolution() {
        return timeToSolution;
    }

    public void setTimeToSolution(double timeToSolution) {
        this.timeToSolution = timeToSolution;
    }

}
