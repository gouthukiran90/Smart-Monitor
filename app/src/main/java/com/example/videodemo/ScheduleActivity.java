package com.example.videodemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity implements ScheduleFragment.OnFragmentInteractionListener {
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v7.widget.Toolbar toolbar;
    ArrayList<Schedule> arrayListOfScheduleList;
    ScheduleListAdapter scheduleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.schedule_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (sharedPreferences.contains(AppConstants.scheduleListPref))
            arrayListOfScheduleList = gson.fromJson(sharedPreferences.getString(AppConstants.scheduleListPref, ""),
                    new TypeToken<List<Schedule>>() {
                    }.getType());
        else
            arrayListOfScheduleList = new ArrayList<>();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        RecyclerView recyclerView = findViewById(R.id.recyclerView_for_scheduleList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scheduleListAdapter = new ScheduleListAdapter(arrayListOfScheduleList);
        recyclerView.setAdapter(scheduleListAdapter);


        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                findViewById(R.id.frame_layout_at_schedule).setVisibility(View.VISIBLE);
                findViewById(R.id.recyclerView_for_scheduleList).setVisibility(View.GONE);
                findViewById(R.id.layout_empty_info).setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frame_layout_at_schedule, ScheduleFragment.newInstance(), null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.add_schedule_title);
            }
        });
        if (!arrayListOfScheduleList.isEmpty()) {
            findViewById(R.id.recyclerView_for_scheduleList).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_empty_info).setVisibility(View.GONE);
        } else {
            findViewById(R.id.recyclerView_for_scheduleList).setVisibility(View.GONE);
            findViewById(R.id.layout_empty_info).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onScheduleAdded(Schedule schedule) {
        arrayListOfScheduleList.add(schedule);
        sharedPreferences.edit().putString(AppConstants.scheduleListPref, gson.toJson(arrayListOfScheduleList)).commit();
        scheduleListAdapter.notifyDataSetChanged();
        onBackPressed();
    }

    Gson gson = new Gson();

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            findViewById(R.id.frame_layout_at_schedule).setVisibility(View.GONE);
            if (arrayListOfScheduleList.isEmpty()) {
                findViewById(R.id.recyclerView_for_scheduleList).setVisibility(View.GONE);
                findViewById(R.id.layout_empty_info).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.recyclerView_for_scheduleList).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_empty_info).setVisibility(View.GONE);
            }
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.schedule_title);
        }
    }

    public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ItemViewHolder> {


        private ArrayList<Schedule> arrayList;

        public ScheduleListAdapter(ArrayList<Schedule> arrayList) {
            this.arrayList = arrayList;
        }

        public void setArrayList(ArrayList<Schedule> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.layout_schedule_item, null));
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.tvForName.setText(arrayList.get(position).getName());
            holder.tvForCamerTimePeriod.setText(Html.fromHtml("From <b>" + arrayList.get(position).getCameraOnTime() + "</b> to <b>" + arrayList.get(position).getCameraOffTime() + "</b>"));

            holder.layoutForDays.removeAllViews();
            for (int index = 0; index < arrayList.get(position).getArrayListForSelectedDays().size(); index++) {
                if (arrayList.get(position).arrayListForSelectedDays.get(index).isEmpty())
                    continue;
                final TextView tvForDay = new TextView(ScheduleActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 0, 15, 0);
                tvForDay.setLayoutParams(params);
                tvForDay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_selected));
                tvForDay.setText(arrayList.get(position).getArrayListForSelectedDays().get(index));
                tvForDay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvForDay.setGravity(Gravity.CENTER);
                tvForDay.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                tvForDay.setTextSize(10);
                holder.layoutForDays.addView(tvForDay);
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView tvForName, tvForCamerTimePeriod;
            Switch switchForSchedule;
            ImageView imageView;
            LinearLayout layoutForDays;

            public ItemViewHolder(View itemView) {
                super(itemView);
                layoutForDays = itemView.findViewById(R.id.layoutForDays);
                tvForName = itemView.findViewById(R.id.tvForScheduleName);
                tvForCamerTimePeriod = itemView.findViewById(R.id.tvForScheduleTimePeriod);
                switchForSchedule = itemView.findViewById(R.id.scheduleSwitch);
                switchForSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        Toast.makeText(ScheduleActivity.this, isChecked ? "Schedule enabled" : "Schedule disabled", Toast.LENGTH_SHORT).show();
                    }
                });
                switchForSchedule.setChecked(true);
                imageView = itemView.findViewById(R.id.imgForPopupMenu);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(ScheduleActivity.this, view);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.schedule_item_popup_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.action_delete) {
                                    arrayList.remove(getLayoutPosition());
                                    notifyItemRemoved(getLayoutPosition());
                                    arrayListOfScheduleList = arrayList;
                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu
                    }

                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        sharedPreferences.edit().putString(AppConstants.scheduleListPref, gson.toJson(arrayListOfScheduleList)).commit();
        super.onDestroy();
    }
}
