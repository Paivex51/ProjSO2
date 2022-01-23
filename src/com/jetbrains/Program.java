package com.jetbrains;

import java.util.concurrent.*;
import java.util.*;

public class Program {
    Storage storage;
    Semaphore access;

    public Program() {
        this.storage = new Storage(0);
        this.access = new Semaphore(1);
    }

    public void start() {
        switch (goGetMode().toUpperCase()) {
            case "AJR":
                System.out.println("Using AJR...");
                doAJR();
                break;
            case "AJRADV":
            case "ADV":
                System.out.println("Using AJRADV...");
                doAJRADV();
                break;
            default:
                System.out.println("Using default so ARJ...");
                doAJR();
                break;
        }
        System.out.println("Bye :)");
    }

    public void doAJR() {
        boolean alive = true;
        List<AJR> threadList = new ArrayList<>();
        List<int[]> arrayInfo = readFile();
        System.out.println("Values in use: ");
        for (int[] arr : arrayInfo) {
            System.out.println(Arrays.toString(arr));
        }
        List<Integer> in = doReading();
        storage.setMaxC(arrayInfo.get(0)[1]);
        long start = System.currentTimeMillis();
        long end = start + ((in.get(1) + 2) * 1000);
        for (int i = 0; i < in.get(0); i++) {
            threadList.add(new AJR(arrayInfo.get(0), arrayInfo.get(1), arrayInfo.get(2), in.get(1), in.get(2), in.get(3), storage, access));
        }
        for (AJR thread : threadList) {
            thread.start();
        }
        try {
            while (end > System.currentTimeMillis() || alive) {
                for (AJR thread : threadList) {
                    if (!thread.isAlive()) {
                        alive = false;
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            System.out.println(e);
        }
        printResult();
    }

    public void doAJRADV() {
        boolean alive = true;
        List<AJR> threadList = new ArrayList<>();
        List<int[]> arrayInfo = readFile();
        System.out.println("Values in use: ");
        for (int[] arr : arrayInfo) {
            System.out.println(Arrays.toString(arr));
        }
        List<Integer> in = doReading();
        storage.setMaxC(arrayInfo.get(0)[1]);
        int interval = goGetInterval(in);
        int diference = in.get(1) / interval;  //amount of times the adv is going to happen

        long start = System.currentTimeMillis();
        long end = start + ((in.get(1) + 2 + diference) * 1000);
        for (int i = 0; i < in.get(0); i++) {
            threadList.add(new AJR(arrayInfo.get(0), arrayInfo.get(1), arrayInfo.get(2), in.get(1) + diference, in.get(2), in.get(3), storage, access));
        }
        for (AJR thread : threadList) {
            thread.start();
        }
        while (end > System.currentTimeMillis() && alive) {
            try {
                TimeUnit.SECONDS.sleep(interval);
                System.out.println("////////////////////////////");
                System.out.println("AJRADV in progress...");
                for (AJR thread : threadList) {
                    if (!thread.isAlive()) {
                        alive = false;
                    }
                    thread.setStop(true);
                }
                if (alive == false) {
                    System.out.println("AJRADV canceled...");
                } else {
                    System.out.println("Waiting for threads to stop...");
                    //TimeUnit.SECONDS.sleep(1);
                    TimeUnit.MILLISECONDS.sleep(100);
                    System.out.println("Threads stopped...");
                    System.out.println("AJRADV started...");
                    //applyADV(threadList, in);
                    System.out.println("AJRADV ended...");
                    for (AJR thread : threadList) {
                        thread.setStop(false);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        printResult();
    }

    public void applyADV(List<AJR> threadList, List<Integer> in) {
        List<Sujeito> world = new ArrayList<>();
        for (AJR thread : threadList) {
            for (Sujeito suj : thread.getPopulation()) {
                world.add(new Sujeito(suj));
            }
        }
        System.out.println("World size merged: " + world.size());
        Collections.sort(world, Comparator.comparingDouble(Sujeito::calcEval));
        for (int i = world.size() - 1; i >= in.get(3); i--) {
            world.remove(i);
        }
        System.out.println("World size clean: " + world.size());
        for (AJR thread : threadList) {
            thread.setPopulation(world);
        }
    }

    public int goGetInterval(List<Integer> in) {
        Scanner sc = new Scanner(System.in);
        boolean repeat = true;
        int interval;
        String val;
        System.out.println("Interval between ADV's in seconds(default) or p%: " +
                "\nType: s or p");
        val = sc.nextLine();
        if (val.equalsIgnoreCase("p")) {
            System.out.println("Interval between ADV's in % of maxRunTime: " +
                    "\nNote: For each interval that occurs thread gets more 1s of work time " +
                    "        p% of maxRunTime cant be lower than 1s" +
                    "\nn>0 and n<100");
            do {
                interval = sc.nextInt();
                if (interval > 0 && interval < 100) {
                    interval = (in.get(1) * interval) / 100;
                    if (interval < 1) {
                        interval = 1;
                    }
                    repeat = false;
                    System.out.println(interval);
                }
            } while (repeat == true);
        } else {
            System.out.println("Interval between ADV's in seconds: " +
                    "\nNote: For each interval that occurs thread gets more 1s of work time " +
                    "\nn>=1 and n<maxRunTime");
            do {
                interval = sc.nextInt();
                if (interval >= 1 && interval < in.get(1)) {
                    repeat = false;
                }
            } while (repeat == true);
        }

        return interval;
    }

    public String goGetMode() {
        Scanner sc = new Scanner(System.in);
        boolean repeat = true;
        String choice;
        System.out.println("Algorithm: " +
                "\nAJR or AJRADV");
        do {
            choice = sc.nextLine();
            if (choice.equalsIgnoreCase("ajr") || choice.equalsIgnoreCase("ajradv") || choice.equalsIgnoreCase("adv")) {
                repeat = false;
            }
        } while (repeat == true);
        return choice;
    }

    public void printResult() {
        storage.calcCost();
        System.out.println("FINAL RESULT!!!!!!!!!!");
        storage.printInfo();
        System.out.println("Iteration nº: " + storage.getIteration() + " at run time: " + storage.getTimeToSolution() + "s");
    }

    public List<int[]> readFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("File name: ");
        String name = sc.nextLine();
        List<Integer> intList = ReadFile.readFile(name);
        List<int[]> listConcat = new ArrayList<>();
        if (intList == null) {
            listConcat.add(Main.m_maxC);
            listConcat.add(Main.compPecas);
            listConcat.add(Main.qtddPecas);
        } else {
            int[] temp1 = {intList.get(0), intList.get(1)};
            int[] temp2 = new int[intList.get(0)];
            int[] temp3 = new int[intList.get(0)];
            for (int i = 0; i < intList.get(0); i++) {
                temp2[i] = intList.get(i + 2);
                temp3[i] = intList.get(i + 2 + intList.get(0));
            }
            listConcat.add(temp1);
            listConcat.add(temp2);
            listConcat.add(temp3);
        }
        return listConcat;
    }

    public List<Integer> doReading() {
        Scanner sc = new Scanner(System.in);
        List<Integer> out = new ArrayList<>();
        System.out.println("How many threads: " +
                "\nn<=0 -> 10");
        int thread = sc.nextInt();
        if (thread <= 0) {
            thread = 10;
        }
        out.add(thread);
        System.out.println("How many seconds: " +
                "\nn<=0 -> 20s");
        int seconds = sc.nextInt();
        if (seconds <= 0) {
            seconds = 20;
        }
        out.add(seconds);
        System.out.println("Max number of iterations per thread: " +
                "\nn=0-> infinit n<0 -> 20000");
        int iterations = sc.nextInt();
        if (iterations == 0) {
            iterations = 2147483600;
        } else if (iterations < 0) {
            iterations = 20000;
        }
        out.add(iterations);
        System.out.println("Population size: " +
                "\nn<=0-> 75");
        int pops = sc.nextInt();
        if (pops <= 0) {
            pops = 75;
        }
        out.add(pops);
        return out;
    }
}
