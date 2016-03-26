package company.handsome.markappproject;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by handsome on 2016/3/10.
 * 所有标记的处理类
 */
public class MarkLab  {
    private ArrayList<Mark> mMarks;
    private ArrayList<Mark> mMarksCategory;
    private static MarkLab sMarkLab;
    private Context mAppContext;
    private static final String TAG = "MarkLab";
    private static final String FILENAME = "marks.json";
    private MarkJSONSerializer markSerializer;

    private MarkLab(Context appContext){
        mAppContext = appContext;
        markSerializer =  new MarkJSONSerializer(mAppContext,FILENAME);
        mMarksCategory = new ArrayList<Mark>();//先初始化防止空指针
        try{
            Log.e(TAG,"LOAD");
            mMarks = markSerializer.loadMarks();
        }catch (Exception e){
            mMarks = new ArrayList<Mark>();
            Log.e(TAG,"error loading marks:",e);
        }
    }

    public static MarkLab get(Context c){
        if(sMarkLab == null ){
            sMarkLab = new MarkLab(c.getApplicationContext());
        }
        return sMarkLab;
    }

    public ArrayList<Mark> getMark(){
        Mark topMark = getTopMark();
        if(topMark!=null){
            setTop(topMark);
        }
        Log.e(TAG,"getMark");
        return mMarks;
    }
public ArrayList<Mark> getMark(String category){
    mMarksCategory.clear();
    for(Mark m:mMarks){
        if(m.getCategory().equals(category)){
            mMarksCategory.add(m);
        }
    }
    return mMarksCategory;
}

    public Mark getMarks(UUID id){
        for(Mark m:mMarks){
            if(m.getId().equals(id))
                return m;
        }
        return null;
    }

    public void addMark(Mark m){
        mMarks.add(m);
    }

    public boolean saveMarks(){
        try{
            Log.e(TAG,"SAVE");
            markSerializer.saveMarks(mMarks);
            Log.d(TAG,"marks saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving marks:",e);
            return false;
        }
    }

    public Mark getTopMark(){
        for(Mark m:mMarks){
            if(m.getTopflag()){
                return m;
            }
        }
        return null;
    }
    public void setTop(Mark m){
        //置顶算法  去掉置顶怎么写
        Log.e(TAG,"setTop");
//        Mark temp = m;
        //实际上没有清除m这个对象，只是把它移出list？
        mMarks.remove(m);
        Collections.reverse(mMarks);
        mMarks.add(m);
        Collections.reverse(mMarks);

    }

    public void cancelTop(){
        //取消置顶
    }

    public void addTopFlag(){
        for(Mark mark:mMarks){
            if(mark.getTopflag()){
                mark.setTopflag(false);
            }
        }
    }
    public void deleteMark(Mark m){
        mMarks.remove(m);
    }
}
