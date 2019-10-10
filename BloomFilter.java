package com.qijian.demo;


import java.util.BitSet;

public class BloomFilter {
    private int DEFAULT_SIZE = 2 << 25;
    private  int[] seeds = new int[]{3,5,7,11,13,17,19,23};
    private BitSet bits = new BitSet();
    private Hash[] funs = new Hash[seeds.length];
    public BloomFilter () {
        for (int i = 0; i < seeds.length; i++) {
            funs[i] = new Hash(DEFAULT_SIZE, seeds[i]);
        }
    }
    public void add(String value){
        for (Hash hashFunc : funs) {
            bits.set(hashFunc.hash(value), true);
        }
    }

    public boolean contains(String value) {
        if (null == value) {
            return false;
        }
        boolean ret = true;
        for (Hash hashFunc:funs) {
            ret = ret & bits.get(hashFunc.hash(value));
        }
        return ret;
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("www.qimuxiao.com");
        boolean ret = bloomFilter.contains("www.qimuxiao.cm");
        System.out.println(ret);
    }
}
class Hash{
    private int cap;
    private int seed;
    public Hash(int cap, int seed){
        this.cap = cap;
        this.seed = seed;
    }

    public int hash(String value){
        int result = 0;
        for (int i=0; i<value.length(); i++) {
            result = seed * result + value.charAt(i);
        }
        return (cap - 1) & result;
    }

}