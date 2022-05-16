package com.example.planapp_nhom28_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private EditText msignupemail,msignuppassword, msignupname, msignuprepass;
    private RelativeLayout msignup;
    private TextView mgotologin;


    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 1;

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(register.this);
        //updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth= FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleApiClient=new GoogleApiClient.Builder(register.this)
                .enableAutoManage(register.this, register.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(register.this, gso);

        ImageView img = findViewById(R.id.imgRegisterGoogle);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                signIn();
                            }
                        });
            }
        });

        msignupname=findViewById(R.id.signupUsername);
        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignuprepass=findViewById(R.id.signuprepassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);


        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = msignupname.getText().toString().trim();
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();
                String repass=msignuprepass.getText().toString().trim();

                if(username.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Tên đăng nhập không được để trống.",Toast.LENGTH_SHORT).show();
                    msignupname.selectAll();
                    msignupname.requestFocus();
                }
                else if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Email không được để trống.",Toast.LENGTH_SHORT).show();
                    msignupemail.selectAll();
                    msignupemail.requestFocus();
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Mật khẩu không được để trống.",Toast.LENGTH_SHORT).show();
                    msignuppassword.selectAll();
                    msignuppassword.requestFocus();
                }
                else if(password.length()<6)
                {
                    Toast.makeText(getApplicationContext(),"Mật khẩu phải có ít nhất 6 ký tự.",Toast.LENGTH_SHORT).show();
                    msignuppassword.selectAll();
                    msignuppassword.requestFocus();
                }
                else if(!repass.equalsIgnoreCase(password)){
                    Toast.makeText(getApplicationContext(),"Mật khẩu nhập lại phải giống với mật khẩu đã nhập trước đó.",Toast.LENGTH_SHORT).show();
                    msignuprepass.selectAll();
                    msignuprepass.requestFocus();
                }
                else
                {
                    // Đăng ký tài khoản với firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            new register();
            account = GoogleSignIn.getLastSignedInAccount(register.this);
            msignupname.setText(account.getDisplayName());
            msignupemail.setText(account.getEmail());
            msignuppassword.requestFocus();
            Toast.makeText(register.this,"Thông tin từ tài khoản google lấy thành công."+account.getEmail(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(register.this,"Thông tin từ tài khoản google lấy không thành công.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //gửi link xác minh tài khoản đăng ký
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Xác minh email đã được gửi, vui lòng nhấn vào đường link trong email và dăng nhập lại.",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(register.this, MainActivity.class));
                }
            });
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Không gửi được xác minh email của bạn",Toast.LENGTH_SHORT).show();
        }
    }


}