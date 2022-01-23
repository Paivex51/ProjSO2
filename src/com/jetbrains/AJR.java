package com.jetbrains;

import java.util.*;
import java.util.concurrent.*;

public class AJR extends Thread {
    boolean stop = false, dead = false;
    long start, end;
    Semaphore access;
    Storage storage;
    int u, m, maxC, compare, seconds;
    int[] m_maxC, compPecas, qtddPecas, iterations;
    List<Sujeito> population;
    List<Sujeito> descendent;

    public AJR(int[] m_maxC, int[] compPecas, int[] qtddPecas, int seconds, int maxIterations, int pops, Storage storage, Semaphore access) {
        if (m_maxC.length != 2) {
            System.out.println("u_m_maxC.length != 2");
        } else {
            m = m_maxC[0];
            maxC = m_maxC[1];
            if (compPecas.length != m || qtddPecas.length != m) {
                System.out.println("compPecas & qtddPecas .length != m");
            } else {
                this.m_maxC = m_maxC;
                this.compPecas = compPecas;
                this.qtddPecas = qtddPecas;
            }
        }
        if (maxIterations == 0) {
            maxIterations = 2147483600;
        } else if (maxIterations < 0) {
            maxIterations = 20000;
        }
        this.iterations = new int[]{maxIterations, 1}; //[maxIteration, current]
        this.compare = 10;
        this.u = pops;
        this.seconds = seconds;
        this.storage = storage;
        this.access = access;
        this.population = createPopulation();
        this.descendent = new ArrayList<>();
        //popCalcWaste();  //a funçao popCalcCost() ja inclui esta linha
        popCalcCost();  //inclui a funçao acima
    }

    @Override
    public void run() {
        AJR_E();
    }

    public void AJR_E() {
        start = System.currentTimeMillis();
        end = start + (seconds * 1000);
        do {
            if (stop == false) {
                try {
                    popAlg3PS();
                    popCompare();
                    updateStorage();
                    iterations[1]++;
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } while (iterations[0] > iterations[1] && System.currentTimeMillis() < end && storage.getTotalWaste()!=0);
        while (stop) {
            try {
                System.out.println("Waiting for last ADV...");
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Thread Exiting...");
        dead = true;
    }

    public void printInfo() {
        for (Sujeito suj : population) {
            suj.printInfo();
        }
        System.out.println("/////////////////////////");
        System.out.println("Pops size: " + population.size());
    }

    private void popCalcWaste() {
        for (Sujeito suj : population) {
            suj.calcWaste();
        }
    }

    private void popCalcCost() {
        for (Sujeito suj : population) {
            suj.calcCost();
        }
    }

    private void popAlg3PS() {
        popCalcCost();
        descendent.clear();
        for (Sujeito suj : population) {
            descendent.add(new Sujeito(suj));
        }
        for (Sujeito suj : descendent) {
            suj.alg3PS();
            suj.alg3PS();
            suj.calcCost();
        }
    }

    private void popCompare() {
        if (descendent.size() == 0) {
            popAlg3PS();
        }
        population.addAll(new ArrayList<>(descendent));
        descendent.clear();
        for (Sujeito suj : population) {
            suj.resetScore();
            for (int i = 0; i < compare; i++) {
                int idx = (int) (Math.random() * population.size());
                while (suj == population.get(idx)) {
                    idx = (int) (Math.random() * population.size());
                }
                if (suj.getCost() < population.get(idx).getCost()) {
                    suj.incrementScore();
                }
            }
        }
        try {
            Collections.sort(population, (suj1, suj2) -> Double.compare(suj2.getScore(), suj1.getScore()));
        } catch (Exception e) {
            System.out.println(e);
        }
        for (int x = population.size() - 1; x >= u; x--) {
            population.remove(u);
        }
    }

    private void updateStorage() {
        for (Sujeito suj : population) {
            try {
                access.acquire();
                if (suj.calcEval() < storage.calcEval()) {
                    //System.out.println("PREVIOUS RESULT!!!!!!!!!!"); //remove comment to see current best before
                    //storage.printInfo();
                    storage.setPecas(new ArrayList<>(suj.getPecas()));
                    storage.calcCost();
                    storage.setIteration(iterations[1]);
                    storage.setTimeToSolution((double) (System.currentTimeMillis() - start) / 1000);
                    System.out.println("NEW RESULT!!!!!!!!!!");
                    storage.printInfo();
                }
                access.release();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private List<Sujeito> createPopulation() {
        List<Sujeito> pops = new ArrayList<>();
        //population.clear();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int x = qtddPecas[i]; x > 0; x--) {
                temp.add(compPecas[i]);
            }
        }
        for (int w = u; w > 0; w--) {
            Sujeito suj = new Sujeito(maxC);
            suj.setPecas(new ArrayList<>(temp));
            suj.shufflePecas();
            pops.add(suj);
        }
        Collections.shuffle(pops);
        return pops;
    }

    public void setStop(boolean value) {
        this.stop = value;
    }

    public List<Sujeito> getPopulation() {
        return population;
    }

    public void setPopulation(List<Sujeito> population) {
        this.population = new ArrayList<>(population);
    }

}
