package com.jetbrains;

import java.io.*;
import java.util.*;

public class ReadFile {
    public static List<Integer> readFile(String name){
        try {
            Scanner scanner = new Scanner(new File("../ProjSO2/files/"+name));
            List<Integer> list = new ArrayList<>();
            while(scanner.hasNextInt())
            {
                list.add(scanner.nextInt());
            }
            return list;
        }catch (Exception e){
            System.out.println("Error reading file: "+e);
        }
        return null;
    }
}
