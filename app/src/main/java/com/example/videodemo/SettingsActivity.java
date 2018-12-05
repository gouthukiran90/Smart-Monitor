package com.example.videodemo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {


    private static SharedPreferences sharedPreferences;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
//            getFragmentManager().beginTransaction()
//                    .add(android.R.id.content, new GeneralPreferenceFragment()).commit();
//        }
//        setupActionBar();
        sharedPreferences=getSharedPreferences("MyPrefs", 0);
        context=getApplicationContext();


    }

    LinearLayout root;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        AppBarLayout bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent().getParent();
//            } else {
//                root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
//            }
            try {
                root = (LinearLayout) findViewById(android.R.id.list).getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, root, false);
            root.addView(bar, 0);
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);
            root.removeAllViews();
            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, root, false);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }
            content.setPadding(0, height, 0, 0);
            root.addView(content);
            root.addView(bar);
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) bar.getChildAt(0);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText("Camera Settings");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        setupSimplePreferencesScreen();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_settings_screen);
        Preference camera_name_list_pref = findPreference("camera_name_list");
        camera_name_list_pref.setTitle(getResources().getStringArray(R.array.pref_camera_name_titles)[0]);


        Preference notification_freq_list_pref = findPreference("notification_frequency_list");
        notification_freq_list_pref.setTitle(getResources().getStringArray(R.array.pref_notification_frequency_titles)[0]);

        Preference motion_sens_pref = findPreference("motion_sensitivity_list");
        motion_sens_pref.setDefaultValue(getResources().getStringArray(R.array.motion_sens_titles_descs)[0]);
        motion_sens_pref.setTitle(getResources().getStringArray(R.array.motion_sens_titles)[0]);

        Preference night_vision_pref = findPreference("night_vision_list_pref");
        night_vision_pref.setDefaultValue(getResources().getStringArray(R.array.night_vision_titles_descs)[0]);
        night_vision_pref.setTitle(getResources().getStringArray(R.array.night_vision_list_titles)[0]);

        bindPreferenceSummaryToValue(camera_name_list_pref);
        bindPreferenceSummaryToValue(notification_freq_list_pref);
        bindPreferenceSummaryToValue(motion_sens_pref);
        bindPreferenceSummaryToValue(night_vision_pref);


//        setPreferenceSummary(app_version, appVersion);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the Title to reflect the new value.
                preference.setTitle(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                if (preference.getKey().contentEquals("camera_name_list")) {
                    sharedPreferences.edit().putString(AppConstants.cameraName, preference.getTitle().toString()).commit();
                    Intent broadcastIntent=new Intent("SETTINGS_CHANGED");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                }
                if (preference.getKey().contentEquals("motion_sensitivity_list") ||
                        preference.getKey().contentEquals("night_vision_list_pref")) {
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntryValues()[index]
                                    : null);
                }


            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
//    private void setupActionBar() {
//        //setting up toolbar
//        getLayoutInflater().inflate(R.layout.toolbar_setting, (ViewGroup) findViewById(android.R.id.content));
//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("Camera Settings");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public boolean onIsMultiPane() {
//        return isXLargeTablet(this);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public void onBuildHeaders(List<Header> target) {
//        loadHeadersFromResource(R.xml.pref_headers, target);
//    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);

        if (preference != null) {
            if (preference instanceof PreferenceScreen) {
                if (((PreferenceScreen) preference).getDialog() != null) {
                    ((PreferenceScreen) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
                    setUpNestedScreen((PreferenceScreen) preference);
                }
            }
        }

        return false;
    }

    public void setUpNestedScreen(PreferenceScreen preferenceScreen) {
        final Dialog dialog = preferenceScreen.getDialog();

        AppBarLayout appBar;

        View listRoot = dialog.findViewById(android.R.id.list);
        ViewGroup mRootView = (ViewGroup) dialog.findViewById(android.R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LinearLayout root = null;
            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent().getParent().getParent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, root, false);
            root.addView(appBar, 0);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout root = (LinearLayout) dialog.findViewById(android.R.id.list).getParent();
            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, root, false);
            root.addView(appBar, 0);
        } else {
            ListView content = (ListView) mRootView.getChildAt(0);
            mRootView.removeAllViews();

            LinearLayout LL = new LinearLayout(this);
            LL.setOrientation(LinearLayout.VERTICAL);

            ViewGroup.LayoutParams LLParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LL.setLayoutParams(LLParams);

            appBar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_setting, mRootView, false);

            LL.addView(appBar);
            LL.addView(content);

            mRootView.addView(LL);
        }

        android.support.v7.widget.Toolbar Tbar = (android.support.v7.widget.Toolbar) appBar.getChildAt(0);

        Tbar.setTitle(preferenceScreen.getTitle());

        Tbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
//    protected boolean isValidFragment(String fragmentName) {
//        return PreferenceFragment.class.getName().equals(fragmentName)
//                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
//                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
//                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
//    }

    /*
     */
/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 *//*

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_screen);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    */
/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 *//*

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    */
/**
 * This fragment shows data and sync preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 *//*

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
*/
}
