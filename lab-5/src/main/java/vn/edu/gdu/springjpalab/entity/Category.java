package vn.edu.gdu.springjpalab.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Category - bảng "categories".
 * Quan hệ Một-Nhiều (1-N) với Product: 1 danh mục có nhiều sản phẩm.
 * Đây là Inverse Side (khai báo mappedBy) -> Product mới là Owning Side (giữ khóa ngoại).
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String name;

    // mappedBy = "category": trỏ tới field "category" bên Product (Owning Side).
    // cascade = ALL + orphanRemoval: lưu/xóa Category thì Product con theo cùng.
    // LAZY: chỉ tải danh sách products khi thực sự gọi getProducts().
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    protected Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
