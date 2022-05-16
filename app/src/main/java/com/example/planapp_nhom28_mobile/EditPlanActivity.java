package com.example.planapp_nhom28_mobile;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPlanActivity extends AppCompatActivity {

    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;
    ImageView imgPlan;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    ProgressBar mprogressbarofcreatenote;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    private StorageReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);
        imgPlan=findViewById(R.id.imgPlanEdit);
        imgPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        ImageButton btnreturn = findViewById(R.id.btnReturnEdit);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditPlanActivity.this, PlanActivity.class);
                startActivity(intent);
            }
        });



        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

                String newtitle=medittitleofnote.getText().toString();
                String newcontent=meditcontentofnote.getText().toString();

                if(newtitle.isEmpty()||newcontent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Something is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    SimpleDateFormat dt = new SimpleDateFormat("EEE dd/MM/yyyy hh:mm:ss");
                    SimpleDateFormat dt1 = new SimpleDateFormat("EEE_dd-MM-yyyy_hh:mm:ss");
                    String tg = dt1.format(new java.util.Date());
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("plan").document(data.getStringExtra("noteId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("time", dt.format(new java.util.Date()));
                    DocumentReference documentReferenceImg = documentReference.collection("image").document();
                    //note.put("url", documentReferenceImg.set(filePath.getPath()));
                    reference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()).child(data.getStringExtra("noteId"));
                    final StorageReference fileRef = reference.child("Plan." + getFileExtension(filePath));
                    fileRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Plan model = new Plan(uri.toString());
                                    //s = uri.toString();
                                    //String modelId = root.push().getKey();
                                    // DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("plan").document();
                                    //DocumentReference documentReferenceImg = documentReference.collection("image").document();
                                    //reference.setValue(model);
                                    //documentReferenceImg.set(model);
                                    mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                                    Toast.makeText(EditPlanActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    //imgPlan.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            mprogressbarofcreatenote.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                            Toast.makeText(EditPlanActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    note.put("UrlImg", filePath);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditPlanActivity.this, PlanActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        //StorageReference pathReference = reference.child(firebaseUser.getUid()).child(String.valueOf(data));
        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        //String img=data.getStringExtra("UrlImg");
        meditcontentofnote.setText(notecontent);
        medittitleofnote.setText(notetitle);
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()).child(String.valueOf(data.getStringExtra("noteId"))+"/Plan.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getApplicationContext(),"Error: not load image",Toast.LENGTH_SHORT).show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid()).child(String.valueOf(data.getStringExtra("noteId"))+"/Plan.png");
            }
        });
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
                .load(storageReference)
                .into(imgPlan);

    }

    public void addSushiToLocalUsingRoom(List<Plan> arrPlan) {
        for (Plan p: arrPlan) {
            PlanDB.getInstance(this).planDao().insertPlan(p);
        }
    }

    public List<Plan> getAllSushiFromLocal () {
        return PlanDB.getInstance(this).planDao().getAll();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgPlan.setImageBitmap(bitmap);

                /*Bitmap b = BitmapFactory.decodeFile(filePath.getPath());
                final Drawable d = new BitmapDrawable(getResources(), bitmap);
                d.setBounds(0, 0, 1000, 800);
                final ImageSpan is = new ImageSpan(d);

                SpannableStringBuilder ss = new SpannableStringBuilder(".\n");
                ss.setSpan(is, 0, (".\n").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new MyClickAbleSpan(filePath.getPath()), 0, (".\n").length(), 0);
                mcreatecontentofnote.append(ss, 0, (".\n").length());
                mcreatecontentofnote.setMovementMethod(LinkMovementMethod.getInstance());*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private String uploadToFirebase(Uri uri, DocumentReference documentReference){
        String s="";
        reference = FirebaseStorage.getInstance().getReference(firebaseUser.getUid());
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Plan model = new Plan(uri.toString());
                        //s = uri.toString();
                        //String modelId = root.push().getKey();
                        // DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("plan").document();
                        //DocumentReference documentReferenceImg = documentReference.collection("image").document();
                        //reference.setValue(model);
                        //documentReferenceImg.set(model);
                        mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditPlanActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        //imgPlan.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                mprogressbarofcreatenote.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                Toast.makeText(EditPlanActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
        return s= uri.toString();
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}