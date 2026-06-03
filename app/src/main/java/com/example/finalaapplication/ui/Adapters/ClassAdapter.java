package com.example.finalaapplication.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.view_part_example.ClassItem;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassHolder> {

    public interface OnClassActionListener {
        void onDeleteClass(String classId);
        void onLeaveClass(String classId);
    }

    private Context context;
    private List<ClassItem> list;
    private String userId;
    private OnClassActionListener listener;

    public ClassAdapter(Context context, List<ClassItem> list, String userId, OnClassActionListener listener) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item_layout, parent, false);
        return new ClassHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder holder, int position) {
        ClassItem item = list.get(position);
        holder.tvName.setText(item.getClassName());
        holder.tvCode.setText("Код: " + item.getClassCode());

        if ("TEACHER".equals(item.getRole())) {
            holder.btnAction.setText("Удалить");
            holder.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClass(item.getClassId());
            });
        } else {
            holder.btnAction.setText("Выйти");
            holder.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onLeaveClass(item.getClassId());
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvCode;
        private AppCompatButton btnAction;

        public ClassHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.class_name);
            tvCode = itemView.findViewById(R.id.class_code);
            btnAction = itemView.findViewById(R.id.class_action_btn);
        }
    }
}
