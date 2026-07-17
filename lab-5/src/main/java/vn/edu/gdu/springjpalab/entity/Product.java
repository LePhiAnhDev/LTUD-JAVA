package vn.edu.gdu.springjpalab.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity Product - ánh xạ tới bảng "products" trong CSDL (Bài 3).
 */
@Entity                       // (1) Đánh dấu đây là Entity JPA
@Table(name = "products")     // (2) Ánh xạ đến bảng "products" trong DB
public class Product {

    @Id                                                 // (3) Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4) Auto-increment (MySQL)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 150) // (5) Cột product_name
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // (6) Kiểu tiền tệ dùng BigDecimal

    @Column(name = "sku", unique = true, nullable = false, length = 50)
    private String sku; // (7) Mã sản phẩm - duy nhất

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ── Quan hệ Nhiều-Một (N-1) tới Category (Bài 3 - Chương 5) ──
    // Product là Owning Side -> giữ cột khóa ngoại "category_id".
    // ManyToOne mặc định EAGER; ép LAZY để tránh N+1 query.
    // Bỏ qua "products" khi xuất JSON: quan hệ 2 chiều sẽ lặp vô hạn nếu serialize cả hai phía.
    // LAZY khiến Hibernate trả về proxy; "hibernateLazyInitializer"/"handler" là field nội bộ
    // của proxy, Jackson không serialize được nên phải bỏ qua.
    @JsonIgnoreProperties({"products", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // ── 1. Constructor không tham số (BẮT BUỘC cho JPA) ──
    protected Product() {
    }

    // ── 2. Constructor có tham số (tiện lợi cho lập trình viên) ──
    public Product(String name, BigDecimal price, String sku) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.createdAt = LocalDateTime.now();
    }

    // ── 3. Constructor có Category (Bài 3 - Chương 5) ──
    public Product(String name, BigDecimal price, String sku, Category category) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    // ── 3. Getter và Setter ──
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price
                + ", sku='" + sku + "', createdAt=" + createdAt + "}";
    }
}
