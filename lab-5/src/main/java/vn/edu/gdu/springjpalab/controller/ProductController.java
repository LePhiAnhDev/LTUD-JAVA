package vn.edu.gdu.springjpalab.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.gdu.springjpalab.entity.Product;
import vn.edu.gdu.springjpalab.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller thao tác dữ liệu Product (Bài 5).
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    // Constructor Injection
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ── 1. Lấy danh sách tất cả sản phẩm ──
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ── 2. Lấy sản phẩm theo ID ──
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── 3. Thêm mới sản phẩm ──
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // ── 4. Cập nhật sản phẩm ──
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody Product productDetails) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setSku(productDetails.getSku());
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    // ── 5. Xóa sản phẩm ──
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ── 6. Đếm tổng số sản phẩm ──
    @GetMapping("/count")
    public long countProducts() {
        return productRepository.count();
    }

    // ── 7. Lấy sản phẩm có Phân trang + Sắp xếp (Bài 1 - Chương 5) ──
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getProductsPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        // Xác định chiều sắp xếp: asc (tăng dần) hay desc (giảm dần)
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Đóng gói yêu cầu phân trang: trang nào, mấy dòng, sắp xếp ra sao
        Pageable pageable = PageRequest.of(page, size, sort);

        // findAll(Pageable) đã có sẵn từ JpaRepository -> trả về Page<Product>
        return ResponseEntity.ok(productRepository.findAll(pageable));
    }

    // ── 8. Tìm kiếm sản phẩm theo từ khóa tên (Bài 2 - Derived Query) ──
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // ── 9. Lọc sản phẩm theo khoảng giá (Bài 2 - Derived Query) ──
    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return productRepository.findByPriceBetween(min, max);
    }

    // ── 10. Lấy sản phẩm đắt hơn mức tối thiểu (Bài 2 - JPQL @Query) ──
    @GetMapping("/expensive")
    public List<Product> getExpensiveProducts(@RequestParam BigDecimal minPrice) {
        return productRepository.findExpensiveProducts(minPrice);
    }

    // ── 11. Lấy Top 3 sản phẩm đắt nhất (Bài 2 - Native SQL @Query) ──
    @GetMapping("/top3")
    public List<Product> getTop3Expensive() {
        return productRepository.findTop3ExpensiveProducts();
    }

    // ── 12. Cập nhật tăng giá sản phẩm theo tỷ lệ (Bài 2 - @Modifying) ──
    @PutMapping("/{id}/increase-price")
    public ResponseEntity<String> increaseProductPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal rate) {
        int updatedRows = productRepository.updateProductPrice(id, rate);
        if (updatedRows > 0) {
            return ResponseEntity.ok("Cập nhật giá thành công cho sản phẩm ID: " + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Không tìm thấy sản phẩm ID: " + id);
    }
}
