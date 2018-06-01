package org.terna.mynotes;

import java.io.Serializable;

/**
 * Created by user on 20/08/2017.
 */

public class NoteEnter implements Serializable{ // Make Your Object Serializable
    private String title, content;
    private int id;
    private int image;


    public NoteEnter(){

    }
    public NoteEnter(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
