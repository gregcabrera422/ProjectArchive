package com.untitledhorton.projectarchive.model;

/**
 * Created by Greg on 16/05/2018.
 */

public class CourseAnnouncement {

    private String AnnouncementText;
    private String date;
    private String teacherName;
    private String photoUrl;

    public CourseAnnouncement(String AnnouncementText, String date, String teacherName, String photoUrl) {
        this.AnnouncementText = AnnouncementText;
        this.date = date;
        this.teacherName = teacherName;
        this.photoUrl = photoUrl;
    }

    public String getAnnouncementText() {
        return AnnouncementText;
    }

    public void setAnnouncementText(String announcementText) {
        AnnouncementText = announcementText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
