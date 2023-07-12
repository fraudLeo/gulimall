package com.leo.gulimall.gulimall.gateway;

public class Dao2 {


    public static void main(String[] args) {
        test();
    }


    public static void test() {
        Dao1 dao1 = new Dao1();

        dao1.setBinary(Binary.SUCCESS_200);
        System.out.println(dao1.getBinary()+"->"+dao1.getBinary().getValue());
        System.out.println("----------------");
        dao1.setBinary(Binary.ZERO_0);
        System.out.println(dao1.getBinary()+"->"+dao1.getBinary().getValue());

    }
}
