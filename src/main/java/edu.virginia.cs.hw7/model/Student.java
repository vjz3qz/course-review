package edu.virginia.cs.hw7.model;

public class Student {

    //Specifications:
//    Students containing the following
//    An id number [Auto increment primary key]
//    A name used for login (could be computing ID) - must be unique
//    A password - a note that you'll be saving the password as plain text, but this is generally "bad practice" - this was simplified because handling that security is beyond the scope of this course.


    private int id;
    private String name;
    private String password;

    public Student(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


