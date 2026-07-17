package vn.edu.gdu.springjpalab.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="students")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "gpa", precision = 3, scale = 2)
    private BigDecimal gpa;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    // ── Quan hệ Nhiều-Nhiều (N-N) tới Course (Bài 3 - Chương 5) ──
    // Student là Owning Side -> cấu hình bảng trung gian "student_course".
    // Bỏ qua "students" khi xuất JSON: quan hệ 2 chiều sẽ lặp vô hạn nếu serialize cả hai phía.
    @JsonIgnoreProperties({"students"})
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_course",                          // bảng liên kết trung gian
            joinColumns = @JoinColumn(name = "student_id"),   // FK trỏ về bảng hiện tại (students)
            inverseJoinColumns = @JoinColumn(name = "course_id") // FK trỏ về bảng đối diện (courses)
    )
    private Set<Course> courses = new HashSet<>();

    protected Student() {

    }

    public Student(String studentCode, String fullName, String email, BigDecimal gpa, LocalDate enrollmentDate) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.gpa = gpa;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getId() { return id; }

    public String getStudentCode() { return studentCode;}
    public void setStudentCode(String studentCode) { this.studentCode = studentCode;}

    public String getFullName() { return fullName; }
    public void setFullName( String fullName ) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail( String email ) { this.email = email; }

    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    // ── Hàm tiện ích: đăng ký môn học, tự đồng bộ liên kết 2 chiều ──
    public void enrollInCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }
}
