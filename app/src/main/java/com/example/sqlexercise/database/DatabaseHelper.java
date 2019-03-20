package com.example.sqlexercise.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sqlexercise.database.model.Employee;

import java.util.ArrayList;
import java.util.List;
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    public static final int DATABASE_VERSION = 1;
//
//    public static final String DATABASE_NAME = "notes.db";
//
//    public DatabaseHelper(Context context   ) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(Note.CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
//        onCreate(db);
//    }
//
//    public long insertNote(String note ) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(Note.COLUMN_NOTE, note);
////        insert row
//        long id = db.insert(Note.TABLE_NAME, null, values);
//
//        db.close();
////        return newly inserted row id
//        return id;
//    }
//
//    public Note getNote(long id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(Note.TABLE_NAME, new String[] {Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
//                Note.COLUMN_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if(cursor != null)
//            cursor.moveToFirst();
//
////        prepare note object
//        Note note = new Note(
//                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
//                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
//                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
//        );
//
//        cursor.close();
//        return note;
//    }
//
//    public List<Note> getAllNotes() {
//        List<Note> notes = new ArrayList<>();
//
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
//                Note.COLUMN_TIMESTAMP + " DESC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Note note = new Note();
//                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
//                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
//                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
//
//                notes.add(note);
//            } while (cursor.moveToNext());
//        }
//
//        // close db connection
//        db.close();
//
//        // return notes list
//        return notes;
//    }
//
//    public int getNotesCount() {
//        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//
//        // return count
//        return count;
//    }
//
//    public int updateNote(Note note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(Note.COLUMN_NOTE, note.getNote());
//
//        // updating row
//        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(note.getId())});
//    }
//
//    public void deleteNote(Note note) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(note.getId())});
//        db.close();
//    }
//}

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "employees.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Employee.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Employee.TABLE_NAME);
        onCreate(db);
    }

    public long insertEmployee(String name, String address, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Employee.COLUMN_NAME, name);
        values.put(Employee.COLUMN_ADDRESS, address);
        values.put(Employee.COLUMN_CONTACT, contact);

        long id = db.insert(Employee.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Employee getEmployee(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Employee.TABLE_NAME, new String[]{Employee.COLUMN_ID, Employee.COLUMN_NAME, Employee.COLUMN_ADDRESS, Employee.COLUMN_CONTACT, Employee.COLUMN_TIMESTAMP},
                Employee.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //prepare Employee obj
        Employee employee = new Employee(
                cursor.getInt(cursor.getColumnIndex(Employee.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Employee.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Employee.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(Employee.COLUMN_CONTACT)),
                cursor.getString((cursor.getColumnIndex(Employee.COLUMN_TIMESTAMP)))
        );

        cursor.close();
        return employee;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        //select * query
        String selectQuery = "SELECT * FROM " + Employee.TABLE_NAME + " ORDER BY " +
                Employee.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getInt(cursor.getColumnIndex(Employee.COLUMN_ID)));
                employee.setName(cursor.getString(cursor.getColumnIndex(Employee.COLUMN_NAME)));
                employee.setAddress(cursor.getString(cursor.getColumnIndex(Employee.COLUMN_ADDRESS)));
                employee.setContact(cursor.getString(cursor.getColumnIndex(Employee.COLUMN_CONTACT)));
                employee.setTimestamp(cursor.getString(cursor.getColumnIndex(Employee.COLUMN_TIMESTAMP)));

                employees.add(employee);
            } while (cursor.moveToNext());

            db.close();
        }
        return employees;
    }

    public int getEmployeesCount() {
        String countQuery = "SELECT * FROM " + Employee.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Employee.COLUMN_NAME, employee.getName());
        contentValues.put(Employee.COLUMN_ADDRESS, employee.getAddress());
        contentValues.put(Employee.COLUMN_CONTACT, employee.getContact());
        contentValues.put(Employee.COLUMN_TIMESTAMP, employee.getTimestamp());

        // updating row
        return db.update(Employee.TABLE_NAME, contentValues, Employee.COLUMN_ID + "=?",
                new String[] {String.valueOf(employee.getId())});
    }

    public void deleteEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Employee.TABLE_NAME, Employee.COLUMN_ID + "=?",
                new String[] {String.valueOf(employee.getId())});
        db.close();
    }
}
