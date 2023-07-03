package com.digitalpersonasdk.biometricscanner.digitalpersona.device;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity   {

    RelativeLayout menu_home, menu_privacy, menu_rate_us, menu_share_app, menu_moreApps, menu_settings, menu_buy_source;
    private DrawerLayout dr_Layout;
    ImageView nav_btn;
    private ActionBarDrawerToggle drawerToggle;
    private CardView cv_scan_new_finger, cv_scanned, cv_more_apps, cv_rate_app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init_view();

        String folder = getExternalMediaDirs()[0].toString();
        String fullpath = folder + "/" + getString(R.string.app_name);
        File file = new File(fullpath);
        if (!file.exists()) {
            file.mkdirs();
        }



        cv_scan_new_finger = findViewById(R.id.cv_scan_new_finger);
        cv_scanned = findViewById(R.id.cv_scanned);
        cv_more_apps = findViewById(R.id.cv_more_apps);
        cv_rate_app = findViewById(R.id.cv_rate_app);

        cv_scan_new_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_new();
            }
        });

        cv_scanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Scanned_Saved_Activity.class));
            }
        });

        cv_rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        cv_more_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://search?q=pub:" + getString(R.string.accountName));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/search?q=pub:" + getString(R.string.accountName))));
                }
            }
        });


        menu_item_clicks();
        drawerToggle = new ActionBarDrawerToggle(this, dr_Layout, R.string.app_name, R.string.app_name);
        dr_Layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr_Layout.openDrawer(Gravity.LEFT);
            }
        });


    }

    private void dialog_new() {
        Dialog dialog_exit = new Dialog(MainActivity.this);
        dialog_exit.setContentView(R.layout.dialog_input);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_exit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_exit.getWindow().setAttributes(lp);

        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_exit.setCancelable(true);

        EditText edt_named = dialog_exit.findViewById(R.id.edt_name);
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
                if (edt_named.getText().toString().equals("")) {
                    edt_named.setError("Enter name");
                    Toast.makeText(MainActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialog_exit.dismiss();
                    Intent intent = new Intent(MainActivity.this, Scanner_Activity.class);
                    intent.putExtra("name", edt_named.getText().toString());
                    startActivity(intent);
                }

            }
        });

        if (dialog_exit != null) {
            dialog_exit.show();
        }
    }


    private void exitConfirmDialog() {
        Dialog dialog_exit = new Dialog(MainActivity.this);
        dialog_exit.setContentView(R.layout.dialog_exit);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_exit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_exit.getWindow().setAttributes(lp);

        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_exit.setCancelable(true);

        TextView txt_exit = dialog_exit.findViewById(R.id.txt_exit);
        TextView txt_continue = dialog_exit.findViewById(R.id.txt_continue);
        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }

        });
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_exit.dismiss();
            }
        });

        if (dialog_exit != null) {
            dialog_exit.show();
        }
    }

    private void init_view() {

        dr_Layout = findViewById(R.id.drawer);
        nav_btn = findViewById(R.id.nav_btn);
        menu_home = findViewById(R.id.menu_home);
        menu_buy_source = findViewById(R.id.menu_buy_source);
        menu_privacy = findViewById(R.id.menu_privacy);
        menu_rate_us = findViewById(R.id.menu_rate_us);
        menu_share_app = findViewById(R.id.menu_share_app);
        menu_moreApps = findViewById(R.id.menu_moreApps);
        menu_settings = findViewById(R.id.menu_settings);
    }

    private void menu_item_clicks() {

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr_Layout.closeDrawers();
            }
        });



        menu_buy_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr_Layout.closeDrawers();
                dialog_emial();
            }
        });

        menu_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.privacyPolicy);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        menu_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        menu_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = BuildConfig.APPLICATION_ID;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=" + appPackageName;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        menu_moreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://search?q=pub:" + getString(R.string.accountName));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/search?q=pub:" + getString(R.string.accountName))));
                }
            }
        });
        menu_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this, GSS_SettingActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitConfirmDialog();
    }

    private void dialog_emial() {
        Dialog dialog_emial = new Dialog(MainActivity.this);
        dialog_emial.setContentView(R.layout.dialog_email);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_emial.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_emial.getWindow().setAttributes(lp);

        dialog_emial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_emial.setCancelable(true);

        TextView txt_later = dialog_emial.findViewById(R.id.txt_later);
        TextView txt_send = dialog_emial.findViewById(R.id.txt_send);
        txt_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_emial.dismiss();
            }

        });
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_emial.dismiss();
                String[] TO = {getString(R.string.email)};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "We need digital persona app source for Android");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (dialog_emial != null) {
            dialog_emial.show();
        }
    }


}