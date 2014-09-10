/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ia.util;

/**
 *
 * @author mg
 */
public class RandomIntGenerator{  
    public RandomIntGenerator(int l, int h){  
        low = l;  
        high = h;  
    }  
    public int draw(){  
        int r = low + (int)((high-low+1)*nextRandom());  
        return r;  
    }  
    public static void main(String[] args){  
        RandomIntGenerator r1 = new RandomIntGenerator(1,10);  
        RandomIntGenerator r2 = new RandomIntGenerator(0,1);  
        int i;  
        for (i = 1;i<=100; i++)  
           System.out.println(r1.draw()+""+r2.draw());  
     } 
    
     public static double nextRandom(){  
         int pos = (int)(java.lang.Math.random() * BUFFER_SIZE);  
         double r = buffer[pos];  
         buffer[pos] = java.lang.Math.random();  
         return r;  
     }  
    private static final int BUFFER_SIZE = 101;  
    private static double[] buffer = new double[BUFFER_SIZE];  
    static{  
       int i;  
       for (i = 0; i<BUFFER_SIZE; i++)  
           buffer[i] = java.lang.Math.random();  
    }  
    private int low;  
    private int high;  
}
