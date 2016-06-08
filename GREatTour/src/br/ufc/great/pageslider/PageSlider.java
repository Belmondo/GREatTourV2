package br.ufc.great.pageslider;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.graphics.Color;




import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import br.ufc.great.greattour.R;
import br.ufc.great.greattour.R.id;
import br.ufc.great.greattour.R.layout;
import br.ufc.great.greattour.utils.logger.Logger;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class PageSlider extends FragmentActivity {

	TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.page_slider);

        // LOGGER LOG ENTRY
		Logger.addEntry("Displaying Images");

		 mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);

	        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
	        mIndicator = indicator;
	        indicator.setViewPager(mPager);

	        final float density = getResources().getDisplayMetrics().density;
	        indicator.setBackgroundColor(0xffffffff); //branco
	        indicator.setRadius(5*density);
	        indicator.setPageColor(0xffe79f39);//laranja
	        indicator.setFillColor(0xff007cc2); //azul
	        indicator.setStrokeColor(0xffe79f39);//laranja
	        //indicator.setStrokeWidth(density);
	}



}
