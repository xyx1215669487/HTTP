package com.example.networktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private static final int SHOW_RESPONSE=0;

	private Button sendRequest;

	private TextView responesText;
	
	private Handler handler =new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response=(String) msg.obj;
				responesText.setText(response);
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendRequest=(Button) findViewById(R.id.send_request);
		responesText=(TextView) findViewById(R.id.response);
		sendRequest.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.send_request){
//			sendRequestWithHttpURLConnection();
			sendRequestWithHttpClient();
			Log.e("XIEYUXING", "--->点击");
		}
	}
	
	/**
	 * HttpClient
	 */
	private void sendRequestWithHttpClient(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient=new DefaultHttpClient();
					HttpGet httpGet=new HttpGet("http://www.baidu.com");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					Log.e("XIEYUXING", "---->"+httpResponse.getStatusLine().getStatusCode());
					if(httpResponse.getStatusLine().getStatusCode()==200){
						Log.e("XIEYUXING", "---->连接成功。。。。");
						HttpEntity entity=httpResponse.getEntity();
						String response=EntityUtils.toString(entity,"UTF-8");
						Message msg=new Message();
						msg.what=SHOW_RESPONSE;
						msg.obj=response.toString();
						handler.sendMessage(msg);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	/**
	 * HttpURLConnection
	 */
	private void sendRequestWithHttpURLConnection(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try {
					URL url=new URL("http://www.baidu.com");
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new 
							InputStreamReader(in));
					StringBuilder response =new StringBuilder();
					String line ;
					while((line = reader.readLine())!=null){
						Log.e("XIEYUXING", "--->"+line);
						response.append(line);
					}
					Message msg =new Message();
					msg.what=SHOW_RESPONSE;
					msg.obj=response.toString();
					handler.sendMessage(msg);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
