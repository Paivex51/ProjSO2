package com.jetbrains;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    static int[] u_m_maxC = {25,4,12};  //default values
    static int[] compPecas = {3,4,5,6}; //default values
    static int[] qtddPecas = {2,2,1,3}; //default values
    static int u=u_m_maxC[0], m=u_m_maxC[1], maxC=u_m_maxC[2];
    public static void main(String[] args) {
        //do the reading
        //int[] u_m_maxC2 = {3,4,12};
        //u_m_maxC = u_m_maxC2;
        //AJR thread1 = new AJR(u_m_maxC, compPecas, qtddPecas);
        //thread1.AJR_E();
        //tests();
        Program program = new Program();
        program.start();
    }

    static public void tests(){
        //List<Integer> temp = new ArrayList<>(Arrays.asList(3,3,6,4,6,4,6,5));
        List<Integer> temp = new ArrayList<>(Arrays.asList(3, 4, 6, 3, 5, 4, 6, 6));
        //List<Integer> temp = new ArrayList<>(Arrays.asList(4, 6, 3, 4, 6, 6, 5, 3));
        Sujeito suj = new Sujeito();
        suj.setPecas(new ArrayList<>(temp));
        suj.calcWaste();
        suj.calcCost();
        suj.printInfo();
        suj.alg3PS();
        suj.alg3PS();
        suj.calcCost();
        suj.printInfo();
    }

}


