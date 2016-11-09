import com.crawler.download.Crawler;
import org.junit.Test;

/**
 * Created by guanzhenye on 2016/11/9.
 */
public class CrawlerTest {

    @Test
    public void testCrawler(){

        Crawler crawler=new Crawler(new String[]{"http://photo.hupu.com/"},100);
        crawler.crawl();
    }
}
