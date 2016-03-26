package company.handsome.markappproject;


import android.app.Fragment;
import android.content.Intent;

import java.util.UUID;
//标记项的活动
public class MarkActivity extends SingleFragmentActivity {
    boolean isAdd = false;
    //获取传过来的标记ID
   protected Fragment createFragment(){
       UUID markId = (UUID)getIntent().getSerializableExtra(MarkFragment.EXTRA_MARK_ID);
       return MarkFragment.newInstance(markId);
   }

    public void onBackPressed(){
        UUID markId = (UUID)getIntent().getSerializableExtra(MarkFragment.EXTRA_MARK_ID);
        isAdd = getIntent().getBooleanExtra("isAdd", false);
        if(isAdd){
            MarkLab.get(this).deleteMark(MarkLab.get(this).getMarks(markId));
            isAdd = false;
        }
        Intent intent = new Intent(this,MarkMainActivity.class);
        startActivity(intent);
        finish();
    }

}
