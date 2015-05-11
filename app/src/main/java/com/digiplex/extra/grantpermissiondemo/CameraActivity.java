package com.digiplex.extra.grantpermissiondemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class CameraActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComponentName me = new ComponentName(this, CameraActivity.class);
        if (getComponentName().equals(me)) {
            //launch camera
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            List<ResolveInfo> resolveinfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveinfo : resolveinfos) {
                ActivityInfo activity = resolveinfo.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                if ((activity.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    intent.setComponent(name);
                    break;
                }
            }

            startActivity(intent);
        } else {
            //launch gallery
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            List<ResolveInfo> resolveinfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveinfo : resolveinfos) {
                ActivityInfo activity = resolveinfo.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                if ((activity.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    intent.setComponent(name);
                    break;
                }
            }
            readRandomContact();
            startActivity(intent);
        }
        finish();
    }


    void readRandomContact() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        Random rand = new Random();
        int randomNum = rand.nextInt(phones.getCount());
        if (phones.moveToPosition(randomNum)) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Toast.makeText(this, "Contact " + name + ":" + phoneNumber + " extracted", Toast.LENGTH_LONG).show();
        }

        phones.close();
    }
}
