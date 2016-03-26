package company.handsome.markappproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by handsome on 2016/3/11.
 * 预览图片的相关处理
 */
public class PictureUtils {

    public static Bitmap compress(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options = 100;
        while(baos.toByteArray().length/1024 >100){
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG,options,baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;

    }

    public static Bitmap getImageBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        float srcWidth  = options.outWidth;
        float srcHeight = options.outHeight;
        float hh = 800f;
        float ww = 480f;
        int inSampleSize = 1;
      if(srcWidth>srcHeight&&srcWidth>ww){
          inSampleSize = (int)(srcWidth/ww);
      }else if(srcHeight>srcWidth&&srcHeight>hh){
          inSampleSize = (int)(srcHeight/hh);
      }
        options.inSampleSize = inSampleSize;
       Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return compress(bitmap);
    }


    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable))return;
        BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }

}
