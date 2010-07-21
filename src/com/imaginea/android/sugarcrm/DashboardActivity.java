package com.imaginea.android.sugarcrm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.imaginea.android.sugarcrm.provider.DatabaseHelper;

import java.util.List;

/**
 * DashboardActivity
 * 
 * @author Vasavi
 */
public class DashboardActivity extends Activity {

    private GridView mDashboard;

    private List<String> mModuleNames;

    private DatabaseHelper mDbHelper = new DatabaseHelper(this);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Class wizardActivity = WizardDetector.getClass(getBaseContext());
        startActivityForResult(new Intent(this, wizardActivity), 0);

        mModuleNames = mDbHelper.getModuleList();
        
        setContentView(R.layout.dashboard_activity);
        TextView tv = (TextView) findViewById(R.id.headerText);
        tv.setText(R.string.home);
        mDashboard = (GridView) findViewById(R.id.dashboard);
        mDashboard.setAdapter(new AppsAdapter(this));

        mDashboard.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // invoke the corresponding activity when the item in the GridView is clicked
                Intent myIntent;
                String moduleName = mModuleNames.get(position);
                if(moduleName.equals("Settings")){
                    myIntent = new Intent(DashboardActivity.this, SugarCrmSettings.class);
                } else{
                    myIntent = new Intent(DashboardActivity.this, ContactListActivity.class);
                }
                
                myIntent.putExtra(RestUtilConstants.MODULE_NAME, mModuleNames.get(position));
                DashboardActivity.this.startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
            finish();
    }

    public class AppsAdapter extends BaseAdapter {
        private Context mContext;
        
        public AppsAdapter(Context context) {
            mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view; // an item in the GridView
            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.dashboard_item, parent, false);

                String moduleName = mModuleNames.get(position);
                
                ImageView iv = (ImageView) view.findViewById(R.id.moduleImage);
                iv.setImageResource(mDbHelper.getModuleIcon(moduleName));

                TextView tv = (TextView) view.findViewById(R.id.moduleName);
                tv.setText(moduleName);
            } else {
                view = convertView;
            }

            return view;
        }

        public final int getCount() {
            return mModuleNames.size();
        }

        public final Object getItem(int position) {
            return null;
        }

        public final long getItemId(int position) {
            return 0;
        }
        
    }
}