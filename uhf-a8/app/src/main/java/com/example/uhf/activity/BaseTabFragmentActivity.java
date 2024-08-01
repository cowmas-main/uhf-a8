package com.example.uhf.activity;



import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;

import com.example.uhf.R;
import com.example.uhf.adapter.ViewPagerAdapter;
import com.example.uhf.fragment.KeyDownFragment;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.NoScrollViewPager;
import com.rscja.deviceapi.RFIDWithUHFA8;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-03-10.
 */
public class BaseTabFragmentActivity extends FragmentActivity {
    public KeyDownFragment currentFragment=null;
    public RFIDWithUHFA8 mReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        }
    }

    public void initUHF() {

        try {
            mReader = RFIDWithUHFA8.getInstance();
        } catch (Exception ex) {
            toastMessage(ex.getMessage());
            return;
        }
        if (mReader != null) {
            new InitTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.UHF_ver:
                getUHFVersion();
                break;
            case R.id.UHF_temperature:
                getUHFTemperature();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == 139 || keyCode == 280|| keyCode == 294) {
            if (event.getRepeatCount() == 0) {
                if (currentFragment != null) {
                    currentFragment.myOnKeyDwon();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
                Toast.makeText(BaseTabFragmentActivity.this, "init fail",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(BaseTabFragmentActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    public void getUHFVersion() {
        if (mReader != null) {
            String rfidVer = mReader.getVersion();
            UIHelper.alert(this, R.string.action_uhf_ver, rfidVer, R.drawable.webtext);
        }
    }

    public void getUHFTemperature() {
        if (mReader != null) {
            String msgStr = String.format(getString(R.string.title_about_Temperature), mReader.getTemperature());
            UIHelper.alert(this, R.string.module_temperature, msgStr, R.drawable.webtext);
        }
    }

//---------------
     protected List<KeyDownFragment> lstFrg = new ArrayList<KeyDownFragment>();
    protected List<String> lstTitles = new ArrayList<String>();
    protected ActionBar mActionBar;

    protected NoScrollViewPager mViewPager;
    protected ViewPagerAdapter mViewPagerAdapter;
    protected void initViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), lstFrg, lstTitles);
        mViewPager = (NoScrollViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mViewPagerAdapter);
    }
    protected void initTabs() {
        if(mActionBar != null)
            for (int i = 0; i < mViewPagerAdapter.getCount(); ++i) {
                addTab(i);
            }
    }
    public void addTab(int position) {
        mActionBar.addTab(mActionBar.newTab()
                .setText(getTabTitle(position))
                .setTabListener(mTabListener));
    }
    public CharSequence getTabTitle(int position) {
        if(mViewPagerAdapter != null) {
            return mViewPagerAdapter.getPageTitle(position);
        }
        return null;
    }
    protected android.app.ActionBar.TabListener mTabListener = new android.app.ActionBar.TabListener() {

        @Override
        public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            mViewPager.setCurrentItem(tab.getPosition());
            BaseTabFragmentActivity.this.onTabSelected(tab);
        }

        @Override
        public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            BaseTabFragmentActivity.this.onTabReselected(tab);
        }
    };
    protected void onTabSelected(android.app.ActionBar.Tab tab) {}

    protected void onTabReselected(android.app.ActionBar.Tab tab) {}
}
