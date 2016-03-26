package company.handsome.markappproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;


/**
 * Created by handsome on 2016/3/10.
 * 超类，适合只有一个fragment的activity继承，作用是创建一个fragment
 */
public abstract  class SingleFragmentActivity extends BaseActivity {
    protected abstract Fragment createFragment();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
}
