package com.crawler.download;


/**
 * Created by guanzhenye on 2016/11/9.
 */
public class Main {

    public static void main(String[] args) {

        Crawler crawler = new Crawler(new String[]{"https://www.zhihu.com/question/40778754"}, 200);
        crawler.crawl();
    }
}
