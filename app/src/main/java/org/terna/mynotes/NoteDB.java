package org.terna.mynotes;

/**
 * Created by user on 19/08/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;

public  class NoteDB extends SQLiteOpenHelper
{

    public NoteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE note(nid INTEGER PRIMARY KEY AUTOINCREMENT ,title Text,content Text)");
        db.execSQL("Create table notep( password text)");

    }
    public  boolean insertIntoNotep (String pw){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",pw);
        long rowId = db.insert("notep",null,contentValues);
        db.close();
        if(rowId>0){
            return  true;
        }else
            return false;

    }
    public boolean updateIntoNotep(String pw){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password" , pw);
        Cursor c = db.rawQuery("select password from notep",null);
        c.moveToFirst();
        String cpw = c.getString(c.getColumnIndex("password"));
        String whereClause = " password = ?";
        String[] whereArgs = {cpw};
        long rwId = db.update("notep",contentValues,whereClause,whereArgs);
        db.close();
        if(rwId > 0)
        return true;
        else
            return false;
    }
    public boolean getCount(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from notep ",null);
        if(cursor.moveToFirst())
            if(!((cursor.getString(cursor.getColumnIndex("password"))).equals(null))){
                return  true;
            }
        return  false;
    }
    public boolean search( String input){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from notep" ,null);
        if(cursor.moveToFirst())
        {

            NoteModel notem = new NoteModel();
            notem.password = cursor.getString(cursor.getColumnIndex("password"));
            if(notem.password.equals(input) )
            {

                return true;

            }

        }
        return false;
    }

    public  NoteEnter insertIntoNote( String title ,String content){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",title);
        contentValues.put("content",content);
       // NoteEnter noteEnter=new NoteEnter(title,content);

        long rowId = db.insert("note",null,contentValues);
        //Thers a problem how can we find ID of recently inserted row, For that we have to fetch last record, since ID is autoincremented, it will be in last row
        Cursor cursor = db.rawQuery("SELECT * FROM note", null); // fetch all records
        cursor.moveToLast(); // move Cursor To Last Record.
        int id = cursor.getInt(cursor.getColumnIndex("nid")); // Get Last record's nid, Now We have Complete data to make A new NoteEnter Model.
        NoteEnter insertedNote = new NoteEnter(id, title, content);
        db.close();
        if (rowId > 0)
        {
            return insertedNote;
        }
        else
        {
            return null;
        }


    }
    public  NoteEnter updateNoteWith( int nid, String title ,String content)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",title);
        contentValues.put("content",content);
        String whereClause = " nid = ? ";
        String[] whereArgs = {String.valueOf(nid)};//ha vo toh bhul gya tha delete ka krtai hai

        long rowId = db.update("note",contentValues,whereClause,whereArgs);
        NoteEnter updatedNote = new NoteEnter(nid,title,content);
        db.close();
        if(rowId > 0){
            return updatedNote;
        }else
        {
            return  null;
        }

    }
    public  boolean deleteNoteWith( int nid)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = " nid = ? ";                         //We Will Check It Later
        String[] whereArgs = {String.valueOf(nid)};
        long rowId = db.delete("note",whereClause,whereArgs);
        db.close();
        return rowId > 0;
    }
    public ArrayList<NoteEnter> getAllNote(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM note ",null);
        ArrayList<NoteEnter> notes = new ArrayList<>();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                NoteEnter note = new NoteEnter(cursor.getInt(cursor.getColumnIndex("nid")),cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content")));
                notes.add(note);
                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return  notes;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS note");
        onCreate(db);
    }
}

