package vn.edu.gdu.springjpalab.controller;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.gdu.springjpalab.entity.Student;
import vn.edu.gdu.springjpalab.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")

public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
}

