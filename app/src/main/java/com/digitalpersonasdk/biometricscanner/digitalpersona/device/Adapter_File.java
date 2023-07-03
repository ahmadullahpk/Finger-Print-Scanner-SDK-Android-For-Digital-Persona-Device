package com.digitalpersonasdk.biometricscanner.digitalpersona.device;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;


public class Adapter_File extends RecyclerView.Adapter<Adapter_File.ViewHolder> {

    Context context;
    String file_path;
    ArrayList<String> mylist = new ArrayList<>();
    String file_name_correct;



    public Adapter_File(ArrayList<String> mylist, Context context) {
        this.mylist = mylist;
        this.context = context;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_files, parent, false);
        return new ViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        file_path = mylist.get(position);
        String name = new File(mylist.get(position)).getName();
        holder.file_name.setText(name);

        holder.image.setImageURI(Uri.parse(file_path));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_file(mylist.get(position));
            }
        });

        holder.optionMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_menu(holder, mylist.get(position), position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView file_name;
        ImageView optionMenuBtn, image;
        private LinearLayout ll_root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            file_name = itemView.findViewById(R.id.title);
            ll_root = itemView.findViewById(R.id.ll_root);
            optionMenuBtn = itemView.findViewById(R.id.optionMenuBtn);
        }
    }

    private void Open_menu(ViewHolder holder, final String f_path, int posi) {
        PopupMenu popupMenu = new PopupMenu(context, holder.optionMenuBtn);
        popupMenu.inflate(R.menu.option_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.open:
                        open_file(f_path);
                        break;

                    case R.id.rename:
                        Open_Dialog_For_Update(f_path, posi, holder);
                        break;
                    case R.id.delete:
                        Open_Dialog_For_Dlt(f_path, posi, holder);
                        break;

                    case R.id.share:
                        share(f_path);
                        break;

                    default:
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }


    private void Open_Dialog_For_Update(String file_name, int posi, ViewHolder holder) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update?");
        builder.setMessage("Update title!");
        final EditText Title = new EditText(context);
        String file = file_name.substring(file_name.lastIndexOf("/") + 1);
        final String file_Ext = file_name.substring(file_name.lastIndexOf(".") + 1);
        file_name_correct = file.substring(0, file.length() - file_Ext.length() - 1);
        Title.setText(file_name_correct);
        builder.setView(Title);
        builder.setCancelable(true);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String update_title = Title.getText().toString();
                final File old_name = new File(file_name);
                File from = new File(old_name.getParent(), file_name_correct + ".png");
                File to = new File(old_name.getParent(), update_title + ".png");
                from.renameTo(to);
                holder.file_name.setText(update_title);
                Toast.makeText(context, "Rename success" + to.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
    }


    private void Open_Dialog_For_Dlt(String fpath, int posi, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete...");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(fpath);
                if (file.exists()) {
                    file.delete();
                }
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                holder.ll_root.setVisibility(View.GONE);

            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    public void share(String filename) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Share");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filename));
        context.startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void open_file(String path) {
        Dialog dialog_exit = new Dialog(context);
        dialog_exit.setContentView(R.layout.dialog_viewer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_exit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER_VERTICAL;
        dialog_exit.getWindow().setAttributes(lp);

        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_exit.setCancelable(true);

        ImageView image = dialog_exit.findViewById(R.id.image);
        image.setImageURI(Uri.parse(path));
        TextView txt_title = dialog_exit.findViewById(R.id.txt_title);
        txt_title.setText(new File(path).getName());

        TextView txt_ok = dialog_exit.findViewById(R.id.txt_ok);
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_exit.dismiss();
            }

        });


        if (dialog_exit != null) {
            dialog_exit.show();
        }
    }


}

