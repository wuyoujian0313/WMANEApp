package air.com.weimeitc.bqwx;



import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
//import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wmtc.wmane.SharedSDK.ActionSheet;
import com.wmtc.wmane.SharedSDK.SharedManager;

//air.com.weimeitc.bqwx:039fcbae92e6388b7a9babf728ddf696
//air.com.weimeitc.bqdj:233936a9c7c6ff761eb23e34f0e55ceb

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

                model.title = "test";
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
