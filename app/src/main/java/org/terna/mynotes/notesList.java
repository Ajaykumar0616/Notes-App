package org.terna.mynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.terna.mynotes.R.id.search;
import static org.terna.mynotes.R.id.toolbar;

public class notesList extends AppCompatActivity implements SearchView.OnQueryTextListener {
    int ADD_NOTE = 1, UPDATE_NOTE = 2; // request code , which is sent by Intent, in case of multiple requests.
    private ArrayList<NoteEnter> notesList = new ArrayList<>();
    ArrayList<NoteEnter> notesListClone;
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private Toolbar toolbar;
    ImageButton add;
   // private Paint p = new Paint();
    int backButtonCount = 0;
    SearchView searchView;
    NoteDB db; // global

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        searchView = (SearchView) findViewById(R.id.searchIt);
        searchView.setOnQueryTextListener(this);

        db = new NoteDB(this, "notedb.db", null, 1);

        prepareNoteData();
        //shayad list ka clone prepareNoteData() ke bad banana hai., kyunki tabhi to ArrayList me data add hoga.ha run
        notesListClone = new ArrayList<>(notesList);
        mAdapter = new NoteAdapter(notesList);
        add = (ImageButton) findViewById(R.id.plusButton);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditor = new Intent(notesList.this, notes.class);
                toEditor.putExtra("whatToDo", "addNote"); //this has to be done so that the other activity will know what this activity is trying to request.
                startActivityForResult(toEditor, ADD_NOTE);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NoteAdapter(notesList);
        recyclerView.setAdapter(mAdapter);
        //We can define ItemTouchHelper Here.
       /* ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/


    }

    private void prepareNoteData()
    {
        notesList = db.getAllNote();
    }

   /* public void deletefromdb(int position)
    {
        NoteEnter noteEnter = notesList.get(position);
        int i = noteEnter.getId();
        db.deleteNoteWith(i);
        notesList.remove(position);
        mAdapter.notifyDataSetChanged();
    }*/




    //This method is called when another activity sends the result to this activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE && data != null) // to know what kinnd of request is made from this activity
        {
            if (resultCode == Activity.RESULT_OK) //to knw if the result is sent succesfully
            {
                NoteEnter returnedModel = (NoteEnter) data.getSerializableExtra("newNote"); //This method return  the Object in serializable form , we have to convert it into NoteEnter.
                notesList.add(returnedModel); // Put the returned Model in ArrayList
                mAdapter.notifyDataSetChanged(); // This Method Notifies The Adapter That The DataSet(in our case ArrayList) has been changed, so refresh it;
            }
        } else if (requestCode == UPDATE_NOTE && data != null) // to update only when the request was made with code UPDATE_NOTE
        {
            int flag = data.getIntExtra("value", -1); // -1 is default value, in case it can't find key value
            //saara information data ke andar save hota hai.
            if (flag == 1) {
                NoteEnter updatedNote = (NoteEnter) data.getSerializableExtra("updatedNote"); // Get the result .
                //Loop through all notes and check the matching nid
                for (NoteEnter temp : notesList) {
                    if (temp.getId() == updatedNote.getId()) {
                        temp.setTitle(updatedNote.getTitle());//Update title and content using the returned NoteEnter Model
                        temp.setContent(updatedNote.getContent());
                    }
                }
                //On Looping through all notes , we have to refresh RecyclerView
                mAdapter.notifyDataSetChanged();
            } else if (flag == 2) {
                boolean result = data.getBooleanExtra("deletedNote", false); // default value false.
                if (result) {
                    int nid = data.getIntExtra("nid", -1);
                    for (NoteEnter temp : notesList) {
                        if (temp.getId() == nid) {
                            notesList.remove(notesList.indexOf(temp));
                            break;// notesList.indexOf() will give us the index where the note is present which is to be deleted, and remove() will remove the note at that index
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.setFilter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.setFilter(newText);
        return true;
    }


    public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>
    {
        public NoteAdapter(ArrayList<NoteEnter> notesList)
        {

        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView title, content;
            public ImageView img;
            RelativeLayout baseLayout;

            public MyViewHolder(View view)
            {
                super(view);
                title = (TextView) view.findViewById(R.id.titelView);
                content = (TextView) view.findViewById(R.id.noteView);
                img = (ImageView) view.findViewById(R.id.noteImage);
                baseLayout = (RelativeLayout) view.findViewById(R.id.baseLayout);

                baseLayout.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int position = getAdapterPosition();
                        NoteEnter noteEnter = notesList.get(position);
                        Intent i = new Intent(notesList.this, detail.class);
                        //No Need to send individual Data, all data is contained inside Model
                        //Just this
                        i.putExtra("noteToUpdate", noteEnter);

                        startActivityForResult(i, UPDATE_NOTE); // sending code UPDATE_NOTE to know that which activity has returned result notes.java OR detail.java
                        //Now The Model Is Sent To detail.java
                    }
                });
            }

        }

        @Override
        public NoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
            return new NoteAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoteAdapter.MyViewHolder holder, int position) {
            NoteEnter noteEnter = notesList.get(position);
            holder.title.setText(noteEnter.getTitle());
            String lines[] = noteEnter.getContent().split("\n");
            // ye kam karega , maine bhi yahi use kiya, lekin ek problem hai, ye sirf first line store karta hai, baki ke store nahi hote.
            if (lines[0].length() < 20) {
                holder.content.setText(lines[0] + "...");
            } else {
                holder.content.setText(lines[0].substring(0, 19) + "...");
            }
            holder.img.setImageResource(R.drawable.b7);
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }

        public void setFilter(String charText) {
            notesList.clear();
            if (charText.isEmpty()) {
                notesList.addAll(notesListClone);
            } else {
                charText = charText.toLowerCase();
                for (NoteEnter item : notesListClone) {
                    if (item.getTitle().toLowerCase().contains(charText) || item.getContent().toLowerCase().contains(charText)) {
                        notesList.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    //isse nikal ke dekh te hain

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            finish();//mujhe yahan problem lag raha hai.
        } else {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


}