package eu.lucazanini.ariadne.fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.lucazanini.ariadne.R;
import timber.log.Timber;

public class MapFragment extends Fragment{

    public static String TAG = "map fragment";
    protected @BindView(R.id.imageView) ImageView map;
    private Uri imageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, view);

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Timber.d("RECEIVED INTENT FOR IMAGE %s", type);
                handleSendImage(intent); // Handle single image being sent
            }
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.save:
                saveImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map, menu);
    }

/*    *//**
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android#17674787
     * @param bitmapImage
     * @return
     *//*
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }*/

/*    *//**
     * https://stackoverflow.com/questions/21960650/image-uri-to-file
     *//*
    private void getFile(){

        final int chunkSize = 1024;  // We'll read in one kB at a time
        byte[] imageData = new byte[chunkSize];

        InputStream in;
        OutputStream out;

        try {
            in = getActivity().getContentResolver().openInputStream(imageUri);
            out = new FileOutputStream(file);  // I'm assuming you already have the File object for where you're writing to

            int bytesRead;
            while ((bytesRead = in.read(imageData)) > 0) {
                out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
            }

        } catch (Exception ex) {
            Timber.d(ex.getLocalizedMessage());
//            Log.e("Something went wrong.", ex);
        } finally {
            in.close();
            out.close();
        }
    }*/

    /*  adb shell
        run-as yourappPackageName
        cd /data/data/youappPackageName
        ls -all
     */
    private void saveImage(){
        String filename = "myfile.png";
//        String string = "Hello world!";
        FileOutputStream outputStream;

        Bitmap bitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();

        try {
            outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveExternalImage(){
        String filename = "myfile.png";
//        String string = "Hello world!";
        FileOutputStream outputStream;

        Bitmap bitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();

        try {
            outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS), albumName);
        if (!file.mkdirs()) {
            Timber.d("Directory not created");
//            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void handleSendImage(Intent intent) {
//        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Timber.d("PATH IS %s", imageUri.getPath());

            Glide.with(this)
                    .load(imageUri)
                    .into(map);

//            try {
//                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                map.setImageBitmap(captureBmp);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            map.setImageURI(imageUri);
        }
    }
}
