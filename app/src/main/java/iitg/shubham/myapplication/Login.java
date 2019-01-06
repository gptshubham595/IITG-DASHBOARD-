package iitg.shubham.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {
    ImageView b2;
    ProgressDialog load;
    private FirebaseAuth mauth;

    EditText e1,e2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth=FirebaseAuth.getInstance();


        e1=(EditText)findViewById(R.id.email);
        e2=(EditText)findViewById(R.id.pass);
        b1=(Button)findViewById(R.id.login);
        load=new ProgressDialog(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=e1.getText().toString();
                String pass=e2.getText().toString();
                LoginAccount(email,pass);
            }
        });
    }

    private void LoginAccount(String email, String pass) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please Write Your Email",Toast.LENGTH_LONG).show();}
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please Write Your Password",Toast.LENGTH_LONG).show();}
        else
        {
            load.setTitle("Loging To Your Account..");
            load.setMessage("Please Wait");
            load.show();
            mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Logged In!!",Toast.LENGTH_LONG).show();
                        Intent b=new Intent(getApplicationContext(),MainActivity.class);
                        b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(b);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"ERROR OCCURED TRY AGAIN!!",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"Either Password or Email is wrong..",Toast.LENGTH_LONG).show();
                    }
                    load.dismiss();
                }
            });
        }
    }
}
