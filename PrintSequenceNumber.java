package com.qijian.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintSequenceNumber extends Thread {
    private static int current;
    private int max;
    private Lock lock;
    private Condition[] conditions;
    private int threadId;
    public PrintSequenceNumber(Lock lock,Condition[] conditions, int threadId,int start, int max) {
        this.max = max;
        this.lock = lock;
        this.conditions = conditions;
        this.threadId = threadId;
        this.current = start;
        this.setName("thread " + threadId +":");
    }

    @Override
    public void run() {
        while(current < max){
            try{
                lock.lock();
                while(current % conditions.length != threadId){
                    conditions[(threadId +1) % conditions.length].signal();
                    conditions[threadId].await();
                }
                System.out.println(this.getName() + current);
                current += 1;
                conditions[(threadId +1) % conditions.length].signal();
                conditions[threadId].await();
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
            lock.lock();
            for(int i=1;i<conditions.length;i++){
                conditions[(threadId + i) % conditions.length].signal();
            }
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition[] conditions = new Condition[3];
        for (int i=0;i<conditions.length;i++) {
            conditions[i] = lock.newCondition();
        }
        for (int i=0;i<conditions.length;i++){
            new PrintSequenceNumber(lock,conditions,i,20,100).start();
        }
    }
}
