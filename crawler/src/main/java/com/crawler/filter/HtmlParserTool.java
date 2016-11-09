package com.crawler.filter;

import com.crawler.util.SpiderUrlQueue;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by guanzhenye on 2016/11/9.
 * 解析html的核心类，爬虫最为重要的部分
 */
public class HtmlParserTool {

    //定义过滤的filter
    private static NodeFilter frameFilter(){

        //结点过滤 过滤frame的src属性
        NodeFilter nodeFilter=new NodeFilter() {
            @Override
            public boolean accept(Node node) {
                if (node.getText().startsWith("frame src=")) {
                    return true;
                }else {
                    return false;
                }
            }
        };
        return nodeFilter;
    }

    //图片
    private static NodeFilter imageFilter(){

        NodeFilter imgFilter=new TagNameFilter("img");
        return imgFilter;
    }


    //遍历结点 只要遍历a标签和frame标签内的链接
    private static void forEachNode(NodeList nodeList,Queue<String> extractLink,LinkFilter filter){
        for(int i=0;i<nodeList.size();i++){
            Node tag=nodeList.elementAt(i);
            //a 标签
            if(tag instanceof LinkTag){
                LinkTag linkTag=(LinkTag)tag;
                //得到链接
                String linkUrl=linkTag.getLink();
                //是否符合条件的linkTag,如果是，放在队列中
                if(filter.accept(linkUrl)){
                    extractLink.add(linkUrl);
                }
            }else{       //frame/img标签

                //<frame src="http://www.baidu.com/" style="">
                //<img src="">
                String frame=tag.getText();
                int start=frame.indexOf("src=");
//                    frame=frame.substring(satrt);
                int end=frame.indexOf(" ");
                //src后面没有其他属性，则到了>符号
                if(end==-1){
                    end=frame.indexOf(">");
                }

                if(start!=-1&&end!=-1&&(start<end)) {
                    String frameUrl = frame.substring(start, end - 1);
                    extractLink.add(frameUrl);
                }
            }
        }
    }

    //遍历图片结点
    private static void forEachImgNode(NodeList nodeList,Queue<String> extractLink,LinkFilter filter){

        for(int i=0;i<nodeList.size();i++){
            Node tag=nodeList.elementAt(i);

            if(tag!=null){
                if(tag instanceof ImageTag){
                    ImageTag imageTag=(ImageTag) tag;
                    String imageUrl=imageTag.getImageURL();
                    if(filter.accept(imageUrl)) {
                        extractLink.add(imageUrl);
                    }
                }
            }
        }
    }

    //解析html页面，获取页面上的链接
    public static Queue<String> extractLinks(String url, LinkFilter filter){

        Queue<String> extractLink=new LinkedList<>();
        try {
            //html解析器
            Parser parser=new Parser(url);
            parser.setEncoding("utf-8");

            //结点过滤 过滤frame标签的src属性
            NodeFilter frameFilter=frameFilter();

            //orFilter 过滤a标签和img标签和input和frame标签
            OrFilter LinkOrImgFilter=new OrFilter(new NodeClassFilter(LinkTag.class),new NodeClassFilter(ImageTag.class));
            OrFilter inputOrLinkOrImgFilterFilter=new OrFilter(new NodeClassFilter(InputTag.class),LinkOrImgFilter);
            OrFilter finalFilter=new OrFilter(inputOrLinkOrImgFilterFilter,frameFilter);

            //解析器根据过滤条件，得到所有过滤的标签
            NodeList nodeList=parser.extractAllNodesThatMatch(finalFilter);

            //遍历标签
            for(int i=0;i<nodeList.size();i++){
                Node tag=nodeList.elementAt(i);
                //a 标签
                if(tag instanceof LinkTag){
                    LinkTag linkTag=(LinkTag)tag;
                    //得到链接
                    String linkUrl=linkTag.getLink();
                    //是否符合条件的linkTag,如果是，放在队列中
                    if(filter.accept(linkUrl)){
                        extractLink.add(linkUrl);
                    }
                }else if(tag instanceof ImageTag){  //img 标签
                    ImageTag imageTag=(ImageTag) tag;
                    String imageUrl=imageTag.getImageURL();
                    if(filter.accept(imageUrl)) {
                        extractLink.add(imageUrl);
                    }
                }else if(tag instanceof InputTag){ //input标签
                    InputTag inputTag=(InputTag)tag;
                    String inputUrl=inputTag.getAttribute("src");
//                    System.out.println("inputTag src="+inputUrl);
                    if(inputUrl!=null&&filter.accept(inputUrl)) {
                        extractLink.add(inputUrl);
                    }
                } else{       //frame/img标签
                    //<frame src="http://www.baidu.com/" style=""
                    String frame=tag.getText();
                    int start=frame.indexOf("src=");
//                    frame=frame.substring(satrt);
                    int end=frame.indexOf(" ");
                    //src后面没有其他属性，则到了>符号
                    if(end==-1){
                        end=frame.indexOf(">");
                    }
                    if(start!=-1&&end!=-1&&(start<end)) {
                        String frameUrl = frame.substring(start, end - 1);
                        extractLink.add(frameUrl);
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
            return extractLink;
        }
        return extractLink;
    }
}
