package cn.konglingwen.new_networktest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.konglingwen.new_networktest.util.Data_analyse;
import cn.konglingwen.new_networktest.util.HttpUtil;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final int SHOW_RESPONSE = 0;
    private Button sendRequest;
    private TextView responseText;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    responseText.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        String address_xml = "https://raw.githubusercontent.com/deank90/test_app/master/china_cities.xml";
        if (v.getId() == R.id.send_request){
            HttpUtil.sendHttpRequest(address_xml, new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Data_analyse.parseXMLWithPull(response);
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
