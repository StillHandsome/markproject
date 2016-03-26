package company.handsome.markappproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 *  mark详情界面
 * mainactivity的启动模式设置为singleTask后,在此activity点击home键后，该markActivity被销毁，
 * 回到main界面，并且有新的mark添加。重启程序后消失。怀疑和home键的机制有关。
 */
public class MarkFragment extends Fragment {
    public static final String EXTRA_MARK_ID = "com.bignerdranch.android.mark_id";
    private Mark mMark;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mItemDone;
    private Button mItemBack;
    private Button mPhotoTake;
    private Button mPhotoChoose;
    private Button mDeleteItem;
    private Switch mTopSwitch;
    private Uri imageUri;
    private ImageView m;
    private Button mCategory;
    private EditText mContent;
    private boolean isAdd = false;
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int REQUEST_SELECT_CATEGORY = 3;
    private static final String TAG = "MarkFragment";
    private static final String DIALOG_IMAGE = "image";

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //通过ID获取对应的mark
        UUID markID = (UUID)getArguments().getSerializable(EXTRA_MARK_ID);
        mMark = MarkLab.get(getActivity()).getMarks(markID);
        //获取isAdd标志
        isAdd = getActivity().getIntent().getBooleanExtra("isAdd", false);
    }


        @TargetApi(11)
        //视图展示
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_mark,parent,false);
            //设置标签
            TextView textViewLabel = (TextView)v.findViewById(R.id.textView_label);
            if(isAdd){
                textViewLabel.setText("新增标记");
            }else{
                textViewLabel.setText("编辑标记");
            }
            //设置标题
        mTitleField = (EditText)v.findViewById(R.id.editText_title);
            String title = mMark.getTitle();
        mTitleField.setText(title);
            if(!TextUtils.isEmpty(title)){
           mTitleField.setSelection(mMark.getTitle().length());}

            //设置日期按钮
        mDateButton = (Button)v.findViewById(R.id.button_date);
            //获取日期
        updateDate();
            //点击日期按钮修改日期
       mDateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FragmentManager fragmentManager = getActivity().getFragmentManager();
               DatePickerFragment dialog = DatePickerFragment.newInstance(mMark.getDate());
               dialog.setTargetFragment(MarkFragment.this, REQUEST_DATE);
               dialog.show(fragmentManager, DIALOG_DATE);

           }
       });
    //完成按钮
        mItemDone = (Button)v.findViewById(R.id.button_item_done);
        mItemDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置修改后的标题以及内容，并存入json
                mMark.setTitle(mTitleField.getText().toString());
                mMark.setContent(mContent.getText().toString());
                mMark.setCategory(mCategory.getText().toString());
                if(mTopSwitch.isChecked()&&!mMark.getTopflag()){
                    Log.e(TAG,"TOP");
                    MarkLab.get(getActivity()).addTopFlag();
                }
                if(mMark.getTopflag()&&!mTopSwitch.isChecked()){
                    Log.e(TAG,"cancleTop");
                    MarkLab.get(getActivity()).cancelTop();
                }
                mMark.setTopflag(mTopSwitch.isChecked());
                MarkLab.get(getActivity()).saveMarks();
                //启动主界面
                Intent intent = new Intent(getActivity(), MarkMainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
    //返回按钮
        mItemBack = (Button)v.findViewById(R.id.button_item_back);
        mItemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //如果是点击添加按钮进入编辑界面的，点击返回会删除无用的mark。点击列表项进入的话，不删除。
                if(isAdd){
                    MarkLab.get(getActivity()).deleteMark(mMark);
                    isAdd = false;
                }
                Intent intent = new Intent(getActivity(),MarkMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
            //置顶
            mTopSwitch  = (Switch)v.findViewById(R.id.switch_top);
            mTopSwitch.setChecked(mMark.getTopflag());
            //拍照按钮
            mPhotoTake = (Button)v.findViewById(R.id.button_takePhoto);
            mPhotoTake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //
                    String filename = UUID.randomUUID().toString()+".jpg";
                    /**
                     *  有一个问题，可能不同品牌的安卓手机的照片路径不同,已经解决，不同则创建目录
                     */
                    String dirPath = Environment.getExternalStorageDirectory()+"/DCIM/Camera";
                    File dir = new File(dirPath);
                    if(!dir.exists()) {
                        dir.mkdir();
                    }
                    File outputImage = new File(dir,filename);
                    try{
                        outputImage.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,REQUEST_PHOTO);
                }
            });
            mPhotoChoose = (Button)v.findViewById(R.id.button_choosePhoto);
            mPhotoChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_CHOOSE_PHOTO);
                }
            });
            m = (ImageView)v.findViewById(R.id.imageView_photo);
            m.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Photo p = mMark.getPhoto();
                    if(p==null){
                        return;
                    }
                    FragmentManager fm = getActivity().getFragmentManager();
                    String path = p.getPath();
                    ImageFragment.newInstance(path)
                            .show(fm,DIALOG_IMAGE);
                }
            });
            //分类按钮
            mCategory = (Button)v.findViewById(R.id.button_selectCategory);
            mCategory.setText(mMark.getCategory());
                mCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(getActivity(),SelectCategoryActivity.class);
                        startActivityForResult(intent,REQUEST_SELECT_CATEGORY);
                    }
                });


            //内容编辑框
            mContent = (EditText)v.findViewById(R.id.editText_content);
            mContent.setText(mMark.getContent());
        //删除按钮，修改标记时可见有效,弹出框确认是否删除
        mDeleteItem  = (Button)v.findViewById(R.id.button_delete);
            mDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity()).setTitle("删除提示")
                            .setMessage("确认删除该标记")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MarkLab.get(getActivity()).deleteMark(mMark);
                                    MarkLab.get(getActivity()).saveMarks();
                                    Intent intent = new Intent(getActivity(), MarkMainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();

                }
            });
            if(isAdd){
            mDeleteItem.setVisibility(View.GONE);
            }else{
                    //删除按钮的view动画效果
                    Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_button_alpha);
                    mDeleteItem.startAnimation(animation);
            }


        return v;
    }
//通过markID构造fragment
    public static MarkFragment newInstance(UUID markId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MARK_ID, markId);
        MarkFragment fragment = new MarkFragment();
        fragment.setArguments(args);
        return  fragment;
    }
//如果修改了日期以及拍照后则更新mark的date以及photo，并在照片预览中展示图片
    public void onActivityResult(int requestCode,int resultCode,Intent intent){

        if(resultCode!=Activity.RESULT_OK)return;
        if(requestCode == REQUEST_DATE){
            Date date = (Date)intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMark.setDate(date);
            updateDate();
        }else if (requestCode == REQUEST_PHOTO) {
                    if(!imageUri.getPath().equals("")){
                    Photo p  = new Photo(imageUri.getPath());
                        mMark.setPhoto(p);}
                displayImage(imageUri.getPath());
        }else if(requestCode == REQUEST_CHOOSE_PHOTO) {
            Uri uri = intent.getData();
            String imagePath = getImagePath(uri, null);
            if(!imagePath.equals("")){
                    Photo p = new Photo(imagePath);
                    mMark.setPhoto(p);
                }
                displayImage(imagePath);
            } else if(requestCode == REQUEST_SELECT_CATEGORY){
            String select = intent.getStringExtra("select");
            mCategory.setText(select);
        }

    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor =  getActivity().getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return  path;
    }
    private void displayImage(String imagePath){
            Bitmap bitmap = PictureUtils.getImageBitmap(imagePath);
            m.setImageBitmap(bitmap);
    }

//设置日期
    public void updateDate(){
        mDateButton.setText(DateFormat.getDateTimeInstance().format(mMark.getDate()));
    }



//启动fragment时展示图片
    public void onStart(){
        super.onStart();
        Photo p = mMark.getPhoto();
        if(p!=null) {
                displayImage(p.getPath());

        }

    }
//清理预览框图片资源
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(m);
    }





}
