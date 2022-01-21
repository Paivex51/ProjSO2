package com.jetbrains;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    static int[] m_maxC = {4,12};  //default values
    static int[] compPecas = {3,4,5,6}; //default values
    static int[] qtddPecas = {2,2,1,3}; //default values
    static int m=m_maxC[0], maxC=m_maxC[1];
    public static void main(String[] args) {
        //do the reading
        //int[] u_m_maxC2 = {3,4,12};
        //u_m_maxC = u_m_maxC2;
        //AJR thread1 = new AJR(m_maxC, compPecas, qtddPecas);
        //thread1.AJR_E();
        //fileTest();
        //simple();
        Program program = new Program();
        program.start();

    }

    public static void simple(){
        int[] m_maxC = {24, 120};  //default values
        int[] compPecas = {22, 23, 24, 26, 27, 28, 29, 30, 31, 36, 39, 41, 42, 48, 49, 50, 51, 54, 55, 56, 59, 60, 66, 67}; //default values
        int[] qtddPecas = {12, 8, 27, 15, 25, 7, 10, 22, 5, 16, 19, 21, 26, 16, 12, 26, 20, 25, 9, 17, 22, 14, 17, 9};
        Storage storage = new Storage(m_maxC[1]);
        Semaphore access = new Semaphore(1);
        AJR arj = new AJR(m_maxC, compPecas, qtddPecas, 10, 20000000, 75,storage, access);
        arj.start();






        try {
            TimeUnit.SECONDS.sleep(12);
        }catch (Exception e){
            System.out.println(e);
        }
        storage.calcCost();
        storage.printInfo();
        System.out.println("Iteration nº: "+storage.getIteration()+" at run time: "+storage.getTimeToSolution()+"s");
    }

    public static void fileTest(){
        Scanner sc = new Scanner(System.in);
        System.out.println("File name: ");
        String name = sc.nextLine();
        System.out.println(name);
        List<Integer> listinha = ReadFile.readFile(name);
        System.out.println(listinha);
    }

}


