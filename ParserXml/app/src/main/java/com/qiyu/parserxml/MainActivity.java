package com.qiyu.parserxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = (Button) findViewById(R.id.btn);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttp();
            }
        });
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request reques = new Request.Builder()
                            .url("http://10.0.2.2/xml_data.xml")
                            .build();
                    Response response = client.newCall(reques).execute();
                    String responseData = response.body().string();
                    parseXMLWithPull(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            String id = "";
            String name = "";
            String sex = "";
            int eventType = xmlPullParser.getEventType();//得到当前的解析事件
            while (eventType != XmlPullParser.END_DOCUMENT) {//如果当前解析事件不等于这个，说明解析工作还未完成
                String nodeName = xmlPullParser.getName();//得到当前节点名字
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();//获取节点内的具体内容
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("sex".equals(nodeName)) {
                            sex = xmlPullParser.nextText();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("person".equals(nodeName)) {
                            Log.d("MainActivity", "id is  " + id);
                            Log.d("MainActivity", "name is  " + name);
                            Log.d("MainActivity", "sex is " + sex);
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();//获取下一个解析事件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
