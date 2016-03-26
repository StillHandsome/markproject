package company.handsome.markappproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    ActivityCollector.addActivity(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}