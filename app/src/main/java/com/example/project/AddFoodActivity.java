package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.AddImageFoodAdapter;
import com.example.project.Object.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AddFoodActivity extends AppCompatActivity {

    private Button btnConfirm, btnOpenGallery;
    private EditText edtFoodName, edtFoodPrice, edtFoodDiscount, edtFoodDetails;
    DatabaseReference ref;
    StorageReference storageRef;
    private ArrayList<Uri> uri;
    private RecyclerView rcvImageFood;
    AddImageFoodAdapter adapter;
    GridLayoutManager gridLayoutManager;
    List<String> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        initUI();
        loadData();
        eventListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        Intent i = getIntent();
        if (i == null) return;
        String foodName = i.getStringExtra("foodName"),
                details = i.getStringExtra("details");
        int price = i.getIntExtra("price", 0),
                discount = i.getIntExtra("discount", 0);

        edtFoodName.setText(foodName);
        edtFoodPrice.setText(String.valueOf(price));
        edtFoodDiscount.setText(String.valueOf(discount));
        edtFoodDetails.setText(details);

        ArrayList<String> imgUrls = i.getStringArrayListExtra("imageFood");
        if(imgUrls==null) return;
        for(String data: imgUrls) uri.add(Uri.parse(data));
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    uri.add((data.getClipData().getItemAt(i).getUri()));
                }
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void eventListener() {
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null) uri.clear();
                String[] permission = {Manifest.permission.READ_MEDIA_IMAGES};
                TedPermission.create()
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent i = new Intent();
                                i.setType("image/*");
                                i.putExtra(i.EXTRA_ALLOW_MULTIPLE, true);
                                i.setAction(i.ACTION_GET_CONTENT);
                                startActivityForResult(i.createChooser(i, "Select Picture"), 1);
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(AddFoodActivity.this, "Need permission to access Gallery", Toast.LENGTH_SHORT).show();
                            }
                        }).setPermissions(permission).check();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUrls.clear();
                //get text
                String foodName = edtFoodName.getText().toString();
                String foodDetails = edtFoodDetails.getText().toString();
                if (edtFoodPrice.getText().toString().isEmpty()) edtFoodPrice.setText("0");
                if (edtFoodDiscount.getText().toString().isEmpty()) edtFoodDiscount.setText("0");
                int foodPrice = Integer.parseInt(edtFoodPrice.getText().toString());
                int foodDiscount = Integer.parseInt(edtFoodDiscount.getText().toString());

                if (foodName.isEmpty()) {
                    edtFoodName.setError("required");
                } else if (rcvImageFood.getLayoutManager().getChildCount() == 0) {
                    Toast.makeText(AddFoodActivity.this, "Need at least one image", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị ProgressDialog
                    ProgressDialog progressDialog = new ProgressDialog(AddFoodActivity.this);
                    progressDialog.setMessage("Food is adding...");
                    progressDialog.show();

                    storageRef = FirebaseStorage.getInstance().getReference();
                    ref = FirebaseDatabase.getInstance().getReference("foods").child(foodName);
                    Food food = new Food(foodName, foodDetails, foodPrice, foodDiscount);

                    AtomicInteger uploadCount = new AtomicInteger(uri.size());// ==cnt

                    for (Uri data : uri) {
                        StorageReference ew = storageRef.child(foodName + "/" + System.currentTimeMillis());
                        ew.putFile(data) //put img
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ew.getDownloadUrl() //get url
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uuri) {
                                                        imgUrls.add(uuri.toString());

                                                        if (uploadCount.decrementAndGet() == 0) { //check cnt
                                                            food.setImageFood(imgUrls);
                                                            ref.setValue(food)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            progressDialog.dismiss();
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                                                                                finish();
                                                                            } else {
                                                                                Toast.makeText(AddFoodActivity.this, "Failed",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddFoodActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });

    }

    private void initUI() {
        btnConfirm = findViewById(R.id.btn_confirm_management);
        edtFoodName = findViewById(R.id.edt_food_name_management);
        edtFoodDetails = findViewById(R.id.edt_details_management);
        edtFoodDiscount = findViewById(R.id.edt_discount_management);
        edtFoodPrice = findViewById(R.id.edt_price_management);
        btnOpenGallery = findViewById(R.id.btn_open_gallery);
        storageRef = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference("foods");
        imgUrls = new ArrayList<>();

        uri = new ArrayList<>();
        rcvImageFood = findViewById(R.id.rcv_image_foods);
        adapter = new AddImageFoodAdapter(uri);
        gridLayoutManager = new GridLayoutManager(AddFoodActivity.this, 3);
        rcvImageFood.setLayoutManager(gridLayoutManager);
        rcvImageFood.setAdapter(adapter);

    }

}