package com.crawler.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by guanzhenye on 2016/11/8.
 * 访问过的url集合
 */
public class VisiterUrlSet {

    public static Set<String> visitedUrlSet=new HashSet<>();

    //添加url到未访问过的集合中
    public static void addUrl(String url){
        visitedUrlSet.add(url);
    }

    //判断没有访问过的url集合中含有参数中的url
    public static boolean isContainedUrl(String url){
        return visitedUrlSet.contains(url);
    }

    public static boolean isEmpty(){

        return visitedUrlSet.isEmpty();
    }

    public static int visitedUrlSize(){
        return visitedUrlSet.size();
    }

}
