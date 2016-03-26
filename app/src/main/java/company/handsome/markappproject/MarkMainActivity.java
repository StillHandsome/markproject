package company.handsome.markappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

public class MarkMainActivity extends BaseActivity {
    private ArrayList<Mark> mMarks;
    private ArrayList<Mark> mMarksCategory;
    private MarkAdapter adapter;
    private MarkAdapter adapterCategory;
    private ListView listView;
    private Button button_setting;
    private Button button_add;
    private Spinner spinner_category;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onStart(){
        super.onStart();
        mMarks = MarkLab.get(this).getMark();
        button_add = (Button)findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mark mark = new Mark();
                MarkLab.get(getApplicationContext()).addMark(mark);
               Intent intent = new Intent(MarkMainActivity.this,MarkActivity.class);
                intent.putExtra(MarkFragment.EXTRA_MARK_ID,mark.getId());
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });
        button_setting = (Button)findViewById(R.id.button_setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MarkMainActivity.this,MarkSettingActivity.class);
                startActivity(i);
            }
        });

        listView = (ListView)findViewById(R.id.listView_markList);
        adapter = new MarkAdapter(this,R.layout.list_item_mark,mMarks);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mark m = mMarks.get(position);
                //start MarkActivity
                Intent intent = new Intent(MarkMainActivity.this, MarkActivity.class);
                //必须传入标记的ID
                intent.putExtra(MarkFragment.EXTRA_MARK_ID, m.getId());
                startActivity(intent);
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_mark_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_list_delete_mark:

                        MarkLab markLab = MarkLab.get(getApplicationContext());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (listView.isItemChecked(i)) {
                                markLab.deleteMark(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        //刷新列表数据
                        adapter.notifyDataSetChanged();
                        //删除后更新json文件
                        MarkLab.get(getApplicationContext()).saveMarks();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//
            }
        });
//下拉选择菜单
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
       String[] arr = CategoryLab.get(this).getCategories();
        ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,arr);
        spinner_category.setAdapter(adapter_category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spinner_category.getSelectedItem().toString();
                if (category.equals("全部")) {
                    listView.setAdapter(adapter);
                } else {
                    mMarksCategory = MarkLab.get(MarkMainActivity.this).getMark(category);
                    adapterCategory = new MarkAdapter(MarkMainActivity.this, R.layout.list_item_mark, mMarksCategory);
                    listView.setAdapter(adapterCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    //适配器的类，列表显示的内容由它决定
    private class MarkAdapter extends ArrayAdapter<Mark> {
        private int resourceId;
        public MarkAdapter(Context context,int textViewResourceId,ArrayList<Mark> marks){
            super(context,textViewResourceId,marks);
            resourceId = textViewResourceId;
        }
        public View getView(int position,View convertView,ViewGroup parent){
            ViewHolder holder =null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
                holder.title = (TextView)convertView.findViewById(R.id.textView_title);
                holder.date = (TextView)convertView.findViewById(R.id.textView_date);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();

            }
            //显示标题和时间
            Mark m = getItem(position);
            holder.title.setText(m.getTitle());
            holder.date.setText(DateFormat.getDateTimeInstance().format(m.getDate()));
            return convertView;
        }

        public final class ViewHolder{
            public TextView title;
            public TextView date;
        }
    }

    public void onBackPressed(){
        ActivityCollector.finishAll();
    }


}
