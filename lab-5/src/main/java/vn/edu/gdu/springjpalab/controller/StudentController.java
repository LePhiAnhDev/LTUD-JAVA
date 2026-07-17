package vn.edu.gdu.springjpalab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.gdu.springjpalab.entity.Course;
import vn.edu.gdu.springjpalab.entity.Student;
import vn.edu.gdu.springjpalab.repository.CourseRepository;
import vn.edu.gdu.springjpalab.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")

public class StudentController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public  List<Student> getAll() {
        return  studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Student> getByID(@PathVariable Long id){
        Optional<Student> s = studentRepository.findById(id);
        return s.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student saved = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student details) {
        Optional<Student> existing = studentRepository.findById(id);
        if(existing.isPresent()) {
            Student s = existing.get();
            s.setStudentCode(details.getStudentCode());
            s.setFullName(details.getFullName());
            s.setEmail(details.getEmail());
            s.setGpa(details.getGpa());
            s.setEnrollmentDate(details.getEnrollmentDate());
            return  ResponseEntity.ok(studentRepository.save(s));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public long count() {
        return studentRepository.count();
    }

    // ── Đăng ký sinh viên vào một môn học (Bài tập về nhà - Chương 5) ──
    // Ghi dữ liệu vào bảng trung gian "student_course" qua quan hệ N-N.
    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<?> enroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy sinh viên ID: " + studentId);
        }

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy môn học ID: " + courseId);
        }

        Student student = studentOpt.get();
        // Hàm tiện ích tự đồng bộ liên kết hai chiều trước khi lưu.
        student.enrollInCourse(courseOpt.get());
        return ResponseEntity.ok(studentRepository.save(student));
    }
}

