package com.example.videodemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.videodemo.utils.CircularAnim;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, EditText.OnEditorActionListener, AppBarLayout.OnOffsetChangedListener {

    private final int REQUEST_CODE_DOC = 231;
     private final int MY_PERMISSIONS_REQUEST_STORAGE = 22;
    private EditText etFname, etLname, etPwd, etConfirmPwd, etEmail, etMobile;
    private NestedScrollView scrollView;
    private FrameLayout frameLayout;
    private ProgressBar progressWheelForSignUpBtn;
    private Button btnForSignup;
    private boolean temp;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private View parentView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        context = this;
        temp = true;
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btnForSignup = findViewById(R.id.submitBtnAtSignUp);
        btnForSignup.setOnClickListener(this);
        parentView = findViewById(R.id.coordinatelayoutAtSignup);
//        tvForSelectedDocName = findViewById(R.id.tvForSelectedDocName);
        etFname = findViewById(R.id.et_fname_at_signup);
        etLname = findViewById(R.id.et_lname_at_signup);
        etMobile = findViewById(R.id.et_mobile_at_signup);
        etPwd = findViewById(R.id.et_pwd_at_signup);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd_at_signup);
        etEmail = findViewById(R.id.et_email_at_signup);
        scrollView = findViewById(R.id.nested_scroll_at_signup);
        frameLayout = findViewById(R.id.frame_layout_at_signup);
        progressWheelForSignUpBtn = findViewById(R.id.progressWheelForSignUpBtn);
        etPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPwd.setFocusable(true);
                etPwd.requestFocus();
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPwd.getRight() - etPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (temp) {
                            temp = false;
                            etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            etPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_grey, 0);
                            etPwd.setSelection(etPwd.getText().length());
                        } else {
                            etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_grey, 0);
                            etPwd.setSelection(etPwd.getText().length());
                            temp = true;
                        }
                    }

                }
                return false;
            }
        });

//        findViewById(R.id.tvForSignInAtSignUp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CircularAnim.fullActivity(SignUpActivity.this, v)
//                        .go(new CircularAnim.OnAnimationEndListener() {
//                            @Override
//                            public void onAnimationEnd() {
//                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//            }
//        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    private boolean isFormValidated() {
        if (etFname.getText().toString().length() < 2) {
            showSnackBar(getString(R.string.error_txt_for_no_fname), R.color.app_red_color, parentView);
            return false;
        } else if (etLname.getText().toString().length() < 2) {
            showSnackBar(getString(R.string.error_txt_for_no_lname), R.color.app_red_color, parentView);
            return false;
        } else if (etPwd.getText().toString().isEmpty()) {
            showSnackBar(getString(R.string.error_txt_for_no_signup_pwd), R.color.app_red_color, parentView);
            return false;
        } else if (etPwd.getText().toString().length() < 6) {
            showSnackBar(getString(R.string.error_txt_for_min_length_pwd), R.color.app_red_color, parentView);
            return false;
        } else if (etConfirmPwd.getText().toString().isEmpty()) {
            showSnackBar(getString(R.string.error_txt_for_no_confirm_pwd), R.color.app_red_color, parentView);
            return false;
        } else if (!etConfirmPwd.getText().toString().contentEquals(etPwd.getText().toString())) {
            showSnackBar(getString(R.string.error_txt_for_no_matched_pwd), R.color.app_red_color, parentView);
            return false;
        } else if (etEmail.getText().toString().trim().length() > 0 && !(android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())) {
            showSnackBar(getString(R.string.error_txt_for_no_email), R.color.app_red_color, parentView);
            return false;
        } else if (etMobile.getText().toString().length() < 10 && etMobile.getText().toString().length() > 1) {
            showSnackBar(getString(R.string.error_txt_for_no_mobilenuber), R.color.app_red_color, parentView);
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * sets up all click disables when displaying progress wheel
     */
    void setProgress() {
        etFname.setEnabled(false);
        etLname.setEnabled(false);
        etPwd.setEnabled(false);
        etConfirmPwd.setEnabled(false);
        etMobile.setEnabled(false);
        etEmail.setEnabled(false);
    }

    /**
     * sets up all click enables after hiding progress wheel
     */
    void resetProgress() {
        etFname.setEnabled(true);
        etLname.setEnabled(true);
        etPwd.setEnabled(true);
        etConfirmPwd.setEnabled(true);
        etMobile.setEnabled(true);
        etEmail.setEnabled(true);
        CircularAnim.hide(progressWheelForSignUpBtn).go();
        CircularAnim.show(btnForSignup).go();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitBtnAtSignUp) {
            if (!isFormValidated()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                imm.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
                return;//if not form validated then not executing the sign up api
            }

            setProgress();
            CircularAnim.hide(v)
                    .endRadius(progressWheelForSignUpBtn.getWidth() / 2)
                    .go(new CircularAnim.OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            progressWheelForSignUpBtn.setVisibility(View.VISIBLE);
                            CircularAnim.show(progressWheelForSignUpBtn);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CircularAnim.fullActivity(SignUpActivity.this, progressWheelForSignUpBtn)
                                            .go(new CircularAnim.OnAnimationEndListener() {
                                                @Override
                                                public void onAnimationEnd() {
                                                    Intent intent=new Intent(SignUpActivity.this, HomeActivity.class);
                                                    intent.putExtra("username", etEmail.getText().toString());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putString(AppConstants.username,etEmail.getText().toString());
                                                    editor.putBoolean(AppConstants.isLoggedIn,true);
                                                    finish();
                                                }
                                            });
                                }
                            },3000);

                        }
                    });
        }

    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        boolean isShow = true;
        int scrollRange = -1;
 /*       if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {//total collapsed
//            findViewById(R.id.imgAtSignUp).setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Registration");
            Toast.makeText(context,"collapsed",Toast.LENGTH_SHORT).show();
        } else if (verticalOffset == 0) {
            getSupportActionBar().setTitle("");
//            findViewById(R.id.imgAtSignUp).setVisibility(View.VISIBLE);
            // Fully expanded
            Toast.makeText(context,"not collapsed",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context,"not collapsed",Toast.LENGTH_SHORT).show();

            getSupportActionBar().setTitle("");
//            findViewById(R.id.imgAtSignUp).setVisibility(View.VISIBLE);
            // Not fully expanded or collapsed
        }*/
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            collapsingToolbarLayout.setTitle("Registration");
            isShow = true;
        } else if(isShow) {
            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
            isShow = false;
        }
    }
}
