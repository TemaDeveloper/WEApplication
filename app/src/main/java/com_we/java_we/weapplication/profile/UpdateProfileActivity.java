package com_we.java_we.weapplication.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.managers.SharedPrefManager;
import com_we.java_we.weapplication.rest.ApiClient;
import com_we.java_we.weapplication.rest.ApiInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private ImageView backImageView;
    private MaterialButton updateMaterialButton;
    private EditText nameEditText, emailEditText;
    private ConstraintLayout imageChangerConstraintLayout;
    private ImageView profileImageView;
    //bitmap
    private Bitmap bitmap;
    //request code
    private static final int PICK_IMAGE_REQUEST = 777;
    //uri path for image
    private Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        init();
        getIntentData();

        backImageView.setOnClickListener(this);
        updateMaterialButton.setOnClickListener(this);
        imageChangerConstraintLayout.setOnClickListener(this);

    }

    private void init(){
        //initialization
        backImageView = findViewById(R.id.image_view_back);
        updateMaterialButton = findViewById(R.id.button_done_editing);
        nameEditText = findViewById(R.id.edit_text_name);
        emailEditText = findViewById(R.id.edit_text_email);
        imageChangerConstraintLayout = findViewById(R.id.constraint_layout_image_changer);
        profileImageView = findViewById(R.id.image_view_profile);
    }

    private void getIntentData(){
        //get intent data from ProfileActivity.class
        nameEditText.setText(getIntent().getStringExtra("name"));
        emailEditText.setText(getIntent().getStringExtra("email"));
        Picasso.get().load(Uri.parse(getIntent().getStringExtra("image"))).into(profileImageView);
    }

    public String convertImageToString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            //get path from media store
            path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.image_view_back:
                onBackPressed();
                break;
            case R.id.button_done_editing:
                //gather data from fields
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                int id = getIntent().getIntExtra("ID", 0);
                String imagePath = convertImageToString(bitmap);
                SharedPrefManager.getInstance(getApplicationContext()).setUserEmail(email);
                //update data
                ApiInterface apiInterface = ApiClient.getApiService();
                Call<ResponseBody> callUpdate = apiInterface.updateUser(id, name, email, imagePath);
                callUpdate.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("UPDATING_ERROR", t.getMessage());
                    }
                });
                break;
            case R.id.constraint_layout_image_changer:
                selectImage();
                break;
        }
    }
}