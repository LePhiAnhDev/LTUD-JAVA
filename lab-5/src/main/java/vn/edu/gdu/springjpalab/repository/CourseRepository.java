package vn.edu.gdu.springjpalab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.gdu.springjpalab.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
