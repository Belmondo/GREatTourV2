package br.ufc.great.pageslider;

import br.ufc.great.greattour.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */
@SuppressLint("ValidFragment")
public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    int imageSource;


	public TestFragment(int imageSource) {
		this.imageSource = imageSource;

	}
	public TestFragment() {

	}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	imageSource = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup root = (ViewGroup) inflater.inflate(R.layout.page, null);
		ImageView image = (ImageView)root.findViewById(R.id.slider_image);
		image.setImageResource(imageSource);
		setRetainInstance(true);
        return root;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, imageSource);
    }
}
