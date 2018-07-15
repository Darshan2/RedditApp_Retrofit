package darshan.com.redditdemoapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import darshan.com.redditdemoapp.models.signin.SignInData;
import darshan.com.redditdemoapp.util.URLS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mProgressBar = findViewById(R.id.loginRequestLoadingProgressBar);
        mProgressBar.setVisibility(View.GONE);

        initWidgets();
    }


    private void initWidgets() {
        final EditText inputUserName, inputPassword;
        Button logInBtn;

        inputUserName = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        logInBtn = findViewById(R.id.btn_login);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String userName = inputUserName.getText().toString();
                String password = inputPassword.getText().toString();

                if(!(userName.equals("") || password.equals(""))) {
                    Log.d(TAG, "onClick: ");
                    signIn(userName, password);

                } else {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LogInActivity.this,
                            "Enter user name/Pass word", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void signIn(final String userName, String passWord) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLS.LOG_IN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RSSFeedAPI rssFeedAPI = retrofit.create(RSSFeedAPI.class);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Call<SignInData> call = rssFeedAPI.postLoginInfo(headerMap, userName, userName, passWord, "json");

        call.enqueue(new Callback<SignInData>() {
            @Override
            public void onResponse(Call<SignInData> call, Response<SignInData> response) {
                mProgressBar.setVisibility(View.GONE);
                try {
                    Log.d(TAG, "onResponse: serverResponse " + response);
    //                Log.d(TAG, "onResponse: object " + response.body());

                    String modhash = response.body().getJson().getData().getModhash();
                    String cookie = response.body().getJson().getData().getCookie();

                    if(!modhash.equals("")) {
                        setSessionParams(userName, modhash, cookie);
                        Toast.makeText(LogInActivity.this, "SignIn success", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d(TAG, "onResponse: Problem signing in");
                        Toast.makeText(LogInActivity.this, "Error:Signin error", Toast.LENGTH_SHORT).show();

                        finish();
                    }

                } catch (NullPointerException e) {
                    Log.d(TAG, "onResponse: " + e.getMessage());
                    Toast.makeText(LogInActivity.this, "Error:Signin error", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<SignInData> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: Unable to retrieve rss feed" + t.getMessage());
                Toast.makeText(LogInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
        save the following data. Which is must if you want to maintain sign in status of the user
        throughout the app so user can comment, like or etc.
     */
    private void setSessionParams(String userName, String modhash, String cookie) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.sessionUserName), userName);
        editor.commit();
        editor.putString(getString(R.string.sessionModHash), modhash);
        editor.commit();
        editor.putString(getString(R.string.sessionCookie), cookie);
        editor.commit();

        Log.d(TAG, "setSessionParams: cookie=" + cookie +
                "\n modHash='" + modhash +
                "\n userName='" + userName +
                "\n }");

        finish();
    }
}
