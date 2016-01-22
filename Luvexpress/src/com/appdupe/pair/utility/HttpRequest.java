package com.appdupe.pair.utility;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.StrictHostnameVerifier;

import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.appdupe.pair.utility.Base64.InputStream;

import android.R;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import android.util.Log;

/**
 * This class sends your data through GET and POST methods
 * 
 * */
public class HttpRequest {

	HttpClient httpClient;
	HttpContext localContext;
	private String ret;
	static Context context;
	HttpResponse response = null;
	HttpPost httpPost = null;
	HttpGet httpGet = null;
	@SuppressWarnings("rawtypes")
	Map.Entry me;
	@SuppressWarnings("rawtypes")
	Iterator i;

	public HttpRequest() {
		/*HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 50000);
		HttpConnectionParams.setSoTimeout(myParams, 50000);*/
		this.context=context;
		httpClient = createHttpClient();
		localContext = new BasicHttpContext();
	}

	
	public HttpRequest(String test)
	{
		
	}
	public static HttpClient createHttpClient()
	{
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 50000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
//		SSLSocketFactory sf=SSLSocketFactory.getSocketFactory();
//		sf.setHostnameVerifier(new HttpRequest("test").new MyHostnameVerifier());
		
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", new HttpRequest("test").new TlsSniSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	/*public void clearCookies() {
		httpClient.getCookieStore().clear();
	}*/


//	class MyHostnameVerifier implements org.apache.http.conn.ssl.X509HostnameVerifier
//	{
//		@Override
//		public boolean verify(String host, SSLSession session) {
//			String sslHost = session.getPeerHost();
//			System.out.println("Host=" + host);
//			System.out.println("SSL Host=" + sslHost);    
//			if (host.equals(sslHost)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//
//		@Override
//		public void verify(String host, SSLSocket ssl) throws IOException {
//			String sslHost = ssl.getInetAddress().getHostName();
//			System.out.println("Host=" + host);
//			System.out.println("SSL Host=" + sslHost);    
//			if (host.equals(sslHost)) {
//				return;
//			} else {
//				throw new IOException("hostname in certificate didn't match: " + host + " != " + sslHost);
//			}
//		}
//
//
//		@Override
//		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
//			throw new SSLException("Hostname verification 2 not implemented");
//		}
//
//
//		@Override
//		public void verify(String host, X509Certificate cert) throws SSLException {
//			// TODO Auto-generated method stub
//			throw new SSLException("Hostname verification 1 not implemented");
//		}
//	}
	
	
	public class TlsSniSocketFactory implements LayeredSocketFactory {
	    private static final String TAG = "davdroid.SNISocketFactory";

	    HostnameVerifier hostnameVerifier = new StrictHostnameVerifier();


	    // Plain TCP/IP (layer below TLS)


	    @Override
	    public boolean isSecure(Socket s) throws IllegalArgumentException {
	            if (s instanceof SSLSocket)
	                    return ((SSLSocket)s).isConnected();
	            return false;
	    }


	    // TLS layer

	    @Override
	    public Socket createSocket(Socket plainSocket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	            if (autoClose) {
	                    // we don't need the plainSocket
	                    plainSocket.close();
	            }

	            // create and connect SSL socket, but don't do hostname/certificate verification yet
	            SSLCertificateSocketFactory sslSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(0);
	            SSLSocket ssl = (SSLSocket)sslSocketFactory.createSocket(InetAddress.getByName(host), port);

	            // enable TLSv1.1/1.2 if available
	            // (see https://github.com/rfc2822/davdroid/issues/229)
	            ssl.setEnabledProtocols(ssl.getSupportedProtocols());

	            // set up SNI before the handshake
	            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
	                    Log.i(TAG, "Setting SNI hostname");
	                    sslSocketFactory.setHostname(ssl, host);
	            } else {
	                    Log.d(TAG, "No documented SNI support on Android <4.2, trying with reflection");
	                    try {
	                         java.lang.reflect.Method setHostnameMethod = ssl.getClass().getMethod("setHostname", String.class);
	                         setHostnameMethod.invoke(ssl, "www.luvexpress.com");
	                    } catch (Exception e) {
	                            Log.w(TAG, "SNI not useable", e);
	                    }
	            }

	            // verify hostname and certificate
	            SSLSession session = ssl.getSession();
	            if (!hostnameVerifier.verify(host, session))
	                    throw new SSLPeerUnverifiedException("Cannot verify hostname: " + host);

	            Log.i(TAG, "Established " + session.getProtocol() + " connection with " + session.getPeerHost() +
	                            " using " + session.getCipherSuite());

	            return ssl;
	    }

		@Override
		public Socket connectSocket(Socket sock, String host, int port,
				InetAddress localAddress, int localPort, HttpParams params)
				throws IOException, UnknownHostException,
				ConnectTimeoutException {
			// TODO Auto-generated method stub
			return null;
		}




		@Override
		public Socket createSocket() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	}
	public String sendGet(String url) {
		httpGet = new HttpGet(url);

		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ret = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			Log.e("HttpRequest", "" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public String postData(String url, List<NameValuePair> nameValuePairs)
			throws Exception {
		// Getting the response handler for handling the post response
		ResponseHandler<String> res = new BasicResponseHandler();
		HttpPost postMethod = new HttpPost(url);

		// Setting the data that is to be sent
		postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		// Execute HTTP Post Request
		String response = httpClient.execute(postMethod, res);
		System.out.println("Url"+url);
		System.out.println("responseeeee"+response);
		return response;
	}

}