package vn.edu.gdu.springjpalab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.gdu.springjpalab.entity.Product;

/**
 * Repository cho Product (Bài 4).
 * Kế thừa JpaRepository để có sẵn các phương thức CRUD mà không cần viết SQL:
 * save, findById, findAll, deleteById, count, existsById...
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
