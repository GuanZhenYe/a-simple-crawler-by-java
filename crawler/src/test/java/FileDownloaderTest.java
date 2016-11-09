import com.crawler.download.FileDownlaoder;
import org.junit.Test;

/**
 * Created by guanzhenye on 2016/11/8.
 */
public class FileDownloaderTest {

    @Test
    public void testDownload(){

        FileDownlaoder downlaoder=new FileDownlaoder();
        downlaoder.downloadFile("http://tse1.mm.bing.net/th?&id=OIP.M34e88050f1c65744f7f485dcb80cb234o0&w=300&h=187&c=0&pid=1.9&rs=0&p=0&r=0");
    }
}
