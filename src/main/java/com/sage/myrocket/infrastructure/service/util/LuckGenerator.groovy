package com.sage.myrocket.infrastructure.service.util;

public class LuckGenerator {

    static boolean itHappens (int luck, int card){
        Math.random() % card > (card - luck)   
    }
}
