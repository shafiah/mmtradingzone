package com.example.mmtradingzone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.R;
import com.example.mmtradingzone.PdfViewActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

import okhttp3.ResponseBody; // ⭐ NEW (fix for JSON error)
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {

    Context context;
    List<FilesModel> pdfList;

    public PdfAdapter(Context context, List<FilesModel> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pdf, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FilesModel pdf = pdfList.get(position);

        // ⭐ TITLE SHOW
        if (pdf.getTitle() != null && !pdf.getTitle().isEmpty()) {
            holder.txtPdfName.setText(pdf.getTitle());
        } else {
            holder.txtPdfName.setText("PDF " + (position + 1));
        }

        String pdfUrl = "http://18.206.151.182:8085/pdf/" + pdf.getFileName();

        Log.d("PDF_DEBUG", "PDF URL: " + pdfUrl);

        // =====================================================
        // ✅ OPEN PDF
        // =====================================================
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra("pdf_url", pdfUrl);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });

        // =====================================================
        // ⭐ DELETE BUTTON (FINAL FIX)
        // =====================================================
        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            FilesModel selectedPdf = pdfList.get(pos);

            new AlertDialog.Builder(context)
                    .setTitle("Delete PDF")
                    .setMessage(context.getString(R.string.delete_confirm))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {

                        ApiService apiService =
                                ApiClient.getClient(context).create(ApiService.class);

                        // ⭐ FIX: use ResponseBody (avoid JSON error)
                        Call<ResponseBody> call = apiService.deleteFile(selectedPdf.getId());

                        call.enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful() && response.body() != null) {

                                    try {
                                        String msg = response.body().string(); // ⭐ NEW

                                        Toast.makeText(context,
                                                msg,
                                                Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        Toast.makeText(context,
                                                "Deleted Successfully",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ⭐ FINAL FIX: stable refresh
                                    pdfList.remove(pos);
                                    notifyDataSetChanged(); // ⭐ IMPORTANT

                                } else {
                                    Toast.makeText(context,
                                            "Delete Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                Toast.makeText(context,
                                        "Error: " + t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .setNegativeButton(context.getString(R.string.no), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPdfName;
        ImageView pdfIcon;
        ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtPdfName = itemView.findViewById(R.id.txtPdfName);
            pdfIcon = itemView.findViewById(R.id.pdfIcon);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}