package br.ufc.great.greattour;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class LegendActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.legendscreen);
	}

}
