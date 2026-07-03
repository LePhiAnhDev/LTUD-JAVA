# Lab 3 — Cấu hình Spring Boot Cơ bản

Dự án Spring Boot minh họa `@SpringBootApplication`, ngoại hóa cấu hình bằng
`application.properties`, tiêm cấu hình với `@Value`, chạy code khởi động bằng
`CommandLineRunner`, và chuyển môi trường bằng **Spring Profiles**.

## Cấu trúc

```
lab-3/
├── pom.xml                       # spring-boot-starter-parent 3.2.3 + spring-boot-starter
└── src/main/
    ├── java/com/lab/boot/
    │   ├── Lab3Application.java             # @SpringBootApplication + main()
    │   ├── services/NotificationService.java # @Service — inject config bằng @Value
    │   └── runner/AppRunner.java            # @Component implements CommandLineRunner
    └── resources/
        ├── application.properties           # cấu hình cơ sở + spring.profiles.active=dev
        ├── application-dev.properties        # override cho profile 'dev'
        └── application-prod.properties        # override cho profile 'prod'
```

## Cách chạy

- **IntelliJ IDEA:** mở thư mục `lab-3`, đợi Maven import, chạy `Lab3Application.java`.
- **Dòng lệnh (profile mặc định = `dev`, đã đặt trong `application.properties`):**
  ```
  mvn spring-boot:run
  ```
- **Chạy với profile `prod`:**
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=prod
  ```
- **Hoặc đóng gói rồi chạy jar:**
  ```
  mvn package
  java -jar target/lab3-spring-boot-1.0.jar                          # dev
  java -jar target/lab3-spring-boot-1.0.jar --spring.profiles.active=prod
  ```

Kết quả quan sát được:

| Profile | channel | from | max-retries | Output dòng gửi |
|--------|---------|------|-------------|-----------------|
| `dev`  | console (override) | dev@localhost (override) | 3 (từ file cơ sở) | `[CONSOLE] From: dev@localhost | ... (retry=3)` |
| `prod` | email  | system@company.com | 5 (override) | `[EMAIL] From: system@company.com | ... (retry=5)` |

---

## Câu hỏi ôn tập

### 1. `@SpringBootApplication` gồm những annotation nào? Mỗi cái làm gì?

Là một **meta-annotation** gộp 3 annotation chính:

- **`@SpringBootConfiguration`** (một biến thể của `@Configuration`): đánh dấu class là
  nguồn định nghĩa bean / cấu hình của ứng dụng.
- **`@ComponentScan`**: tự động quét và đăng ký mọi `@Component`, `@Service`,
  `@Repository`, `@Controller`... trong package hiện tại **và các package con** (ở đây là
  `com.lab.boot.*`).
- **`@EnableAutoConfiguration`**: Spring Boot **tự động cấu hình** dựa trên các thư viện
  có sẵn trên classpath (auto-configuration).

### 2. Khác biệt giữa `@Value("${key}")` và `@Value("${key:default}")`?

- **`@Value("${key}")`**: **bắt buộc** key phải tồn tại. Nếu không tìm thấy trong cấu hình,
  ứng dụng **khởi động lỗi** (`IllegalArgumentException: Could not resolve placeholder 'key'`).
- **`@Value("${key:default}")`**: có **giá trị mặc định** sau dấu `:`. Có key → dùng giá trị
  cấu hình; không có key → dùng `default` và ứng dụng vẫn chạy bình thường.
  Ví dụ trong lab: `@Value("${app.notification.max-retries:3}")` → mặc định `3` nếu thiếu key.

### 3. `CommandLineRunner.run()` được gọi khi nào trong vòng đời ứng dụng?

Được gọi **một lần**, **ngay sau khi Spring Context đã khởi tạo xong và ứng dụng sẵn sàng**,
nhưng **trước khi** `SpringApplication.run(...)` trả về. Trình tự: context refresh xong →
tất cả bean sẵn sàng → chạy các `CommandLineRunner` (và `ApplicationRunner`) → sau đó
`main()` tiếp tục. Dùng cho tác vụ khởi động: seed dữ liệu, kiểm tra hệ thống, in thông
tin... Nó nhận `String... args` chính là tham số dòng lệnh.

### 4. Khi đặt `spring.profiles.active=prod`, `application.properties` có còn được đọc không?

**Có.** Spring Boot **luôn load `application.properties` (file cơ sở) trước**, sau đó load
`application-prod.properties` và **ghi đè các key trùng nhau**. Những key chỉ có trong file
cơ sở (không bị profile ghi đè) vẫn giữ nguyên giá trị.

> **Bằng chứng từ lab:** khi chạy profile `dev`, file `application-dev.properties` chỉ override
> `channel` và `from`, nên `max-retries` vẫn = `3` lấy từ `application.properties`. Cơ chế
> "base trước, profile ghi đè sau" là lý do cho điều này.
