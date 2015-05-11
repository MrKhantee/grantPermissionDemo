package com.digiplex.extra.grantpermissiondemo;

import android.app.ActivityManager;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by digiplex on 7/5/15.
 */
public class ProviderActivity extends ListActivity {


    private final static Comparator<MyPackageInfo> sDisplayNameComparator
            = new Comparator<MyPackageInfo>() {
        private final Collator collator = Collator.getInstance();

        public final int compare(MyPackageInfo a, MyPackageInfo b) {
            return collator.compare(a.label, b.label);
        }
    };
    private List<MyPackageInfo> mPackageInfoList = new ArrayList<MyPackageInfo>();
    private PackageListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new PackageListAdapter(this);
        setListAdapter(mAdapter);
    }

    static class MyPackageInfo {
        ApplicationInfo info;
        String label;
        String authority;
    }

    public class PackageListAdapter extends ArrayAdapter<MyPackageInfo> {

        public PackageListAdapter(Context context) {
            super(context, R.layout.package_list_item);

            for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        boolean allowed = checkPermissionForSystem(provider);


                        if (provider.grantUriPermissions && allowed) {
                            MyPackageInfo info = new MyPackageInfo();
                            info.info = provider.applicationInfo;
                            info.label = info.info.loadLabel(getPackageManager()).toString();
                            info.authority = provider.authority;
                            mPackageInfoList.add(info);
                        }
                    }
                }
            }

            if (mPackageInfoList != null) {
                Collections.sort(mPackageInfoList, sDisplayNameComparator);
            }
            setSource(mPackageInfoList);
        }


        public boolean checkPermissionForSystem(ProviderInfo provider) {

            boolean allowed = true;

            if (provider.readPermission != null) {
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                if (getPackageManager().checkPermission(provider.readPermission, "android") != PackageManager.PERMISSION_GRANTED) {
                    allowed = false;
                }
            } else if (provider.pathPermissions != null) {

                for (PathPermission pathPermission : provider.pathPermissions) {
                    if (getPackageManager().checkPermission(pathPermission.getReadPermission(), "android") != PackageManager.PERMISSION_GRANTED) {
                        allowed = false;
                    }
                }

            }else if (provider.applicationInfo.uid != 1000) {
                allowed = false;
            }
            return allowed;
        }

        @Override
        public void bindView(View view, MyPackageInfo info) {
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView description = (TextView) view.findViewById(R.id.description);
            icon.setImageDrawable(info.info.loadIcon(getPackageManager()));
            name.setText(info.label);
            description.setText(info.authority);
        }
    }

}
