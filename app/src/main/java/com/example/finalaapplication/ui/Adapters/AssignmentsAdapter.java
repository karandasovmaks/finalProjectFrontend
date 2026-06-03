package com.example.finalaapplication.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.view_part_example.AssignmentItem;

import java.util.List;

public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.AssignmentHolder> {

    public interface OnAssignmentActionListener {
        void onTakeTest(AssignmentItem item);
    }

    private Context context;
    private Fragment fragment;
    private List<AssignmentItem> list;
    private String role;
    private String userId;
    private OnAssignmentActionListener listener;

    public AssignmentsAdapter(Context context, Fragment fragment, List<AssignmentItem> list,
                               String role, String userId) {
        this.context = context;
        this.fragment = fragment;
        this.list = list;
        this.role = role;
        this.userId = userId;
        if (fragment instanceof OnAssignmentActionListener) {
            this.listener = (OnAssignmentActionListener) fragment;
        }
    }

    @NonNull
    @Override
    public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_item_layout, parent, false);
        return new AssignmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHolder holder, int position) {
        AssignmentItem item = list.get(position);
        holder.tvTestName.setText(item.getTestName());
        holder.tvClassName.setText(item.getClassName());

        if ("TEACHER".equals(role)) {
            holder.tvTeacherName.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(item.getSubmittedCount() + "/" + item.getTotalCount() + " сдали");
            holder.btnAction.setVisibility(View.GONE);
        } else {
            holder.tvTeacherName.setVisibility(View.VISIBLE);
            holder.tvTeacherName.setText("Учитель: " + item.getTeacherName());
            holder.tvStatus.setVisibility(View.GONE);

            if (item.getSubmittedCount() > 0) {
                holder.btnAction.setText("Пройдено");
                holder.btnAction.setEnabled(false);
            } else {
                holder.btnAction.setText("Пройти");
                holder.btnAction.setEnabled(true);
                holder.btnAction.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onTakeTest(item);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AssignmentHolder extends RecyclerView.ViewHolder {
        private TextView tvTestName, tvClassName, tvTeacherName, tvStatus;
        private androidx.appcompat.widget.AppCompatButton btnAction;

        public AssignmentHolder(@NonNull View itemView) {
            super(itemView);
            tvTestName = itemView.findViewById(R.id.assignment_test_name);
            tvClassName = itemView.findViewById(R.id.assignment_class_name);
            tvTeacherName = itemView.findViewById(R.id.assignment_teacher_name);
            tvStatus = itemView.findViewById(R.id.assignment_status);
            btnAction = itemView.findViewById(R.id.assignment_action_btn);
        }
    }
}
