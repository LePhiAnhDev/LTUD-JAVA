package vn.edu.gdu.springjpalab.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
