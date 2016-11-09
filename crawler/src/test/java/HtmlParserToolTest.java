import com.crawler.filter.HtmlParserTool;
import com.crawler.filter.LinkFilter;
import org.junit.Test;

import java.util.Iterator;
import java.util.Queue;

/**
 * Created by guanzhenye on 2016/11/9.
 * htmlParser解析测试
 * 测试是否能够根据一个url爬去响应页面上的url连接
 */
public class HtmlParserToolTest {

    @Test
    public void testParser(){

        Queue<String> queue=HtmlParserTool.extractLinks("http://nba.hupu.com/", new LinkFilter() {
            @Override
            public boolean accept(String url) {
                return true;
            }
        });

        System.out.println(queue.size());
        Iterator<String> it=queue.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }

    }
}
