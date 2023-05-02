package edu.virginia.cs.hw7.model;

public class Student {

    //Specifications:
//    Students containing the following
//    An id number [Auto increment primary key]
//    A name used for login (could be computing ID) - must be unique
//    A password - a note that you'll be saving the password as plain text, but this is generally "bad practice" - this was simplified because handling that security is beyond the scope of this course.


    private int id;
    private String computingId;
    private String password;

    public Student(int id, String computingId, String password) {
        this.id = id;
        this.computingId = computingId;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComputingId() {
        return computingId;
    }

    public void setComputingId(String computingId) {
        this.computingId = computingId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", computingId='" + computingId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


