package org.terna.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class detail extends AppCompatActivity {
    EditText title, content;
    Button update, delete;
    NoteDB db;
    String titleTxt , contentTxt;
    int position;
    NoteEnter currentNote; // The Note Which Is Opened Currently.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new NoteDB(this,"notedb.db",null,1);
        title = (EditText) findViewById(R.id.setTitle);
        content = (EditText) findViewById(R.id.setContent);
        update = (Button) findViewById(R.id.updateBtn);

       Intent intent = getIntent();

        currentNote = (NoteEnter) intent.getSerializableExtra("noteToUpdate"); // we will accept the sent model.
        //Now we have our currently  Opened note's Model
        title.setText(currentNote.getTitle());
        content.setText(currentNote.getContent());

        /*currentNote = (NoteEnter) intent.getSerializableExtra("noteToUpdate"); // we will accept the sent model.
        //Now we have our currently  Opened note's Model
        Intent intent = getIntent();
        titleTxt = getIntent().getStringExtra("title");
        contentTxt = getIntent().getStringExtra("content");
        title.setText(titleTxt);
        content.setText(contentTxt);*/

        //If user clicks on update we simply have to create a new model with updated title and content , but same nid.
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                NoteEnter modelToReturn = db.updateNoteWith(currentNote.getId(), title.getText().toString(),content.getText().toString());
                Intent i = new Intent();
                i.putExtra("updatedNote", modelToReturn);
                i.putExtra("value" , 1);
                setResult(Activity.RESULT_OK, i);
                startActivity(new Intent(detail.this,notesList.class));
                //Now the updated note is returned.
            }
        });
        delete = (Button)findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i = new Intent();
                boolean f =  db.deleteNoteWith(currentNote.getId());
                i.putExtra("deletedNote" ,f);
                i.putExtra("value" , 2);
                i.putExtra("nid", currentNote.getId()); // we need currentNote's nid to delete it in ArrayList.
                setResult(Activity.RESULT_OK,i);

                startActivity(new Intent(detail.this,notesList.class));

            }
        });
        finish();
    }
}
