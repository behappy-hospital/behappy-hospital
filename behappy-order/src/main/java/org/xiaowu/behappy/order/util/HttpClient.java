package org.xiaowu.behappy.order.util;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xiaowu.behappy.order.config.WxConfigProperties;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * http请求客户端
 * @author xiaowu
 */
public class HttpClient {

   private String url;
   private Map<String, String> param;
   private int statusCode;
   private String content;
   private String xmlParam;
   private boolean isHttps;
   private boolean isCert = false;
   //证书密码 微信商户号（mch_id）
   private String certPassword;
   public boolean isHttps() {
      return isHttps;
   }
   public void setHttps(boolean isHttps) {
      this.isHttps = isHttps;
   }
   public boolean isCert() {
      return isCert;
   }
   public void setCert(boolean cert) {
      isCert = cert;
   }

   public String getXmlParam() {
      return xmlParam;
   }
   public void setXmlParam(String xmlParam) {
      this.xmlParam = xmlParam;
   }
   public HttpClient(String url, Map<String, String> param) {
      this.url = url;
      this.param = param;
   }
   public HttpClient(String url) {
      this.url = url;
   }
   public String getCertPassword() {
      return certPassword;
   }
   public void setCertPassword(String certPassword) {
      this.certPassword = certPassword;
   }
   public void setParameter(Map<String, String> map) {
      param = map;
   }
   public void addParameter(String key, String value) {
      if (param == null) {
         param = new HashMap<String, String>();
      }
      param.put(key, value);
   }
   public void post() throws ClientProtocolException, IOException {
      HttpPost http = new HttpPost(url);
      setEntity(http);
      execute(http);
   }
   public void put() throws ClientProtocolException, IOException {
      HttpPut http = new HttpPut(url);
      setEntity(http);
      execute(http);
   }
   public void get() throws ClientProtocolException, IOException {
      if (param != null) {
         StringBuilder url = new StringBuilder(this.url);
         boolean isFirst = true;
         for (String key : param.keySet()) {
            if (isFirst) {
               url.append("?");
            } else {
               url.append("&");
            }
            url.append(key).append("=").append(param.get(key));
            isFirst = false;
         }
         this.url = url.toString();
      }
      HttpGet http = new HttpGet(url);
      execute(http);
   }
   /**
    * set http post,put param
    */
   private void setEntity(HttpEntityEnclosingRequestBase http) {
      if (param != null) {
         List<NameValuePair> nvps = new LinkedList<NameValuePair>();
         for (String key : param.keySet()) {
            nvps.add(new BasicNameValuePair(key, param.get(key))); // 参数
         }
         http.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8)); // 设置参数
      }
      if (xmlParam != null) {
         http.setEntity(new StringEntity(xmlParam, Consts.UTF_8));
      }
   }
   private void execute(HttpUriRequest http) throws ClientProtocolException,
         IOException {
      CloseableHttpClient httpClient = null;
      try {
         if (isHttps) {
            if(isCert) {
               KeyStore keystore = KeyStore.getInstance("PKCS12");
               char[] partnerId2charArray = certPassword.toCharArray();
               WxConfigProperties wxConfigProperties = SpringUtil.getBean(WxConfigProperties.class);
               // 这里取得classpath
               Resource certResource = new ClassPathResource(wxConfigProperties.getCert());
               keystore.load(certResource.getInputStream(), partnerId2charArray);
               SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build();
               SSLConnectionSocketFactory sslsf =
                     new SSLConnectionSocketFactory(sslContext,
                           new String[] { "TLSv1" },
                           null,
                           SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
               httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } else {
               SSLContext sslContext = new SSLContextBuilder()
                     .loadTrustMaterial(null, new TrustStrategy() {
                        // 信任所有
                        @Override
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType)
                              throws CertificateException {
                           return true;
                        }
                     }).build();
               SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                     sslContext);
               httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                     .build();
            }
         } else {
            httpClient = HttpClients.createDefault();
         }
         try (CloseableHttpResponse response = httpClient.execute(http)) {
            if (response != null) {
               if (response.getStatusLine() != null) {
                  statusCode = response.getStatusLine().getStatusCode();
               }
               HttpEntity entity = response.getEntity();
               // 响应内容
               content = EntityUtils.toString(entity, Consts.UTF_8);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         httpClient.close();
      }
   }
   public int getStatusCode() {
      return statusCode;
   }
   public String getContent() throws ParseException, IOException {
      return content;
   }
}
