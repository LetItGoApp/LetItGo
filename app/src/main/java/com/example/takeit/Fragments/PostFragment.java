package com.example.takeit.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takeit.GPSTracker;
import com.example.takeit.R;
import com.example.takeit.Models.Listing;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

public class PostFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "PostFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private EditText etTitle;
    private EditText etPrice;
    private Button btnCaptureImage;
    private Button btnPostListing;
    private ImageView ivListImage;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    GPSTracker gps;



    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etPrice = view.findViewById(R.id.etPrice);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        btnPostListing = view.findViewById(R.id.btnPostListing);
        ivListImage = view.findViewById(R.id.ivListImage);


        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        btnPostListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                String description;
                Double price;

                if(etTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Title cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    title = etTitle.getText().toString();
                }
                if(etDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    description = etDescription.getText().toString();
                }
                if(etPrice.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "You must enter a price.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    price = Double.valueOf(etPrice.getText().toString());
                }
                if (photoFile == null || ivListImage.getDrawable() == null){
                    Toast.makeText(getContext(), "An image must be attached.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                try {
                    savePost(description, currentUser, photoFile, price, title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @AfterPermissionGranted(69)
    private void checkPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Log.i(TAG, "App has permissions.");
            launchCamera();
        } else {
            EasyPermissions.requestPermissions(this, "Location required for displaying where you wish to sell this item.", 69, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.example.takeit.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            gps = new GPSTracker(getContext());

            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivListImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                    Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // ???
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile, Double price, String title) throws IOException {

        double MyLat = gps.getLatitude();
        double MyLong = gps.getLongitude();

        Listing listing = new Listing();
        listing.setDescription(description);
        listing.setImage(new ParseFile(photoFile));
        listing.setUser(currentUser);
        listing.setPrice(price);
        listing.setTitle(title);
        ParseGeoPoint userLocation = new ParseGeoPoint(MyLat, MyLong);
        listing.setLocation(userLocation);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();
        String cityState = cityName + ", " + stateName;

        listing.setCitystate(cityState);

        listing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Your post was uploaded!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Post save was successful!");
                etDescription.setText(null);
                ivListImage.setImageResource(0);
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


}