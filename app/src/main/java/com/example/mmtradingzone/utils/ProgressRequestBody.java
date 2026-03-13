package com.example.mmtradingzone.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File file;
    private ProgressListener listener;

    public interface ProgressListener {
        void onProgressUpdate(int percentage);
    }

    public ProgressRequestBody(File file, ProgressListener listener) {
        this.file = file;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        long fileLength = file.length();

        byte[] buffer = new byte[2048];

        FileInputStream in = new FileInputStream(file);

        long uploaded = 0;

        int read;

        while ((read = in.read(buffer)) != -1) {

            uploaded += read;

            sink.write(buffer, 0, read);

            int progress = (int) (100 * uploaded / fileLength);

            listener.onProgressUpdate(progress);
        }

        in.close();
    }
}