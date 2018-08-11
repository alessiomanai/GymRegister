package com.alessiomanai.gymregister.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.alessiomanai.gymregister.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupManager {

    private String databaseName = "gymRegister.db";
    private String databaseBackup = "gymRegister_backup.db";

    public BackupManager() {
    }

    public boolean doBackup(Context context) {

        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, databaseBackup);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            return false;
        }

        Toast.makeText(context.getApplicationContext(), R.string.backupdone, Toast.LENGTH_SHORT).show();

        return true;
    }

    public boolean doRestore(Context context) {

        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, databaseBackup);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(context.getApplicationContext(), R.string.restorerr, Toast.LENGTH_SHORT).show();

            return false;
        }

        Toast.makeText(context.getApplicationContext(), R.string.restoredone, Toast.LENGTH_SHORT).show();

        return true;

    }

}
