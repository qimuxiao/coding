package com.qijian.demo;

import java.util.concurrent.locks.StampedLock;

public class Main {
     class A {
         A () {
         }
         public void test() {
             System.out.println("this is a test");
         }
    }

    public void test(){
         A a = new A();
    }

    public static void main(String[] args) {
        Main main = new Main();
        Main.A a = main.new A();
        a.test();
        StampedLock lock = new StampedLock();
    }
}
