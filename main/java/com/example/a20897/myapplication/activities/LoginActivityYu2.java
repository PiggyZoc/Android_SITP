package com.example.a20897.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.UserModel;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivityYu2 extends MyActivity implements LoaderCallbacks<Cursor> {

    private MyActivity ma;
    private UserModel um;

    private int action_mod = 1;
    
    private static final int ACTION_MOD_LOGIN = 1;
    private static final int ACTION_MOD_REGISTER = 2;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private QueryManager mAuthTask = null;
    

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "123", "123"
    };

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private EditText mPasswordAgainView;
    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mEmailView;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout mRegisterFormView;

    private Button mActionExecuteBtn;
    private Button mActionChangeBtn;

    private int RegisterFormHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityLoginYu";

        ma = this;

        setContentView(R.layout.activity_login_yu2);
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.login_user_name);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    actionExecute();
                    return true;
                }
                return false;
            }
        });

        mActionExecuteBtn = (Button) findViewById(R.id.login_action_execute);
        mActionExecuteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionExecute();
            }
        });


        mActionChangeBtn = (Button) findViewById(R.id.login_action_change_state);
        mActionChangeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionStateChange();
            }
        });

        mPasswordAgainView = (EditText) findViewById(R.id.login_register_passwordAgain);
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.login_register_phone);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_register_email);

        mLoginFormView = findViewById(R.id.login_form);
        mRegisterFormView = (LinearLayout) findViewById(R.id.login_register_form);
        mProgressView = findViewById(R.id.login_progress);

        ViewGroup.LayoutParams params = mRegisterFormView.getLayoutParams();
        RegisterFormHeight = params.height;
        params.height = 0;
        mRegisterFormView.setLayoutParams(params);
    }

    private void actionExecute(){
        switch (action_mod){
            case ACTION_MOD_LOGIN:
                attemptLogin();
                return;
            case ACTION_MOD_REGISTER:
                attemptRegister();
                return;
        }
    }

    private void actionStateChange(){
        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        mEmailView.setError(null);

        switch (action_mod){
            case ACTION_MOD_LOGIN:
                action_mod = ACTION_MOD_REGISTER;
                mActionExecuteBtn.setText("注册");
                mActionChangeBtn.setText("已有账号");
                showRegisterForm();
                return;
            case ACTION_MOD_REGISTER:
                action_mod = ACTION_MOD_LOGIN;
                mActionExecuteBtn.setText("登录");
                mActionChangeBtn.setText("没有账号？");
                hideRegisterForm();
                return;
        }
    }

    private void hideRegisterForm(){
        mRegisterFormView.clearAnimation();
        final int startValue = mRegisterFormView.getHeight();
        final int deltaValue = 0 - startValue;
        int durationMillis = 200;
        Animation animation = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ViewGroup.LayoutParams params = mRegisterFormView.getLayoutParams();
                params.height = (int) (startValue + deltaValue * interpolatedTime);
                mRegisterFormView.setLayoutParams(params);
            }
        };
        animation.setDuration(durationMillis);
        mRegisterFormView.startAnimation(animation);
    }

    private void showRegisterForm(){
        mRegisterFormView.clearAnimation();
        int lines = 2;
        final int startValue = mRegisterFormView.getHeight();
        final int deltaValue = RegisterFormHeight - startValue;
        int durationMillis = 200;
        Animation animation = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ViewGroup.LayoutParams params = mRegisterFormView.getLayoutParams();
                params.height = (int) (startValue + deltaValue * interpolatedTime);
                mRegisterFormView.setLayoutParams(params);
            }
        };
        animation.setDuration(durationMillis);
        mRegisterFormView.startAnimation(animation);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isEmailValid(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            QueryManager mAuthTask=new QueryManager(ma);

            mAuthTask.execute("UserLogin","User_id",userName,"password",password);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordAgain = mPasswordAgainView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (!password.equals(passwordAgain)) {
            mPasswordAgainView.setError("两次密码不同");
            focusView = mPasswordAgainView;
            cancel = true;
        }

        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mUserNameView.setError(getString(R.string.error_field_required));
//            focusView = mUserNameView;
//            cancel = true;
//        }
//        else
        if (!isEmailValid(email)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            QueryManager mAuthTask=new QueryManager(ma);

            mAuthTask.execute("insertUserInfo",
                    "user_id", userName,
                    "password", password,
                    "uname", userName,
                    "phone", phone,
                    "email", email
            );
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivityYu2.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    @Override
    public void goingOn(ArrayList<String> arrayList) {
        showProgress(false);
        mAuthTask = null;
        um=new UserModel();
        String methodName = arrayList.get(0);
        Intent intent = new Intent();
        boolean have_error = false;
        try {
            switch (methodName) {
                case "UserLogin":
                    have_error = true;
                    if (arrayList.size() > 1) {
                        String rs = arrayList.get(1);
                        if (!rs.isEmpty()) {
                            um= ResultParser.parseUser(rs);
                            have_error = false;
                        }
                    }
                     if (have_error) {
                        mUserNameView.setError(getString(R.string.error_incorrect_password));
                        mUserNameView.requestFocus();
                        um = null;
                        return;
                    }
                    UserAccount.getInstance().setUser(um);
                    //  Fan Hui Main
                    intent.putExtra("ShowToast", true);
                    setResult(201, intent);
                    finish();
                    //
                    return;
                case "insertUserInfo":
                    String str = arrayList.get(1);
                    if (str.equals("true")) {
                        um.user_id = mUserNameView.getText().toString();
                        um.password = mPasswordView.getText().toString();
                        //TODO what is user name
                        um.user_name = mUserNameView.getText().toString();
//                        um.gender = txtGender.getText().toString();
                    } else {
                        mUserNameView.setError("用户名已存在");
                        mUserNameView.requestFocus();
                        um = null;
                        return;
                    }
                    UserAccount.getInstance().setUser(um);
                    //  Fan Hui Main
                    intent.putExtra("ShowToast", true);
                    setResult(301, intent);
                    finish();
                    //
                    return;
                case "Error":

                    Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                @TargetApi(Build.VERSION_CODES.M)
                                public void onClick(View v) {
                                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                                }
                            });
//                    UserAccount.getInstance().setUser(um);
//                    intent.setClassName(getApplicationContext(),"com.example.a20897.myapplication.MainActivity");
//                    startActivity(intent);
//                    finish();
                    //
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

