package com.crawler.download;

import com.crawler.filter.HtmlParserTool;
import com.crawler.filter.LinkFilter;
import com.crawler.util.SpiderUrlQueue;
import com.crawler.util.VisiterUrlSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by guanzhenye on 2016/11/9.
 */
public class Crawler {

    private String[] seeds;
    private int crawlCount;

    public Crawler(String[] seeds, int count) {
        this.seeds = seeds;
        this.crawlCount = count;
    }

    private void init() {
        for (int i = 0; i < seeds.length; i++) {
            SpiderUrlQueue.addQueue(seeds[i]);
        }
    }

    //属于图片的url
    private boolean imageUrlFilter(String url) {

        int dot = url.lastIndexOf(".");
        if (dot != -1) {
            String suffix = url.substring(dot + 1);
            if ("jpeg".equals(suffix) || "png".equals(suffix) || "jpg".equals(suffix) || "bmp".equals(suffix) || "gif".equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    public void crawl() {
        init();
        String popUrl = null;
        int count = 0;
        //爬取结束条件
        while (VisiterUrlSet.visitedUrlSize() < crawlCount && !SpiderUrlQueue.isEmpty()) {
            //出列
            popUrl = SpiderUrlQueue.popQueue();
            System.out.println("popUrl===" + (count++) + "队列大小=" + SpiderUrlQueue.queueSize() + "===" + popUrl);
            //不为空并且没有爬过，如果爬过，可能导致死循环
            if ((popUrl != null) && (!VisiterUrlSet.isContainedUrl(popUrl))) {
                Queue<String> extractLink = HtmlParserTool.extractLinks(popUrl, new LinkFilter() {
                    //过滤条件
                    @Override
                    public boolean accept(String url) {
                        //运行所有的url
                        return true;
                    }
                });
                //入列
                SpiderUrlQueue.addAllqueue(extractLink);
                //访问过的url放进visitedSet集合中 过滤条件
                VisiterUrlSet.addUrl(popUrl);

            }//if

        }//while

        //下载访问过的url内容
        Iterator<String> it = VisiterUrlSet.visitedUrlSet.iterator();
        System.out.println("visitedUrlSet Size=" + VisiterUrlSet.visitedUrlSize());
        int downloadCount=0;
        while (it.hasNext()) {
            String imageURl = it.next();

            //只下载图片
            if (imageUrlFilter(imageURl)) {
                FileDownlaoder.downloadFile(imageURl);
                downloadCount++;
            }
        }//while
        System.out.print("下载的数量为："+downloadCount);
    }//crawl
}
