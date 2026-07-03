package com.example.productapp.controller;

import com.example.productapp.model.Product;
import com.example.productapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

    // Constructor injection: Spring tiem ProductService vao controller.
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Xem danh sach san pham. Ho tro tim kiem theo ten qua tham so ?keyword=...
     * (bai tap mo rong 2). Neu khong co keyword thi hien thi toan bo.
     */
    @GetMapping
    public String listProducts(@RequestParam(name = "keyword", required = false) String keyword,
                               Model model) {
        model.addAttribute("products", productService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "products";
    }

    /** Hien thi form them san pham moi. */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    /**
     * Xu ly submit form them san pham.
     * {@code @Valid} kich hoat validation tren Product; ket qua nam trong BindingResult.
     * Neu co loi -> quay lai form; neu hop le -> luu va redirect ve danh sach.
     */
    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-product";
        }
        productService.add(product);
        return "redirect:/products";
    }

    /** Xem chi tiet san pham theo id. */
    @GetMapping("/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product-detail";
    }

    /** Bai tap mo rong (1): xoa san pham theo id (dung POST). */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
