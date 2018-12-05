package com.example.videodemo;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_schedule, container, false);
    }

    TextView tvForEditSchedule, tvForCameraOnTime, tvForCamerOffTime;
    EditText etForScheduleName;
    ArrayList<String> arrayListForSelectedDays;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etForScheduleName = view.findViewById(R.id.tvForScheduleName);
        tvForEditSchedule = view.findViewById(R.id.tvForEditScheduleName);
        tvForCameraOnTime = view.findViewById(R.id.tvForCameraOnTime);
        tvForCamerOffTime = view.findViewById(R.id.tvForCameraOFFTime);
        tvForCamerOffTime.setOnClickListener(this);
        tvForCameraOnTime.setOnClickListener(this);
        tvForEditSchedule.setOnClickListener(this);
        calendar = Calendar.getInstance();
        view.findViewById(R.id.btnforAddSchedule).setOnClickListener(this);
        LinearLayout layoutForDays=view.findViewById(R.id.layout_days);
        arrayListForSelectedDays=new ArrayList<>();
        for (int index=0;index<getResources().getStringArray(R.array.days).length;index++){
            arrayListForSelectedDays.add("");//filled the list with dummy data
            final TextView tvForDay=new TextView(getContext());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,0,20,0);
            tvForDay.setLayoutParams(params);
            tvForDay.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_normal));
            tvForDay.setText(getResources().getStringArray(R.array.days)[index]);
            tvForDay.setTag("1");
            tvForDay.setId(index);
            tvForDay.setGravity(Gravity.CENTER);
            tvForDay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            layoutForDays.addView(tvForDay);
            tvForDay.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            tvForDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvForDay.getTag().toString().contentEquals("1")){// make it select
                        tvForDay.setTag("2");
                        arrayListForSelectedDays.add(tvForDay.getId(),getResources().getStringArray(R.array.days)[tvForDay.getId()]);
                        tvForDay.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                        tvForDay.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_selected));
                    }else{ //make it unselect
                        tvForDay.setTag("1");
                        arrayListForSelectedDays.add(tvForDay.getId(),"");
                        tvForDay.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                        tvForDay.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_normal));
                    }
                }
            });

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    Calendar calendar;

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.tvForEditScheduleName:
                etForScheduleName.setEnabled(true);
                etForScheduleName.requestFocus();
                etForScheduleName.setCursorVisible(true);
                etForScheduleName.setSelection(etForScheduleName.getText().length());
                etForScheduleName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                            etForScheduleName.setEnabled(false);
                            etForScheduleName.setCursorVisible(false);
                            return true;
                        }
                        return false;
                    }
                });

                break;
            case R.id.tvForCameraOnTime:
            case R.id.tvForCameraOFFTime:

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String am_pm = "";
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        int strHrsToShow = (calendar.get(Calendar.HOUR) == 0) ? 12 : calendar.get(Calendar.HOUR);
                        ((TextView) view).setText((strHrsToShow < 10 ? "0" + strHrsToShow : strHrsToShow) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)) + " " + am_pm);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
                break;
            case R.id.btnforAddSchedule:
                if (tvForCameraOnTime.getText().toString().contentEquals(tvForCamerOffTime.getText().toString())) {
                    Toast.makeText(getActivity(), "Please select valid time range", Toast.LENGTH_SHORT).show();
                    break;
                }
                mListener.onScheduleAdded(new Schedule(etForScheduleName.getText().toString(), tvForCameraOnTime.getText().toString(), tvForCamerOffTime.getText().toString(),arrayListForSelectedDays));
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onScheduleAdded(Schedule schedule);
    }
}
