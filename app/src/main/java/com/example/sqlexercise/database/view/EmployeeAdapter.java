package com.example.sqlexercise.database.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sqlexercise.R;
import com.example.sqlexercise.database.model.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {
    private Context context;
    private List<Employee> employeesList;

    public EmployeeAdapter(Context context, List<Employee> employeesList) {
        this.context = context;
        this.employeesList = employeesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.employee_list_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Employee employee = employeesList.get(i);
        Log.i("TAG", employee.getName() + employee.getAddress() + employee.getContact());
        myViewHolder.name.setText(employee.getName());
        myViewHolder.address.setText(employee.getAddress());
        myViewHolder.contact.setText(employee.getContact());
        myViewHolder.timestamp.setText(formatDate(employee.getTimestamp()));
        myViewHolder.dot.setText(Html.fromHtml("&#8226;"));
    }

    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public TextView contact;
        public TextView timestamp;
        public TextView dot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_emp_list_name);
            address = itemView.findViewById(R.id.tv_emp_list_address);
            contact = itemView.findViewById(R.id.tv_emp_list_contact);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
            dot = itemView.findViewById(R.id.dot);
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }
}
