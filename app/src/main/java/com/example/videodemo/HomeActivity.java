package com.example.videodemo;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videodemo.utils.CustomVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    public static final int TIMER_FREQUENCY = 3000;
    public static final int TIMER_DELAY = 1500;

    CustomVideoView videoView;
    FrameLayout videoViewContainer;
    MediaController mediaController;
    Uri videoUri;
    private VideoFrameListAdapter videoFrameListAdapter;
    private RecyclerView recyclerView;
    private Timer timer, timerforVideoTime;
    private SettingsReceiver settingsReceiver;
    private NavigationView navigationView;
    //    private int videoDuration;
    ArrayList<String> labelNamesArray = new ArrayList<>();
    private ArrayList<VideoMotionData> videoMotionDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mediaController.hide();
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setTitle(sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]));
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle(sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]));
        View navHeader = navigationView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.nav_header_username)).setText(getIntent().getStringExtra("username"));

        recyclerView = findViewById(R.id.recyclerViewForVideoFrames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        videoViewContainer = findViewById(R.id.videoViewContainer);
        videoView = findViewById(R.id.videoView);
        videoUri =
                Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoplay1);

        videoView.setVideoURI(videoUri);
        videoView.start();
//        mediaController.show(5000);
//        if(mediaController.isShowing()){
//            mediaController.hide();
//        }

//        mediaController.setPrevNextListeners(null, null);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVideoURI(videoUri);
                videoView.start();
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoViewContainer);
                mediaController.setMediaPlayer(videoView);
                mediaController.hide();
                videoMotionDataArrayList.clear();
                videoFrameListAdapter.notifyDataSetChanged();

            }
        });


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
//                boolean running = true;
//                videoDuration = videoView.getDuration();


                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController = new MediaController(HomeActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoViewContainer);
                        mediaController.setMediaPlayer(videoView);
                        mediaController.show();
                        videoView.setPlayPauseListener(playPauseListener);

//                        mediaController.setOnTouchListener(onMediaControllerTouchListener);
                    }
                });
            }
        });


        videoView.setOnTouchListener(this);
        videoViewContainer.setOnTouchListener(this);


//        prepareVideoFrameListData();


        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().commit();
                onBackPressed();

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                Toast.makeText(getApplicationContext(), "You are Signed out", Toast.LENGTH_SHORT).show();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(HomeActivity.this);
                notificationManager.cancelAll();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
//        try {
//            videointelligence.DetectVideo.analyzeShots(String.valueOf("android.resource://" + getPackageName() + "/raw/videoplay.mp4"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        settingsReceiver = new SettingsReceiver();
//        timer = new Timer();
//        timer.schedule(timerTask, 10000, 5000);

//        analyzeVideo();
        requestVideoOperations("us-east1.12810406038676860697");
    }


//    TimerTask timerTask = new TimerTask() {
//        @Override
//        public void run() {
//            sendFrameChanges();
//        }
//    };

    TimerTask timerTaskForVideoCurrentTime = new TimerTask() {
        @Override
        public void run() {
            Log.d("video current time", videoView.getCurrentPosition() / 1000 + "");
            sendFrameChanges(videoView.getCurrentPosition() / 1000);
        }
    };


//    int counter = 0;

    /**
     * sends notification about scene changes in a video frame and also updates the frame change list in UI
     */
    void sendFrameChanges(int videoRunningPosition) {
//        counter++;
//        if (counter > 6) {
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//            return;
//        }
        Log.d("sendframes ", "entered");
        final VideoMotionData videoMotionData = new VideoMotionData(R.drawable.pic_2, videoRunningPosition < 47 ? "Scene change normal" : "Suspected Harmful activity", videoRunningPosition, "6:10 pm");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentSize = videoMotionDataArrayList.size() + 1;
                videoMotionDataArrayList.add(videoMotionData);
                videoFrameListAdapter.notifyItemRangeInserted(currentSize, videoMotionDataArrayList.size());
//                videoFrameListAdapter.notifyItemInserted(videoMotionDataArrayList.size() - 1);

//                recyclerView.setItemAnimator(null);
                recyclerView.scrollToPosition(videoMotionDataArrayList.size() - 1);
            }
        });

        if (videoRunningPosition > 47) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationManager notificationManager;
            NotificationCompat.Builder mBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(getPackageName(), "video activity changes", importance);
                notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                mBuilder = new NotificationCompat.Builder(getApplicationContext(), getPackageName());
            } else {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(this);
            }


            mBuilder.setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(videoMotionData.getTitle())
                    .setContentText("In " + sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]) + " at " + videoMotionData.getTimeFrame() + " seconds")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


// notificationId is a unique int for each notification that you must define
            notificationManager.notify(new Random().nextInt(100), mBuilder.build());
        }

    }


    private static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (mediaController != null)
//            mediaController.show(0);

        //makes full screen
//        if (hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("SETTINGS_CHANGED");
        LocalBroadcastManager.getInstance(this).registerReceiver(settingsReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stopPosition > 0)
            videoView.seekTo(stopPosition);
        else
            videoView.setVideoURI(videoUri);
        videoView.start();
//        mediaCotroller.hide();
    }

    int stopPosition = 0;

    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(settingsReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bedroom) {
            getSupportActionBar().setTitle(sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]));
            // Handle the camera action
        } else if (id == R.id.nav_add_camera) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.layout_add_camera, null);
            alertDialogBuilder.setView(dialogView);
            alertDialogBuilder.setCancelable(false);
            RadioGroup radioGroup = dialogView.findViewById(R.id.radioGrpToAdCamera);
            final AlertDialog alertDialog1 = alertDialogBuilder.show();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    alertDialog1.dismiss();
                    String addedCameraName = ((RadioButton) dialogView.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    Toast.makeText(HomeActivity.this, "A Camera added at " + addedCameraName + " to monitor", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setTitle(addedCameraName);
                    navigationView.getMenu().add(R.id.camera_group, navigationView.getMenu().size() + 1, Menu.NONE, addedCameraName)
                            .setIcon(R.drawable.ic_menu_camera);
                }
            });


        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mediaController.isShowing())
            mediaController.hide();
        else mediaController.show(4000);
        return true;
    }

//    void prepareVideoFrameListData() {
//        videoMotionDataArrayList = new ArrayList<>();
//        videoMotionDataArrayList.add(new VideoMotionData(R.drawable.pic_1, "Motion Detected", "2 sec", "6:10 pm"));
//        videoMotionDataArrayList.add(new VideoMotionData(R.drawable.pic_2, "Motion Detected", "1 sec", "6:10 pm"));
//    }


    public class SettingsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSupportActionBar().setTitle(sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]));
            navigationView.getMenu().getItem(0).setTitle(sharedPreferences.getString(AppConstants.cameraName, getResources().getStringArray(R.array.pref_camera_name_titles)[0]));
        }
    }

    void analyzeVideo() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getString(R.string.google_annotation_url, getString(R.string.google_cloud_api_key)), getJsonReqBody("gs://staging.recipeapp-a7075.appspot.com/videoplay_movmnt_stop.mp4"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    requestVideoOperations(response.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 500)
                    Toast.makeText(HomeActivity.this, "Sorry Internet connection failed.", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(HomeActivity.this, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }


    void requestVideoOperations(String videoCode) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getString(R.string.google_video_operations_api, videoCode, getString(R.string.google_cloud_api_key)), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject annotationObj = jsonObject.getJSONObject("response").getJSONArray("annotationResults").getJSONObject(0);
                    JSONArray labelArray = annotationObj.getJSONArray("segmentLabelAnnotations");
                    labelNamesArray = new ArrayList<>();
                    for (int index = 0; index < labelArray.length(); index++) {
                        JSONObject labelObj = labelArray.getJSONObject(index);
                        if (labelObj.has("entity"))
                            labelNamesArray.add(labelObj.getJSONObject("entity").getString("description"));
                    }

                    videoMotionDataArrayList.clear();
                    videoFrameListAdapter = new VideoFrameListAdapter(videoMotionDataArrayList, labelNamesArray, HomeActivity.this);
                    recyclerView.setAdapter(videoFrameListAdapter);
//                    videoFrameListAdapter.setLabelList(labelNamesArray);
//                    videoFrameListAdapter.notifyDataSetChanged();

                    //initialize TimerTask for video frames list
                    timerforVideoTime = new Timer();
                    timerTaskForVideoCurrentTime = new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("video current time", videoView.getCurrentPosition() / 1000 + "");
                            sendFrameChanges(videoView.getCurrentPosition() / 1000);
                        }
                    };
                    timerforVideoTime.schedule(timerTaskForVideoCurrentTime, TIMER_DELAY, TIMER_FREQUENCY);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 500)
                    Toast.makeText(HomeActivity.this, "Sorry Internet connection failed.", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(HomeActivity.this, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private JSONObject getJsonReqBody(String videopath) {
        String requestString = "{\n" +
                "  \"features\": [\n" +
                "    \"LABEL_DETECTION\",\n" +
                "    \"SHOT_CHANGE_DETECTION\"\n" +
                "  ],\n" +
                "  \"inputUri\": \"" + videopath + "\",\n" +
                "  \"locationId\": \"us-east1\"" +
                "}";
        try {
            return new JSONObject(requestString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


//    private View.OnTouchListener onMediaControllerTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (videoView.isPlaying()) {
//                if (timerforVideoTime == null) {
//                    timerforVideoTime = new Timer();
//                    timerTaskForVideoCurrentTime = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Log.d("video current time", videoView.getCurrentPosition() / 1000 + "");
//                            sendFrameChanges(videoView.getCurrentPosition() / 1000);
//                        }
//                    };
//                    timerforVideoTime.schedule(timerTaskForVideoCurrentTime, 0, TIMER_FREQUENCY);
//                }
//            } else {
//                if (timerforVideoTime != null) {
//                    timerforVideoTime.cancel();
//                    timerTaskForVideoCurrentTime.cancel();
//                    timerforVideoTime = null;
//                }
//            }
//            return true;
//        }
//    };

    CustomVideoView.PlayPauseListener playPauseListener = new CustomVideoView.PlayPauseListener() {
        @Override
        public void onPlay() {
            if (timerforVideoTime == null) {
                timerforVideoTime = new Timer();
                timerTaskForVideoCurrentTime = new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("video current time", videoView.getCurrentPosition() / 1000 + "");
                        sendFrameChanges(videoView.getCurrentPosition() / 1000);
                    }
                };
                timerforVideoTime.schedule(timerTaskForVideoCurrentTime, TIMER_DELAY, TIMER_FREQUENCY);
            }
        }

        @Override
        public void onPause() {
            if (timerforVideoTime != null) {
                timerforVideoTime.cancel();
                timerTaskForVideoCurrentTime.cancel();
                timerforVideoTime = null;
            }
        }


    };
}
