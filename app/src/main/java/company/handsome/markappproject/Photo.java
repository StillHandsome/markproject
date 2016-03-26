package company.handsome.markappproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by handsome on 2016/3/11.
 * 图片类
 */
public class Photo {
    private static final String JSON_PATH = "path";
    private String mPath;

    public Photo(String path) {
        mPath =path;
    }

    public String getPath(){
        return mPath;
    }

    public Photo(JSONObject json) throws JSONException {
        mPath = json.getString(JSON_PATH);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_PATH,mPath);
        return json;
    }

    //添加toString方法，覆盖父类的toString方法
    public String toString(){
        return "path:"+mPath;
    }
}


