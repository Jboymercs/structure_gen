package com.example.structure.util;

import java.util.Random;

public class ModRand {
    //Just putting this in its separate class for organization

    private static Random rand = new Random();


    public static <T> T choice(T[] array) {
        Random rand = new Random();
        return choice(array, rand);
    }

    public static <T> T choice(T[] array, Random rand) {
        int i = rand.nextInt(array.length);
        return array[i];
    }
}
