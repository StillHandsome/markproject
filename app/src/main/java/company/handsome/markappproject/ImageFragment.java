package company.handsome.markappproject;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by handsome on 2016/3/11.
 * 点击预览图片后全屏展示图片的fragment
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.android.image_path";

    public static ImageFragment newInstance(String imagePath){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return fragment;
    }

    private ImageView mImageView;

    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        Bitmap bitmap = PictureUtils.getImageBitmap(path);
        mImageView.setImageBitmap(bitmap);
        return mImageView;
    }

    public void onDestroyView(){
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }

}
