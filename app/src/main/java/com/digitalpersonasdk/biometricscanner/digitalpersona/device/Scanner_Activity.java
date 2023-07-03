package com.digitalpersonasdk.biometricscanner.digitalpersona.device;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ahmaddp.digitalpersona.persona4500library.Fingerprint;
import ahmaddp.digitalpersona.persona4500library.Status;

public class Scanner_Activity extends AppCompatActivity {
    private Fingerprint fingerprint;
    TextView tvError, tvStatus;
    int key = 0;
    Bitmap bm;
    byte[] image;
    ImageView img_scan;
    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    Adapter_File adapterPdfFile;
    String fname = "";
    String name;
    String fullpath;
    TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);

        name = getIntent().getStringExtra("name");

        tvError = findViewById(R.id.tvError);
        error = findViewById(R.id.error);
        tvStatus = findViewById(R.id.tvStatus);
        fingerprint = new Fingerprint();

        String folder = getExternalMediaDirs()[0].toString();
        fullpath = folder + "/" + getString(R.string.app_name);
        File file = new File(fullpath);
        if (!file.exists()) {
            file.mkdirs();
        }

        recyclerView = findViewById(R.id.recyclerView);
        setAdapter();


        img_scan = findViewById(R.id.img_scan);
        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    error.setVisibility(View.GONE);
                    fingerprint.scan(Scanner_Activity.this, printHandler, updateHandler);
                    key = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    error.setText("" + e.getMessage());
                    //Toast.makeText(Scanner_Activity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            error.setVisibility(View.GONE);
            fingerprint.scan(Scanner_Activity.this, printHandler, updateHandler);
            key = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView txt_scan = findViewById(R.id.txt_scan);
        txt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_new();
            }
        });

    }

    private void setAdapter() {
        arrayList = getfile();
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        adapterPdfFile = new Adapter_File(arrayList, Scanner_Activity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(Scanner_Activity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterPdfFile);
        if (arrayList.size() < 1) {

        } else {

        }
    }
    Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String errorMessage = "empty";
            int status = msg.getData().getInt("status");
            Intent intent = new Intent();
            intent.putExtra("status", status);
            if (status == Status.SUCCESS) {
                image = msg.getData().getByteArray("img");
                intent.putExtra("img", image);
            } else {
                errorMessage = msg.getData().getString("errorMessage");
                intent.putExtra("errorMessage", errorMessage);
            }

            if (key == 1) {
                bm = BitmapFactory.decodeByteArray(image, 0, image.length);
                img_scan.setImageBitmap(bm);
                dialog_save();
                // img_scan.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_green));
            }

        }
    };

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int status = msg.getData().getInt("status");
            tvError.setText("");
            switch (status) {
                case Status.INITIALISED:
                    tvStatus.setText("Setting up reader");
                    break;
                case Status.SCANNER_POWERED_ON:
                    tvStatus.setText("Reader powered on");
                    break;
                case Status.READY_TO_SCAN:
                    tvStatus.setText("Ready to scan finger");
                    break;
                case Status.FINGER_DETECTED:
                    tvStatus.setText("Finger detected");
                    break;
                case Status.RECEIVING_IMAGE:
                    tvStatus.setText("Receiving image");
                    break;
                case Status.FINGER_LIFTED:
                    tvStatus.setText("Finger has been lifted off reader");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    tvStatus.setText("Reader is off");
                    break;
                case Status.SUCCESS:
                    tvStatus.setText("Fingerprint successfully captured");
                    break;
                case Status.ERROR:
                    tvStatus.setText("Error");
                    tvError.setText(msg.getData().getString("errorMessage"));
                    break;
                default:
                    tvStatus.setText(String.valueOf(status));
                    tvError.setText(msg.getData().getString("errorMessage"));
                    break;

            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                fingerprint.turnOffReader();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        fingerprint.turnOffReader();
        super.onStop();
    }

    private void dialog_new() {
        Dialog dialog_exit = new Dialog(Scanner_Activity.this);
        dialog_exit.setContentView(R.layout.dialog_input);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_exit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_exit.getWindow().setAttributes(lp);

        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_exit.setCancelable(true);

        EditText edt_name = dialog_exit.findViewById(R.id.edt_name);
        TextView txt_cancel = dialog_exit.findViewById(R.id.txt_cancel);
        TextView txt_ok = dialog_exit.findViewById(R.id.txt_ok);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_exit.dismiss();
            }

        });
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_name.getText().toString().equals("")) {
                    edt_name.setError("Enter name");
                    Toast.makeText(Scanner_Activity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        key = 1;
                        error.setVisibility(View.GONE);
                        fingerprint.scan(Scanner_Activity.this, printHandler, updateHandler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog_exit.dismiss();
                }

            }
        });

        if (dialog_exit != null) {
            dialog_exit.show();
        }
    }

    private void dialog_save() {
        Dialog dialog_exit = new Dialog(Scanner_Activity.this);
        dialog_exit.setContentView(R.layout.dialog_save);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_exit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_exit.getWindow().setAttributes(lp);

        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_exit.setCancelable(true);

        TextView txt_retry = dialog_exit.findViewById(R.id.txt_retry);
        TextView txt_yes = dialog_exit.findViewById(R.id.txt_yes);
        txt_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fingerprint.scan(Scanner_Activity.this, printHandler, updateHandler);
                    key = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog_exit.dismiss();
            }

        });
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = fullpath + "/" + name + "-" + System.currentTimeMillis() + ".png";
                dialog_exit.dismiss();
                try (FileOutputStream out = new FileOutputStream(fname)) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Scanner_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                setAdapter();
            }
        });

        if (dialog_exit != null) {
            dialog_exit.show();
        }
    }

    private ArrayList<String> getfile() {
        String folder = getExternalMediaDirs()[0].toString();
        String fullpath = folder + "/" + getString(R.string.app_name);
        ArrayList<String> list = new ArrayList<>();
        File directory = new File(fullpath + "/");
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getPath().endsWith("png")) {
                list.add(files[i].getPath());
            }
        }
        Collections.reverse(list);
        return list;
    }



}