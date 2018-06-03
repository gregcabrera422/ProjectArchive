package com.untitledhorton.projectarchive.model;

/**
 * Created by Greg on 02/06/2018.
 */

public class SharedNote {
    private String id;
    private String title;
    private String note;
    private String priority;
    private String status;
    private String owner;
    private String isConfirmed;

    public SharedNote(){
    }

    public SharedNote(String id, String title, String note, String priority, String status, String owner) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.priority = priority;
        this.status = status;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
