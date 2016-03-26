package company.handsome.markappproject;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;

public class CategoryLab  {
    private ArrayList<Category> mCategories;
    private static CategoryLab sCategoryLab;
    private static final String TAG = "CategoryLab";
    private static final String FILENAME = "categories.json";
    private CategoryJSONSerializer Serializer;
    private Context mContext;
    private String categoryStr;
    private String[] cate;

    private CategoryLab(Context appContext){
        categoryStr = "全部";
        mContext = appContext;
        Serializer =  new CategoryJSONSerializer(mContext,FILENAME);
        try{

           mCategories =Serializer.loadCategories();

        }catch (Exception e){
            mCategories = new ArrayList<Category>();
            Log.e(TAG, "error loading marks:", e);
        }
    }

    public static CategoryLab get(Context c){
        if(sCategoryLab == null ){
            sCategoryLab = new CategoryLab(c.getApplicationContext());
        }
        return sCategoryLab;
    }

    public String[] getCategories(){
        categoryStr="全部";
        for (Category c:mCategories){
            categoryStr+=","+c.getCategory();
        }
        cate = categoryStr.split(",");
        return cate;
    }

    public ArrayList<Category> getCategory(){
        return mCategories;
    }


    public boolean saveCategories(){
        try{
            Serializer.saveCategories(mCategories);
            Log.d(TAG,"marks saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving marks:",e);
            return false;
        }
    }

    public void deleteCategory(Category c){
        mCategories.remove(c);
    }
}

