package com.huajie.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.primefaces.json.JSONObject;

public class HttpClientUtil {
	private static final  Logger logger = Logger.getLogger(HttpClientUtil.class);
	private static String responseMsg=null;
	/**
	 * 通用POST方法
	 * @param url
	 * @param parmMap
	 */
	  public static String post(String url,Map<String,String> paramMap) {
		  // 创建默认的httpClient实例. 
		  CloseableHttpClient httpclient = HttpClients.createDefault();
		  // 创建httppost 
		  HttpPost httppost = new HttpPost(url);
		  // 创建参数队列 
		  List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();
		  for(Map.Entry<String,String> tmpMap : paramMap.entrySet()){
			  postParams.add(new BasicNameValuePair(tmpMap.getKey(), tmpMap.getValue()));
		  }
		  UrlEncodedFormEntity uefEntity;
		  try {
		  	uefEntity = new UrlEncodedFormEntity(postParams, "GBK");
		  	httppost.setEntity(uefEntity);
		  	CloseableHttpResponse response = httpclient.execute(httppost);
		  	if(response.getStatusLine().getStatusCode()!=200){
		  		return "error";
		  	}
		  	try {
		  		HttpEntity entity = response.getEntity();
		  		if (entity != null) {
		  			responseMsg=EntityUtils.toString(entity, "GBK");
		  			return responseMsg;
		  		}
		  	} finally {
		  		response.close();
		  	}
		  } catch (ClientProtocolException e) {
			  e.printStackTrace();
		  } catch (UnsupportedEncodingException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  } finally {
	      // 关闭连接,释放资源 
			  try {
				  httpclient.close();
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		  }
		  return null;
	}
  
	/**
	 * 通用POST方法
	 * @param url
	 * @param parmMap
	 */
	public static String postFile(String url,Map<String,String> textMap,File file) {
		// 创建默认的httpClient实例. 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost 
		HttpPost httppost = new HttpPost(url);
		MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
		// 创建参数队列 
		for(Map.Entry<String,String> tmpMap : textMap.entrySet()){
			multipartEntityBuilder.addTextBody(tmpMap.getKey(), tmpMap.getValue());
		}
		multipartEntityBuilder.addBinaryBody("file", file);
		HttpEntity  httpEntity=multipartEntityBuilder.build();
		try {
			httppost.setEntity(httpEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()!=200){
				return "{'status':'4'}";
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					responseMsg=EntityUtils.toString(entity, "UTF-8");
					return responseMsg;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源 
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 发送 get请求
	 * @return 
	 */
	public static String get(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			URL tmpUrl = new URL(url);
			// 创建httpget.
			HttpGet httpget = new HttpGet();
			httpget.setURI(new URI(tmpUrl.getProtocol(), tmpUrl.getAuthority(), tmpUrl.getPath(), tmpUrl.getQuery(), null));
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			if(response.getStatusLine().getStatusCode()!=200){
				return "error";
			}
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "UTF-8");
				}
			} finally {
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}  finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * HttpClient连接SSL
	 */
	public void ssl() {
		CloseableHttpClient httpclient = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream instream = new FileInputStream(new File("C:\\tomcat.keystore"));
			try {
				// 加载keyStore d:\\tomcat.keystore
				trustStore.load(instream, "123456".toCharArray());
			} catch (CertificateException e) {
				e.printStackTrace();
			} finally {
				try {
					instream.close();
				} catch (Exception ignore) {
				}
			}
			// 相信自己的CA和所有自签名的证书
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
					.build();
			// 只允许使用TLSv1协议
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			// 创建http请求(get方式)
			HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					EntityUtils.consume(entity);
				}
			} finally {
				response.close();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String executeGet(String url) {  
        BufferedReader in = null;  
        String content = null;  
    	CloseableHttpClient  client = HttpClients.createDefault();  
        try {
        	URL tmpUrl=new URL(url);
            // 实例化HTTP方法  
            HttpGet request = new HttpGet();  
            request.setURI(new URI(tmpUrl.getProtocol(), tmpUrl.getAuthority(), tmpUrl.getPath(), tmpUrl.getQuery(), null));  
            CloseableHttpResponse response = client.execute(request);  
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"GBK"));  
            StringBuffer sb = new StringBuffer();  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                sb.append(line);  
            }  
            content = sb.toString();
            response.close();
            return content;  
        }catch (IOException e) {
			e.printStackTrace();
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}   finally { 
            if (in != null) {  
                try {  
                    in.close();// 最后要关闭BufferedReader  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }
            if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
           
        }
		return null;  
    }  

	public static String httpPostWithJSON(String url,JSONObject json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        StringEntity entity = new StringEntity(json.toString(),"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httpPost.setEntity(entity);
        HttpResponse resp = client.execute(httpPost);
        if(resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he,"UTF-8");
        }
        return respContent;
    }

}
