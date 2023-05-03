package edu.virginia.cs.hw7.model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    //Specifications
    //Courses: note that these fields are only for a course, not individual sections.
    //An id number [Auto increment primary key]
    //Department (such as "CS")
    //Catalog_Number (such as "3140")
    private int id;
    private String department;
    private int catalogNumber;
    List<Review> reviews;

    public Course(String department, int catalogNumber) {
        this.department = department;
        this.catalogNumber = catalogNumber;
        this.reviews = new ArrayList<>();
    }

    public Course(int id, String department, int catalogNumber) {
        this.id = id;
        this.department = department;
        this.catalogNumber = catalogNumber;
        this.reviews = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", department='" + department + '\'' +
                ", catalogNumber='" + catalogNumber + '\'' +
                '}';
    }
}

