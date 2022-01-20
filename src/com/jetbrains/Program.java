package com.jetbrains;

import java.util.concurrent.*;
import java.util.*;

public class Program {
    int threads;
    Storage storage;
    Semaphore access ;

    public Program(){
        this.storage = new Storage();
        this.access = new Semaphore(1);
    }

    public void start(){
        doReading();
        threads=4;
        for (int i = 0; i < threads;i++){
            AJR ajr = new AJR(Main.u_m_maxC, Main.compPecas, Main.qtddPecas, 10000, storage, access);
            ajr.start();
            try {
                ajr.join();
            }catch (Exception e){
                System.out.println(e);
            }
        }
        printResult();
    }

    public void printResult(){
        storage.calcCost();
        storage.printInfo();
        System.out.println("Iteration nº: "+storage.getIteration()+" Run time: "+storage.getTimeToSolution()+"ms");
    }

    public boolean doReading(){
        return true;
    }
}
