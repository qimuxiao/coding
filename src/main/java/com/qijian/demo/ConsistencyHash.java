package com.qijian.demo;

import java.util.*;

public class ConsistencyHash {
    private List<String> serverList = new ArrayList<String>() {
        {
            add("192.168.0.0:1111");
            add("192.168.0.1:1111");
            add("192.168.0.2:1111");
            add("192.168.0.3:1111");
            add("192.168.0.4:1111");
        }
    };
    private TreeMap<Integer,String> nodeSplit;
    public ConsistencyHash(){
        nodeSplit = new TreeMap<Integer, String>();
        for (String server:serverList) {
            int hash = hash(server);
            System.out.println(server + "'s hashcode:" + hash);
            nodeSplit.put(hash,server);
        }
    }

    public int hash(String key){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        //hash有可能为负数，取绝对值
        hash = Math.abs(hash);
        return hash;
    }

    public String getRouterServer(String uuid){
        int hash = hash(uuid);
        System.out.println(uuid + "'s hash code:" + hash);
        int serverHashCode = nodeSplit.firstKey();
        SortedMap<Integer,String> subMap = nodeSplit.tailMap(hash);
        if (subMap.values().size() > 1) {
            serverHashCode = subMap.firstKey();
        }
        String server = nodeSplit.get(serverHashCode);
        System.out.println(uuid + "'s server is:" + server);
        return server;
    }

    public static void main(String[] args) {
        ConsistencyHash consistencyHash = new ConsistencyHash();
        for(int i=0;i<100;i++) {
            consistencyHash.getRouterServer(UUID.randomUUID().toString());
        }


    }
}
