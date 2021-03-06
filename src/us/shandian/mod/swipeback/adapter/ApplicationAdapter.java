package us.shandian.mod.swipeback.adapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Locale;
import java.text.Collator;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;

import us.shandian.mod.swipeback.R;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo>
{
	private List<ApplicationInfo> mAppsList = null;
	private List<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
	private List<View> mViews = new ArrayList<View>();
	private Context mContext;
	private PackageManager mPackageManager;
	
	public ApplicationAdapter(Context context, int textViewResourceId,
	                          List<ApplicationInfo> appsList) {
		super(context, textViewResourceId, appsList);
		this.mContext = context;
		this.mAppsList = appsList;
		mPackageManager = mContext.getPackageManager();
		
		// Sort in alphabetical
		Collections.sort(appsList, new Comparator<ApplicationInfo>() {

				@Override
				public int compare(ApplicationInfo p1, ApplicationInfo p2)
				{
					String name1 = p1.loadLabel(mPackageManager).toString();
					String name2 = p2.loadLabel(mPackageManager).toString();
					return Collator.getInstance().compare(name1, name2);
				}

			
		});
		
		for (int i = 0; i < mAppsList.size(); i++) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
				                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = layoutInflater.inflate(R.layout.item_listview_blacklist, null);
			ApplicationInfo data = mAppsList.get(i);
			if (null != data) {
				TextView appName = (TextView) view.findViewById(R.id.blacklist_app_name);
				ImageView iconview = (ImageView) view.findViewById(R.id.blacklist_app_icon);
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.blacklist_checkbox);

				checkBox.setChecked(true);
				appName.setText(data.loadLabel(mPackageManager));
				iconview.setImageDrawable(data.loadIcon(mPackageManager));

				mCheckBoxes.add(checkBox);
			}
			mViews.add(view);
		}
	}
	
	@Override
	public int getCount() {
		return ((null != mAppsList) ? mAppsList.size() : 0);
	}
	
	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != mAppsList) ? mAppsList.get(position) : null);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (null != mViews.get(position)) {
			view = mViews.get(position);
		}
		
		return view;
	}
	
	public ArrayList<ApplicationInfo> getCheckedItems() {
		ArrayList<ApplicationInfo> retApps = new ArrayList<ApplicationInfo>();
		if (null != mAppsList) {
			ApplicationInfo info;
			CheckBox checkBox;
			for (int i = 0; i < mAppsList.size(); i++) {
				info = mAppsList.get(i);
				checkBox = mCheckBoxes.get(i);
				if (null != checkBox) {
					if (checkBox.isChecked()) {
						retApps.add(info);
					}
				}
			}
		}
		return retApps;
	}
	
	public void invertSeletion() {
		for (int i = 0; i < mCheckBoxes.size(); i++) {
			mCheckBoxes.get(i).setChecked(!mCheckBoxes.get(i).isChecked());
		}
	}
	
	public void setChecked(int position, boolean checked) {
		mCheckBoxes.get(position).setChecked(checked);
	}
	
	public boolean isChecked(int position) {
		return mCheckBoxes.get(position).isChecked();
	}
}
