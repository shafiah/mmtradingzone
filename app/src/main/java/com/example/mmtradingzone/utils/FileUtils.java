package com.example.mmtradingzone.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    // ✅ MAIN METHOD
    public static File getFile(Context context, Uri uri) {

        String path = getPath(context, uri);

        // ✅ CASE 1 : direct file path mil gaya
        if (path != null) {
            return new File(path);
        }

        // ✅ CASE 2 : path null hai (PDF / Document picker)
        return copyUriToFile(context, uri);
    }

    // ✅ EXISTING METHOD (same as your code)
    public static String getPath(Context context, Uri uri) {

        String[] projection = {
                MediaStore.MediaColumns.DATA
        };

        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                null
        );

        if (cursor != null) {

            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            cursor.moveToFirst();

            String path = cursor.getString(column_index);

            cursor.close();

            return path;
        }

        return null;
    }

    // ✅ ADDED METHOD (PDF / Documents support)
    private static File copyUriToFile(Context context, Uri uri) {

        try {

            InputStream inputStream =
                    context.getContentResolver().openInputStream(uri);

            File file =
                    new File(context.getCacheDir(), "temp_upload_file");

            FileOutputStream outputStream =
                    new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {

                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}