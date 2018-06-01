package org.terna.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class notes extends AppCompatActivity {

    EditText title, content;
    Button save;
    NoteDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = new NoteDB(this, "notedb.db", null, 1);
        title = (EditText) findViewById(R.id.titleEditTxt);
        content = (EditText) findViewById(R.id.noteEditTxt);
        save = (Button) findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NoteEnter noteEnter= new NoteEnter()
                String titlen = title.getText().toString();
                String contentn = content.getText().toString();

                if (titlen.equals("") || contentn.equals("")) {
                    Toast.makeText(notes.this, "please fill each field", Toast.LENGTH_LONG).show();
                } else {

                    NoteEnter modelToReturn = db.insertIntoNote(titlen, contentn); // now the insert method will return the Model itself.
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("newNote", modelToReturn); //Error Gone.// This Method is giving error, That We Cannot Simply Pass an object, the solution is , just make your object Serializ
                    setResult(Activity.RESULT_OK, returnIntent);
                    startActivity(new Intent(notes.this,notesList.class));
                    // Now The Model is returned back, On Pressing Back Button It Will Be Displayed.
                    // setContentView(R.layout.activity_notes_list);
                    //prepareNoteData();
                }


                title.setText("");
                content.setText("");
               // Toast.makeText(notes.this, "Insert value", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


}
