package com.crk.elasticsearch;


public class Test {
    public static void main(String[] args) {
        System.out.println(gcd(6,3));
        System.out.println(Math.abs(-2147483648));
        String s = "123456";
        System.out.println(s.hashCode());

    }

    public static int gcd(int a,int b){
        if (b==0) return a;
        int r = a%b;
        return gcd(b,r);
    }
}
