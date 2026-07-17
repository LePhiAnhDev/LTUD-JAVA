package vn.edu.gdu.springjpalab.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity Course - bảng "courses".
 * Quan hệ Nhiều-Nhiều (N-N) với Student. Đây là Inverse Side (mappedBy = "courses")
 * -> Student giữ cấu hình bảng trung gian @JoinTable("student_course").
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_title", nullable = false, length = 150)
    private String title;

    // mappedBy = "courses": trỏ tới field "courses" bên Student (Owning Side).
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    protected Course() {
    }

    public Course(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
