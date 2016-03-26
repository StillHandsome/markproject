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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectCategoryActivity extends BaseActivity {
    private CategoryAdapter adapter;
    private ArrayList<Category> categories;
    private ListView listView;
    private Button add;
    private Button back;
    private EditText categoryText;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        categories = CategoryLab.get(this).getCategory();
        adapter =new CategoryAdapter(this,R.layout.list_item_category,categories);
        listView  = (ListView)findViewById(R.id.listView_selectCategory);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category c = categories.get(position);
                Intent intent = new Intent();
                intent.putExtra("select", c.getCategory());
                setResult(RESULT_OK, intent);
                finish();
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

                        CategoryLab categoryLabLab = CategoryLab.get(getApplicationContext());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (listView.isItemChecked(i)) {
                                categoryLabLab.deleteCategory(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        //刷新列表数据
                        adapter.notifyDataSetChanged();
                        //删除后更新json文件
                        CategoryLab.get(getApplicationContext()).saveCategories();
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
        categoryText = (EditText)findViewById(R.id.editText_category);
        add = (Button)findViewById(R.id.button_addCategory);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!categoryText.getText().toString().equals("")) {
                    String category = categoryText.getText().toString();
                    Category c = new Category();
                    c.setCategory(category);
                    //直接用ArrayList的add方法添加成员
                    categories.add(c);
                    CategoryLab.get(SelectCategoryActivity.this).saveCategories();
                    adapter.notifyDataSetChanged();
                    categoryText.setText("");
                } else {
                    Toast.makeText(SelectCategoryActivity.this,"请输入分类名称",Toast.LENGTH_SHORT).show();
                }

            }
        });
        back = (Button)findViewById(R.id.button_category_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent();
                setResult(RESULT_CANCELED,back);
                finish();
            }
        });



    }


    private class CategoryAdapter extends ArrayAdapter<Category>{
        private int resourceId;
        public CategoryAdapter(Context context,int textViewResourceId,ArrayList<Category> categories){
            super(context,textViewResourceId,categories);
            resourceId = textViewResourceId;
        }
        public View getView(int position,View convertView,ViewGroup parent){
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder.category = (TextView)convertView.findViewById(R.id.textView_category);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            //显示标题和时间
            Category c = getItem(position);
            viewHolder.category.setText(c.getCategory());
            return convertView;
        }
        public final class ViewHolder{
           public TextView category;
        }
    }

}
