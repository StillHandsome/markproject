package company.handsome.markappproject;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
    private static final String JSON_NAME = "name";
    private String Category;

    public Category(){
    }

    public void setCategory(String category){
        Category = category;
    }

    public String getCategory(){
        return Category;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_NAME,Category);
        return json;
    }

    public Category(JSONObject json) throws JSONException{
            Category= json.getString(JSON_NAME);

    }

}
