package com.jetbrains;

import java.util.*;
import java.util.concurrent.*;

public class AJR extends Thread{
    long start, end;
    Semaphore access;
    Storage storage;
    int u, m, maxC, compare;
    int[] u_m_maxC, compPecas, qtddPecas, iterations;
    List<Sujeito> population;
    List<Sujeito> descendent;
    public AJR(int[] u_m_maxC, int[] compPecas, int[] qtddPecas, int maxIterations, Storage storage, Semaphore access){
        if(u_m_maxC.length!=3){
            System.out.println("u_m_maxC.length != 3");
        }else{
            u=u_m_maxC[0];
            m=u_m_maxC[1];
            maxC=u_m_maxC[2];
            if(compPecas.length!=m || qtddPecas.length != m){
                System.out.println("compPecas & qtddPecas .length != m");
            }else{
                this.u_m_maxC=u_m_maxC;
                this.compPecas=compPecas;
                this.qtddPecas=qtddPecas;
            }
        }
        if(maxIterations == 0){
            maxIterations=2147483600;
        } else if(maxIterations < 0){
            maxIterations=20000;
        }
        this.iterations = new int[]{maxIterations,1}; //[maxIteration, current]
        this.compare=10;
        this.storage = storage;
        this.access = access;
        this.population = createPopulation();
        this.descendent = new ArrayList<>();
        //popCalcWaste();  //a funçao popCalcCost() ja inclui esta linha
        popCalcCost();  //inclui a funçao acima
    }

    @Override
    public void run(){
        AJR_E();
    }

    public void AJR_E(){
        start = System.currentTimeMillis();
        do {
            popAlg3PS();   // inclui as 2 funçoes acima
            popCompare();  //inclui as 3 acima
            updateStorage();
            iterations[1]++;
        }while (iterations[0] > iterations[1]);
        //printInfo();
    }

    public void printInfo(){
        for (Sujeito suj : population){
            suj.printInfo();
        }
        System.out.println("/////////////////////////");
        System.out.println("Pops size: "+population.size());
    }

    private void popCalcWaste(){
        for (Sujeito suj: population) {
            suj.calcWaste();
        }
    }

    private void popCalcCost(){
        for (Sujeito suj: population) {
            suj.calcCost();
        }
    }

    private void popAlg3PS(){
        popCalcCost();
        descendent.clear();
        for (Sujeito suj: population) {
            descendent.add(new Sujeito(suj));
        }
        for (Sujeito suj: descendent) {
            suj.alg3PS();
            suj.alg3PS();
            suj.calcCost();
        }
    }

    private void popCompare(){
        if(descendent.size()==0){
            popAlg3PS();
        }
        population.addAll(new ArrayList<>(descendent));
        descendent.clear();
        for (Sujeito suj: population) {
            suj.resetScore();
            for (int i = 0; i<compare; i++){
                int idx = (int)(Math.random() * population.size());
                while (suj == population.get(idx)){
                    idx = (int)(Math.random() * population.size());
                }
                if(suj.getCost() < population.get(idx).getCost()){
                    suj.incrementScore();
                }
            }
        }
        Collections.sort(population, (suj1, suj2) -> Double.compare(suj2.getScore(), suj1.getScore()));
        for (int x = population.size() - 1; x >= u; x--){
            population.remove(u);
        }
    }

    private void updateStorage(){
        for (Sujeito suj: population){
            try {
                access.acquire();
                if(suj.isBetter() < storage.isBetter()){
                    //System.out.println("Previous !!!!!!!!!!");
                    //storage.printInfo();
                    storage.setPecas(new ArrayList<>(suj.getPecas()));
                    storage.calcCost();
                    storage.setIteration(iterations[1]);
                    storage.setTimeToSolution(System.currentTimeMillis()-start);
                    //System.out.println("NEW !!!!!!!!!!");
                    storage.printInfo();
                }
                access.release();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    private List<Sujeito> createPopulation(){
        List<Sujeito> pops = new ArrayList<>();
        //population.clear();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0 ; i < m; i++){
            for(int x = qtddPecas[i]; x > 0; x--){
                temp.add(compPecas[i]);
            }
        }
        for (int w = u; w > 0; w--){
            Sujeito suj = new Sujeito();
            suj.setPecas(new ArrayList<>(temp));
            suj.shufflePecas();
            pops.add(suj);
        }
        Collections.shuffle(pops);
        return pops;
    }

}
