package br.ufc.great.pageslider;

import br.ufc.great.greattour.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

class TestFragmentAdapter extends FragmentPagerAdapter  {
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    private int[] offerImages = {
			R.drawable.ctqs1,
			R.drawable.foto,
			R.drawable.great1
	};

    private int mCount = offerImages.length;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TestFragment(offerImages[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TestFragmentAdapter.CONTENT[position % offerImages.length];
    }


    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
