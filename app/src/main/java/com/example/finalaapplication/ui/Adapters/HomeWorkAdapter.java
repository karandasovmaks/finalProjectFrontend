package com.example.finalaapplication.ui.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.view_part_example.Task;
import com.example.finalaapplication.ui.view_part_example.Test_example;

import java.util.ArrayList;
import java.util.List;

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.TestsHolder> {

    private Context context;
    private Fragment fragment;
    List<Test_example> listOfTests;

    public HomeWorkAdapter(Context context, Fragment fragment, List<Test_example> listOfTests) {
        this.context = context;
        this.fragment = fragment;
        this.listOfTests = listOfTests;
    }

    @NonNull
    @Override
    public TestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_layout, parent, false);
        return new HomeWorkAdapter.TestsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsHolder holder, int position) {
        holder.tv_name.setText(listOfTests.get(position).getTest_name());
        holder.tv_desc.setText(listOfTests.get(position).getTest_description());
        holder.im.setImageResource(listOfTests.get(position).getImage_link());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("test_name", listOfTests.get(position).getTest_name());
                bundle.putString("test_desc", listOfTests.get(position).getTest_description());
                bundle.putString("test_id", listOfTests.get(position).getTest_id());
                bundle.putSerializable("test_task_list", (ArrayList<Task>) listOfTests.get(position).getListoftasks());

                Navigation.findNavController(v).navigate(R.id.action_second_Screen_to_tests_ui, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfTests.size();
    }

    public static class TestsHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_desc;
        private ImageView im;
        private androidx.appcompat.widget.AppCompatButton button;
        public TestsHolder(@NonNull View itemView){
            super(itemView);
            tv_name = itemView.findViewById(R.id.test_name);
            tv_desc = itemView.findViewById(R.id.description);
            im = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button_try);
        }
    }
}
