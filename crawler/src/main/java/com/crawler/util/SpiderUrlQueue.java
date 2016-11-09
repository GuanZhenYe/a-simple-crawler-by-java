package com.crawler.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by guanzhenye on 2016/11/8.
 * 从一个根url遍历，将爬取的url存放在该队列上
 */
public class SpiderUrlQueue {

    public static Queue<String> spiderUrlQueue=new LinkedList<>();

    //添加队列 末尾添加
    public static void addQueue(String url){
        spiderUrlQueue.offer(url);
    }

    //移除队列 队头移除
    private static void removeQueueFirst(){
        spiderUrlQueue.remove();
    }

    //队列是否为空
    public static boolean isEmpty(){
       return spiderUrlQueue.isEmpty();
    }

    //出列
    public static String popQueue(){
        String url=null;
        if(!isEmpty()){
            url=spiderUrlQueue.poll();
        }
        return url;
    }

    public static void addAllqueue(Collection<String> e){
        spiderUrlQueue.addAll(e);
    }

    public static int queueSize(){
        return spiderUrlQueue.size();
    }
}
