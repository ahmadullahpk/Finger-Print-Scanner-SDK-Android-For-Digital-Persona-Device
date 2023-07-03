package com.digitalpersonasdk.biometricscanner.digitalpersona.device;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Scanned_Saved_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    Adapter_File adapterPdfFile;
    private TextView nofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanned_saved_activity);

        nofile = findViewById(R.id.nofile);
        recyclerView = findViewById(R.id.recyclerView);
        setAdapter();



    }

    private void setAdapter() {
        arrayList = getfile();
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        adapterPdfFile = new Adapter_File(arrayList, Scanned_Saved_Activity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(Scanned_Saved_Activity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterPdfFile);
        if (arrayList.size() < 1) {
            nofile.setVisibility(View.VISIBLE);
        } else {
            nofile.setVisibility(View.GONE);
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