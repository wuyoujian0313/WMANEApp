package com.weimeitc.wmaneapp;



import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
//import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.wmtc.wmane.*;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private ActionSheet actionSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedManager sharedManager = SharedManager.getSingleton();
                sharedManager.regiterSharedSDK(MainActivity.this);

                SharedManager.SharedDataModel model = SharedManager.getSingleton().new SharedDataModel();

                model.dataType = SharedManager.E_SharedDataType.SharedDataTypeText;
                model.content = "demoTest";
                SharedManager.getSingleton().sharedData(model, new SharedManager.SharedFinishCallback() {
                    @Override
                    public void finishSharedCallback(int statusCode, Object resp) {
                        //
                    }
                });
            }
        });

    }
}
