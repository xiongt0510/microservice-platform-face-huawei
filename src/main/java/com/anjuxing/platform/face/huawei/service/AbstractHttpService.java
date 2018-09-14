package com.anjuxing.platform.face.huawei.service;

import com.cloud.sdk.http.HttpMethodName;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;


/**
 * @author xiongt
 * @Description
 */
public abstract class AbstractHttpService {

    protected HttpResponse template(String url,HttpMethodName httpMethod,String contents) throws Exception {

        //创建request
        HttpRequestBase request = createRequest(url,httpMethod);

        addHeader(request);

        addContent(request,contents);

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null,new TrustSelfSignedStrategy())
                .useTLS().build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,new AllowAllHostnameVerifier());

        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

        HttpResponse response =  client.execute(request);

        return response;

    }

    private HttpRequestBase createRequest(String url,HttpMethodName httpMethod){
        HttpRequestBase httpRequestBase;

        if (httpMethod == HttpMethodName.POST){
            httpRequestBase = new HttpPost(url);
        }  else if (httpMethod == HttpMethodName.PUT){
            httpRequestBase = new HttpPut(url);
        } else if (httpMethod == HttpMethodName.PATCH){
            httpRequestBase = new HttpPatch(url);
        } else if (httpMethod == HttpMethodName.GET){
            httpRequestBase = new HttpGet(url);
        } else if (httpMethod == HttpMethodName.DELETE){
            httpRequestBase = new HttpDelete(url);
        } else if (httpMethod == HttpMethodName.HEAD){
            httpRequestBase = new HttpHead(url);
        } else {
            throw new RuntimeException("Unknown HTTP method name:" + httpMethod);
        }
        return httpRequestBase;

    }

    protected void addHeader(HttpRequestBase request){
        request.addHeader("Content-Type","application/json");
    }

    protected  void addContent(HttpRequestBase request){
        addContent(request,"");
    }

    protected abstract void addContent(HttpRequestBase request,String content);


    protected String httpEntityData(HttpResponse response) throws IOException {
        HttpEntity httpEntity = response.getEntity();

        String resp =  EntityUtils.toString(httpEntity,"UTF-8");

        return resp;
    }


}
