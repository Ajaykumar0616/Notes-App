package org.terna.mynotes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    String save_pattern_key = "pattern_code";
PatternLockView mPatternLockView;
    String  final_pattern;
    String save_pattern;
    boolean flag = false;
    Button enter,step1 ,step2,step3;
    TextView pw;
    int i = 1;
    NoteDB noteDb = new NoteDB(this, "notepdb.db", null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enter = (Button)findViewById(R.id.buttonView);
        pw = (TextView)findViewById(R.id.password);
        pw.setSelected(true);
        step1=(Button)findViewById(R.id.step1);
        step2=(Button)findViewById(R.id.step2);
        step3=(Button)findViewById(R.id.step3);
       // pw.setSelected(true);
        mPatternLockView = (PatternLockView)findViewById(R.id.PatternView);
        Paper.init(this);
          save_pattern = Paper.book().read(save_pattern_key);
        flag = noteDb.getCount();
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                final_pattern = PatternLockUtils.patternToString(mPatternLockView,pattern);
            }

            @Override
            public void onCleared() {

            }
        });

        if (flag) {

           // pw.setSelected(true);
            pw.setText(" To Change Password strictly follow steps in sequence 1,2,3");
            //pw.setSelected(true);
            enter.setText("Done");
            step1.setText("1");
            step2.setText("2");
            step3.setText("3");
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noteDb.search(final_pattern)){
                        Intent i = new Intent(MainActivity.this, notesList.class);
                        startActivity(i);
                    finish();}
                    else
                        Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteDb.insertIntoNotep(final_pattern);
                    Intent i = new Intent(MainActivity.this, notesList.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "draw previous pattern", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"After this click 2",Toast.LENGTH_SHORT).show();
            }
        });
        step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    }

                    @Override
                    public void onCleared() {

                    }
                });
                if(noteDb.search(final_pattern))
                {
                    Toast.makeText(MainActivity.this,"Now draw new pattern and click 3",Toast.LENGTH_SHORT).show();
                    i++;
                }
                else
                    Toast.makeText(MainActivity.this,"you are not right person",Toast.LENGTH_SHORT).show();
            }
        });
        step3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    }

                    @Override
                    public void onCleared() {

                    }
                });
                noteDb.updateIntoNotep(final_pattern);
                Intent i = new Intent(MainActivity.this, notesList.class);
                startActivity(i);
                finish();
            }
        });
     /*pw.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(i == 1) {
                 Toast.makeText(MainActivity.this, "draw previous pattern", Toast.LENGTH_SHORT).show();
                 Toast.makeText(MainActivity.this,"After this click ma again",Toast.LENGTH_SHORT).show();
                   i++;
             }
             if(i == 2){
             mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                     @Override
                     public void onStarted() {

                     }

                     @Override
                     public void onProgress(List<PatternLockView.Dot> progressPattern) {

                     }

                     @Override
                     public void onComplete(List<PatternLockView.Dot> pattern) {
                         final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                     }

                     @Override
                     public void onCleared() {

                     }
                 });
                 if(noteDb.search(final_pattern))
                 {
                     Toast.makeText(MainActivity.this,"Now draw new pattern and click me again",Toast.LENGTH_SHORT).show();
                     i++;
                 }
                 else
                     Toast.makeText(MainActivity.this,"you are not right person",Toast.LENGTH_SHORT).show();
             }
             if(i == 3){
             mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                 @Override
                 public void onStarted() {

                 }

                 @Override
                 public void onProgress(List<PatternLockView.Dot> progressPattern) {

                 }

                 @Override
                 public void onComplete(List<PatternLockView.Dot> pattern) {
                     final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                 }

                 @Override
                 public void onCleared() {

                 }
             });
             noteDb.updateIntoNotep(final_pattern);
                 i=0;
             Intent i = new Intent(MainActivity.this, notesList.class);
             startActivity(i);
             finish();
             }
         }
     });*/
    }
}
