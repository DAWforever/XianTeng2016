package cn.edu.nju.iip.spider;

import org.json.JSONObject;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

import org.json.JSONObject;

/**
 * 本教程演示了如何自定义http请求
 *
 * 有些爬取任务中，可能只有部分URL需要使用POST请求，我们可以利用2.20版本中添 加的MetaData功能，来完成POST请求的定制。
 *
 * 使用MetaData除了可以标记URL是否需要使用POST，还可以存储POST所需的参数信息
 *
 * 教程中还演示了如何定制Cookie、User-Agent等http请求头信息
 *
 * WebCollector中已经包含了org.json的jar包
 *
 * @author hu
 */
public class DemoPostCrawler extends BreadthCrawler {

    public DemoPostCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request = new HttpRequest(crawlDatum.getUrl());
        request.setCookie("wzwsconfirm=96c7a9555173d25a578c639616055486; wzwstemplate=MQ==; wzwschallenge=-1; ccpassport=f1c447d906bab9bdec31812485e54de7; yunsuo_session_verify=2c2aba50885bee04dae4ab3d4a42db40; _gscu_2116842793=58719985yy6gkp84; _gscs_2116842793=587199853t44m284|pv:4; _gscbrs_2116842793=1; _gsref_2116842793=http://wenshu.court.gov.cn/");
        request.addHeader("Accept", "*/*");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        request.addHeader("Connection", "keep-alive");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.addHeader("Host", "wenshu.court.gov.cn");
        request.addHeader("Referer", "http://wenshu.court.gov.cn/list/list/?sorttype=1&conditions=searchWord+QWJS+++%E5%85%A8%E6%96%87%E6%A3%80%E7%B4%A2:%E6%B5%99%E6%B1%9F%E4%B8%9C%E6%96%B9%E5%B8%82%E6%94%BF%E5%9B%AD%E6%9E%97%E5%B7%A5%E7%A8%8B%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        request.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
        request.setMethod(crawlDatum.getMetaData("method"));
        String outputData=crawlDatum.getMetaData("outputData");
        if(outputData!=null){
            request.setOutputData(outputData.getBytes("utf-8"));
        }
        return request.getResponse();
        /*
        //通过下面方式可以设置Cookie、User-Agent等http请求头信息
        request.setCookie("xxxxxxxxxxxxxx");
        request.setUserAgent("WebCollector");
        request.addHeader("xxx", "xxxxxxxxx");
        */
    }

    public void visit(Page page, CrawlDatums next) {
        String jsonStr = page.getHtml();
       // JSONObject json = new JSONObject(jsonStr);
        System.out.println("JSON信息：" + jsonStr);
    }

    /**
     * 假设我们要爬取三个链接 1)http://www.A.com/index.php 需要POST，并且需要附带数据id=a
     * 2)http://www.B.com/index.php?id=b 需要POST，不需要附带数据 3)http://www.C.com/
     * 需要GET
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        DemoPostCrawler crawler = new DemoPostCrawler("json_crawler", true);
        crawler.addSeed(new CrawlDatum("http://wenshu.court.gov.cn/List/ListContent").putMetaData("method", "POST")
                .putMetaData("outputData", "Param=&Index=1&Page=5&Order=%E6%B3%95%E9%99%A2%E5%B1%82%E7%BA%A7&Direction=asc"));
        crawler.start(1);
    }

}