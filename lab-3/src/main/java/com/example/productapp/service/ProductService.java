package com.example.productapp.service;

import com.example.productapp.model.Product;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bai tap mo rong (4): tach toan bo xu ly du lieu ra khoi Controller.
 *
 * Luu tru san pham trong bo nho (in-memory). ID duoc sinh tu dong bang mot
 * bien dem tang dan, dam bao khong trung ke ca sau khi xoa san pham.
 */
@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    public ProductService() {
        // Du lieu mau ban dau (id se duoc gan lai boi add()).
        add(new Product(0, "Laptop", 15000000));
        add(new Product(0, "Mouse", 250000));
    }

    /** Danh sach tat ca san pham. */
    public List<Product> findAll() {
        return products;
    }

    /**
     * Bai tap mo rong (2): tim kiem san pham theo ten (khong phan biet hoa/thuong).
     * Neu tu khoa rong thi tra ve toan bo danh sach.
     */
    public List<Product> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String kw = keyword.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName() != null && p.getName().toLowerCase().contains(kw)) {
                result.add(p);
            }
        }
        return result;
    }

    /** Tim san pham theo id, tra ve {@code null} neu khong co. */
    public Product findById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /** Them san pham moi, tu dong gan id. */
    public Product add(Product product) {
        product.setId(sequence.incrementAndGet());
        products.add(product);
        return product;
    }

    /**
     * Bai tap mo rong (1): xoa san pham theo id.
     * @return true neu co xoa duoc, false neu khong tim thay.
     */
    public boolean deleteById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    /**
     * Bai tap mo rong (3): format gia tien theo VN, vi du "15.000.000 ₫"
     * (nhom hang nghin bang dau cham). Dung DecimalFormat voi symbols co dinh
     * de ket qua khong phu thuoc locale mac dinh cua may.
     * (Trong template cung co the format truc tiep bang #numbers cua Thymeleaf.)
     */
    public String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,##0", symbols);
        return formatter.format(price) + " ₫";
    }
}
