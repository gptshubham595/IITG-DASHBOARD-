package iitg.shubham.myapplication;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private CircleImageView img;
    private StorageReference storageReference;
    private TextView dispname,dispstatus,dispemail,disroll;
    private Button chngimg,chngstatus;
    private DatabaseReference getuser;
    private FirebaseAuth mauth;
    private final static int Gallery=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mauth=FirebaseAuth.getInstance();
        String online_user_id=mauth.getCurrentUser().getUid();
        getuser= FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        storageReference= FirebaseStorage.getInstance().getReference().child("Profile_images");

        img=(CircleImageView)findViewById(R.id.pic);
        dispname=(TextView)findViewById(R.id.username);
        dispemail=(TextView)findViewById(R.id.email);
        disroll=(TextView)findViewById(R.id.roll);
        dispstatus=(TextView)findViewById(R.id.userstatus);
        chngimg=(Button)findViewById(R.id.changeimg);
        chngstatus=(Button)findViewById(R.id.changestatus);

        chngstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b=new Intent(getApplicationContext(),Status.class);
                startActivity(b);
            }
        });

        getuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("user_name").getValue().toString();
                String email=dataSnapshot.child("user_email").getValue().toString();
                String roll=dataSnapshot.child("user_roll").getValue().toString();
                String status=dataSnapshot.child("user_status").getValue().toString();
                String image=dataSnapshot.child("user_image").getValue().toString();
                String thumbimg=dataSnapshot.child("user_thumb_image").getValue().toString();


                dispname.setText(name);
                dispstatus.setText(status);
                disroll.setText(roll);
                dispemail.setText(email);

                Picasso.get().load(image).into(img);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        chngimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,Gallery);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery && resultCode==RESULT_OK && data!=null)
        {

            Uri imageuri =data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resulturi = result.getUri();
                String uid=mauth.getCurrentUser().getUid();
                StorageReference filepath = storageReference.child(uid + ".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Saving Your Profile",Toast.LENGTH_LONG).show();

                            String downloadurl=task.getResult().getDownloadUrl().toString();
                            getuser.child("user_image").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){Toast.makeText(getApplicationContext(),"Imgae updated!!",Toast.LENGTH_LONG).show();}
                                }
                            });

                        }
                        else{Toast.makeText(getApplicationContext(),"Error Occured while uploading",Toast.LENGTH_LONG).show();}
                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }

    }
}


