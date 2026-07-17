package vn.edu.gdu.springjpalab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.gdu.springjpalab.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository cho Product.
 * Kế thừa JpaRepository -> có sẵn CRUD + phân trang (findAll(Pageable)).
 * Bài 2 (Chương 5): bổ sung Derived Query, @Query (JPQL/Native) và @Modifying.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Derived Query: tìm theo tên (không phân biệt hoa/thường)
    //    -> WHERE UPPER(product_name) LIKE UPPER('%keyword%')
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 2. Derived Query: tìm sản phẩm có giá trong khoảng [min, max]
    //    -> WHERE price BETWEEN ? AND ?
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 3. Custom Query (JPQL): tìm sản phẩm có giá > mức tối thiểu
    //    Viết theo Entity (Product) và field (price), không phải tên bảng/cột.
    @Query("SELECT p FROM Product p WHERE p.price > :minPrice")
    List<Product> findExpensiveProducts(@Param("minPrice") BigDecimal minPrice);

    // 4. Custom Query (Native SQL): lấy 3 sản phẩm giá cao nhất
    //    Viết SQL thật, chạy trực tiếp trên bảng "products".
    @Query(value = "SELECT * FROM products ORDER BY price DESC LIMIT 3", nativeQuery = true)
    List<Product> findTop3ExpensiveProducts();

    // 5. @Modifying + @Query: cập nhật tăng giá theo tỷ lệ (price = price * rate)
    //    UPDATE/DELETE thủ công BẮT BUỘC có @Modifying + @Transactional.
    //    Trả về int = số dòng bị tác động.
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * :rate WHERE p.id = :id")
    int updateProductPrice(@Param("id") Long id, @Param("rate") BigDecimal rate);
}
