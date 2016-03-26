package company.handsome.markappproject;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.UUID;

/**
 * 标记的基本类，所有标记的抽象化
 */
public class Mark {
    //JSON文件中的键名
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_CATEGORY = "category";
    private static final String JSON_TOPFLAG = "topflag";
    //标记的所有成员
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Photo mPhoto;
    private String mContent;
    private String mCategory;
    private boolean mTopflag;
    //构造方法
    public Mark(){
        //生成唯一标识符
        mId = UUID.randomUUID();
        //日期为新增标记的日期
        mDate = new Date();
        mTopflag = false;
    }
    //各个成员的getter方法以及setter方法
    public UUID getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date date){
        mDate = date;
    }

    public Photo getPhoto(){
        return mPhoto;
    }

    public void setPhoto(Photo p){
        mPhoto =  p;
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

    public void setCategory(String category){mCategory = category;}

    public String getCategory(){return mCategory;}

    public void setTopflag(boolean flag){
        mTopflag = flag;
    }
    public boolean getTopflag(){return mTopflag;}


    //把标记存入json文件
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_DATE,mDate.getTime());
        json.put(JSON_CONTENT,mContent);
        json.put(JSON_CATEGORY,mCategory);
        json.put(JSON_TOPFLAG,mTopflag);
        if(mPhoto!=null){
            json.put(JSON_PHOTO,mPhoto.toJSON());
        }
        return json;
    }
//从json文件中取出标记
    public Mark(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        mDate = new Date(json.getLong(JSON_DATE));
        mTopflag = json.getBoolean(JSON_TOPFLAG);
        if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if(json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        if(json.has(JSON_CONTENT)){
            mContent = json.getString(JSON_CONTENT);
        }
        if(json.has(JSON_CATEGORY)){
            mCategory= json.getString(JSON_CATEGORY);
        }

    }

    //添加toString方法，覆盖父类的toString方法
    public String toString(){
        return mId.toString()+","+mTitle+","+mDate.toString()+","+mCategory+","+mTopflag+","+mContent+","+mPhoto.toString();
    }



}
