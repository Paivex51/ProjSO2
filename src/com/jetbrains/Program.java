package com.jetbrains;

import java.util.concurrent.*;
import java.util.*;

public class Program {
    Storage storage;
    Semaphore access ;

    public Program(){
        this.storage = new Storage(0);
        this.access = new Semaphore(1);
    }

    public void start(){
        List<int[]> arrayInfo = readFile();
        System.out.println("Values in use: ");
        for (int[] arr: arrayInfo){
            System.out.println(Arrays.toString(arr));
        }
        List<Integer> in = doReading();
        storage.setMaxC(arrayInfo.get(0)[1]);
        for (int i = 0; i < in.get(0);i++){
            new AJR(arrayInfo.get(0), arrayInfo.get(1), arrayInfo.get(2), in.get(1), in.get(2), in.get(3),storage, access).start();
        }
        try {
            TimeUnit.SECONDS.sleep(in.get(1)+2);
        }catch (Exception e){
            System.out.println(e);
        }
        printResult();

    }

    public void printResult(){
        storage.calcCost();
        System.out.println("FINAL RESULT!!!!!!!!!!");
        storage.printInfo();
        System.out.println("Iteration nº: "+storage.getIteration()+" at run time: "+storage.getTimeToSolution()+"s");
    }

    public List<int[]> readFile(){
        Scanner sc = new Scanner(System.in);
        System.out.println("File name: ");
        String name = sc.nextLine();
        List<Integer> intList = ReadFile.readFile(name);
        List<int[]> listConcat = new ArrayList<>();
        if(intList==null){
            listConcat.add(Main.m_maxC);
            listConcat.add(Main.compPecas);
            listConcat.add(Main.qtddPecas);
        }else{
            int[] temp1 = {intList.get(0),intList.get(1)};
            int[] temp2 = new int[intList.get(0)];
            int[] temp3 = new int[intList.get(0)];
            for(int i = 0; i<intList.get(0);i++){
                temp2[i] = intList.get(i+2);
                temp3[i] = intList.get(i+2+intList.get(0));
            }
            listConcat.add(temp1);
            listConcat.add(temp2);
            listConcat.add(temp3);
        }
        return listConcat;
    }
    public List<Integer> doReading(){
        Scanner sc = new Scanner(System.in);
        List<Integer> out = new ArrayList<>();
        System.out.println("How many threads: n<=0 -> 10");
        int thread = sc.nextInt();
        if(thread<=0){
            thread = 10;
        }
        out.add(thread);
        System.out.println("How many seconds: n<=0 -> 10s");
        int seconds = sc.nextInt();
        if(seconds<=0){
            seconds=10;
        }
        out.add(seconds);
        System.out.println("Max number of iterations per thread: ");
        System.out.println("n=0-> infinit n<0 -> 20000");
        int iterations = sc.nextInt();
        if(iterations == 0){
            iterations=2147483600;
        } else if(iterations < 0){
            iterations=20000;
        }
        out.add(iterations);
        System.out.println("Population size: ");
        System.out.println("n<=0-> 75");
        int pops = sc.nextInt();
        if(pops <= 0){
            pops=75;
        }
        out.add(pops);
        return out;
    }
}
