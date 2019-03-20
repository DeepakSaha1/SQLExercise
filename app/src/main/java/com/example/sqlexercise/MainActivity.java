package com.example.sqlexercise;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlexercise.database.DatabaseHelper;
import com.example.sqlexercise.database.model.Employee;
import com.example.sqlexercise.database.view.EmployeeAdapter;
import com.example.sqlexercise.database.view.MyDividerItemDecoration;
import com.example.sqlexercise.database.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EmployeeAdapter mAdapter;
    private List<Employee> employeeList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noEmployesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noEmployesView = findViewById(R.id.tv_empty_employee_view);

        db = new DatabaseHelper(this);

        employeeList.addAll(db.getAllEmployees());

        mAdapter = new EmployeeAdapter(this, employeeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyEmployes();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmployeeDialog(false, null, -1);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showEmployeeDialog(true, employeeList.get(position), position);
                } else {
                    deleteEmploye(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a employee detail.
     * when shouldUpdate=true, it automatically displays old value and changes the
     * button text to UPDATE
     */

    private void showEmployeeDialog(final boolean shouldUpdate, final Employee employee, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        final View view = layoutInflaterAndroid.inflate(R.layout.employee_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.et_emp_name);
        final EditText inputAddress = view.findViewById(R.id.et_emp_address);
        final EditText inputContact = view.findViewById(R.id.et_emp_contact);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);

        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_employee_title) : getString(R.string.lbl_edit_employee_title));

        if (shouldUpdate && employee != null) {
            inputName.setText(employee.getName());
            inputAddress.setText(employee.getAddress());
            inputContact.setText(employee.getContact());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter Employee Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(inputAddress.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter Employee Address!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(inputContact.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter Employee Contact!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && employee != null) {
                    // update note by it's id
                    updateEmploye(inputName.getText().toString(), inputAddress.getText().toString(), inputContact.getText().toString(), position);
                } else {
                    // create new note
                    createEmployee(inputName.getText().toString(), inputAddress.getText().toString(), inputContact.getText().toString());
                }
            }
        });

    }

//     Toggling list and empty notes view
    private void toggleEmptyEmployes() {
        if (db.getEmployeesCount() > 0) {
            noEmployesView.setVisibility(View.GONE);
        } else {
            noEmployesView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void createEmployee(String name, String address, String contact) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertEmployee(name, address, contact);

        Employee employee = db.getEmployee(id);

        if(employee != null) {
            // adding new note to array list at 0 position
            employeeList.add(0, employee);

            mAdapter.notifyDataSetChanged();
            toggleEmptyEmployes();
        }
    }
    
    private void updateEmploye(String name, String address, String contact, int position) {
        Employee e = employeeList.get(position);
        // updating employee detail
        
        e.setName(name);
        e.setAddress(address);
        e.setContact(contact);

        // updating note in db
        db.updateEmployee(e);
        
        /** Replaces the element at the specified position in this list with the
         * specified element (optional operation).*/
        employeeList.set(position, e);
        mAdapter.notifyDataSetChanged();
        toggleEmptyEmployes();
    }

//    /**
//     * Deleting note from SQLite and removing the
//     * item from the list by its position
//     */
    private void deleteEmploye(int position) {
        // deleting the note from db
        db.deleteEmployee(employeeList.get(position));
        employeeList.remove(position);
        mAdapter.notifyDataSetChanged();

        toggleEmptyEmployes();
    }

}
