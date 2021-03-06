package com.jetbrains;

import java.util.*;
import java.lang.Math;

public class Sujeito {

    private List<Integer> pecas, waste;
    private double cost, eval;
    private int score;

    public void setMaxC(int maxC) {
        this.maxC = maxC;
    }

    private int maxC;

    public Sujeito(int maxC) {
        this.pecas = new ArrayList<>();
        this.waste = new ArrayList<>();
        this.cost = 69420.0;
        this.score = 0;
        this.maxC = maxC;
        this.eval = 69420.0;
    }

    public Sujeito(Sujeito suj) {
        this.pecas = new ArrayList<>(suj.getPecas());
        this.waste = new ArrayList<>();
        this.cost = suj.getCost();
        this.score = 0;
        this.maxC = suj.getMaxC();
        calcCost();
        this.eval = calcEval();
    }

    public List<Integer> getPecas() {
        return pecas;
    }

    public void incrementScore() {
        this.score += 1;
    }

    public void resetScore() {
        this.score = 0;
    }

    public double getCost() {
        return this.cost;
    }

    public double calcEval() {
        //calcCost();  //makes sure everything is up-to-date
        this.eval = (double) cost + waste.size() + getTotalWaste();
        return eval;
    }

    public int getTotalWaste() {
        int total = 0;
        for (int i : waste) {
            total += i;
        }
        return total;
    }

    public double getScore() {
        return score;
    }

    public void calcWaste() {
        this.waste = new ArrayList<>();
        int c1 = 0;
        for (int i = 0; i < pecas.size(); i++) {
            c1 += pecas.get(i);
            if (c1 > maxC) {
                waste.add(maxC - (c1 - pecas.get(i)));
                c1 = pecas.get(i);
            }
            if (i == pecas.size() - 1) {
                if (c1 > maxC) {
                    waste.add(maxC - (c1 - pecas.get(i)));
                    c1 = pecas.get(i);
                    waste.add(c1);
                } else {
                    waste.add(maxC - c1);
                }
            }
        }
        for (int i = 0; i < waste.size(); i++) {
            if (waste.get(i) == null) {
                calcWaste();
            }
        }
    }

    public synchronized void calcCost() {
        calcWaste();
        int n = waste.size();
        double val = 0.0, v = 0.0;
        for (int w : waste) {
            if (w > 0) {
                v = 1.0;
            } else {
                v = 0.0;
            }
            val += Math.sqrt(((double) w / maxC)) + (v / n);
        }
        val = ((double) 1 / (n + 1)) * val;
        cost = val;
    }

    public void shufflePecas() {
        Collections.shuffle(pecas);
    }

    public void setPecas(ArrayList<Integer> pecas) {
        this.pecas = new ArrayList<>(pecas);
    }

    public void printInfo() {
        System.out.println("////////////////////////////");
        System.out.println("Pecas: " + pecas);
        System.out.println("Waste: " + waste);
        System.out.println("Total Waste: " + getTotalWaste());
        System.out.println("Cuts: " + waste.size());
        System.out.println("Cost: " + cost);
        System.out.println("Eval(includes cost): " + calcEval());
        //System.out.println("Score: "+score);
    }

    public void alg3PS() {
        calcCost();
        List<Double> prob = new ArrayList<>();
        for (int x = 0; x < waste.size(); x++) {
            int w = waste.get(x);
            if (w != 0) {
                double temp2 = 0.0;
                for (int y = 0; y < waste.size(); y++) {
                    if (waste.get(y) != 0) {
                        temp2 += (Math.sqrt((double) 1 / waste.get(y)));
                    }
                }
                double temp = 0;
                temp = (Math.sqrt((double) 1 / w)) / (temp2);
                prob.add(temp);
            } else {
                prob.add(0.0);
            }
        }
        //System.out.println("Prob Individual: "+prob);
        List<Double> prob2 = new ArrayList<>();
        int dif = 1;
        for (int x = 0; x < prob.size(); x++) {
            if (prob2.size() == 0 && prob.get(x) != 0) {
                prob2.add(prob.get(x));
            } else if (prob.get(x) != 0.0 && x > 0) {
                prob2.add((double) prob2.get(x - dif) + prob.get(x));
            } else {
                dif++;
            }
        }
        //System.out.println("Prob Acumulada: "+prob2);
        alg3PSPart2(prob2);
    }

    private void alg3PSPart2(List<Double> prob2) {
        List<Integer> choice = new ArrayList<>();
        choice.add((int) (Math.random() * pecas.size()));
        double random1 = Math.random();
        double random2 = Math.random();
        //System.out.println("Random prob n??1: "+random1);
        //System.out.println("Random prob n??2: "+random2);
        int plateNumb = 0;
        for (double p : prob2) {
            if (random1 < p) {
                choice.add(plateNumb);
                random1 = 2;
            }
            if (random2 < p) {
                choice.add(plateNumb);
                random2 = 2;
            }
            plateNumb++;
        }
        //System.out.println("[1??Index, 1??choice cut, 2?? choice cut]: "+choice);

        List<ArrayList> dividedList = new ArrayList<>();
        List<Integer> cut = new ArrayList<>();
        int c1 = 0;
        for (int i = 0; i < pecas.size(); i++) {
            c1 += pecas.get(i);
            cut.add(i);
            if (i != pecas.size() - 1) {
                if (c1 == maxC) {
                    c1 = 0;
                    cut.clear();
                }
                if (c1 > maxC) {
                    c1 = pecas.get(i);
                    cut.remove(cut.size() - 1);
                    dividedList.add(new ArrayList<>(cut));
                    cut.clear();
                    cut.add(i);
                }
            }
            if (i == pecas.size() - 1) {
                if (c1 > maxC) {
                    cut.remove(cut.size() - 1);
                    dividedList.add(new ArrayList<>(cut));
                    cut.clear();
                    cut.add(i);
                    dividedList.add(new ArrayList<>(cut));
                } else {
                    dividedList.add(new ArrayList<>(cut));
                }
            }
        }
        //System.out.println("Index List :");
        for (ArrayList arr : dividedList) {
            //System.out.println(arr);
        }
        int idx1, idx2;
        Random rand = new Random();
        idx1 = (int) dividedList.get(choice.get(1)).get(rand.nextInt(dividedList.get(choice.get(1)).size()));
        idx2 = (int) dividedList.get(choice.get(2)).get(rand.nextInt(dividedList.get(choice.get(2)).size()));
        choice.set(1, idx1);
        choice.set(2, idx2);
        //System.out.println("Final idx's: "+choice);
        if (choice.get(0) == choice.get(1) || choice.get(0) == choice.get(2) || choice.get(1) == choice.get(2)) {
            alg3PSPart2(prob2);  //se algum dos index de troca for igual repete este metodo
        } else {
            int temp = pecas.get(choice.get(0));
            pecas.set(choice.get(0), pecas.get(choice.get(1)));
            pecas.set(choice.get(1), pecas.get(choice.get(2)));
            pecas.set(choice.get(2), temp);
        }
    }

    public int getMaxC() {
        return maxC;
    }
}
