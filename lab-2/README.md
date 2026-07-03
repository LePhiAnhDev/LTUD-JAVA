# Lab 2 — Debug Vòng đời của Bean

Dự án Maven minh họa vòng đời Spring Bean: thứ tự khởi tạo theo dependency graph,
`@PostConstruct` / `@PreDestroy`, và so sánh **Singleton** với **Prototype**.

## Cấu trúc

```
lab-2/
├── pom.xml                       # spring-context 6.1.4 + jakarta.annotation-api 2.1.1
└── src/main/java/com/lab/lifecycle/
    ├── Main.java                 # chạy chung MỘT ApplicationContext cho cả 3 bước
    ├── config/AppConfig.java     # @Configuration + @ComponentScan("com.lab.lifecycle")
    ├── singleton/ServiceB.java   # @Service — được ServiceA phụ thuộc
    ├── singleton/ServiceA.java   # @Service — constructor injection ServiceB
    ├── managed/ManagedBean.java  # @Component — @PostConstruct / @PreDestroy
    └── prototype/RequestProcessor.java  # @Component @Scope(PROTOTYPE)
```

## Cách chạy

- **IntelliJ IDEA:** mở thư mục `lab-2`, đợi Maven import xong, chạy `Main.java`.
- **Dòng lệnh:** `mvn compile exec:java`

> **Lưu ý về output:** Tài liệu in kết quả *theo từng bước riêng lẻ* (mỗi bước một
> context riêng). `Main.java` ở đây dùng **một context duy nhất** (tạo một lần ở Bước 1,
> chỉ `close()` ở cuối), nên output là **hợp (superset)** của các ảnh chụp trong tài liệu:
> - `[1] Constructor` và `[2] @PostConstruct` của `ManagedBean` xuất hiện **ngay lúc khởi
>   tạo context** (giữa `--- Before/After context ---`), vì nó là singleton được quét.
> - `[3] @PreDestroy` chỉ chạy **một lần** khi `ctx.close()` ở cuối chương trình.
> - `RequestProcessor` (prototype) **im lặng** cho tới khi `getBean()` được gọi.
>
> Đây là hành vi đúng khi có đầy đủ bean, không phải lỗi.

---

## Câu hỏi ôn tập

### 1. Tại sao `ServiceB` được khởi tạo trước `ServiceA`?

Vì `ServiceA` **phụ thuộc** `ServiceB` qua constructor injection
(`public ServiceA(ServiceB serviceB)`). Spring dựng **dependency graph** và khởi tạo
bean theo thứ tự phụ thuộc: muốn tạo `ServiceA`, Spring phải có sẵn `ServiceB` để tiêm
vào constructor → `ServiceB` được tạo trước. Output chứng minh điều này:
`[INIT] ServiceB created` luôn xuất hiện **trước** `[INIT] ServiceA created (ServiceB injected)`.

### 2. `@PostConstruct` khác gì so với viết code khởi tạo trong Constructor?

- **Constructor** chạy ngay khi đối tượng vừa được `new`. Với field/setter injection thì
  lúc này dependency **có thể chưa được tiêm** → dùng chúng trong constructor sẽ lỗi/NPE.
- **`@PostConstruct`** chạy **SAU** khi constructor xong **VÀ** sau khi Spring đã **tiêm
  đầy đủ dependency (DI hoàn tất)**. Vì vậy đây là nơi an toàn để khởi tạo cache, mở kết
  nối DB, validate cấu hình... dựa trên các dependency đã inject.
- Nó cũng tách bạch "tạo object" khỏi "khởi tạo trạng thái/tài nguyên", và là **hook lifecycle
  chuẩn** — Spring bảo đảm gọi đúng một lần khi bean đã sẵn sàng.

### 3. Singleton bean có attribute *mutable* gây vấn đề gì trong môi trường multi-thread?

Singleton = **một instance duy nhất dùng chung** cho toàn ứng dụng. Nếu bean có field
thay đổi được (mutable) và **nhiều thread cùng đọc/ghi**, sẽ xảy ra **race condition**:
dữ liệu không nhất quán, mất cập nhật, lỗi visibility, và rất khó tái hiện. Cách phòng tránh:

- Giữ singleton **stateless** (không lưu trạng thái biến đổi) — cách phổ biến nhất.
- Dùng field `final` / immutable (constructor injection giúp làm điều này).
- Nếu buộc phải có state chung → **đồng bộ hóa** (`synchronized`, `Lock`, biến `Atomic`).
- Chuyển state ra **biến cục bộ / tham số method** thay vì field của bean.

### 4. Tại sao inject Prototype vào Singleton bằng `@Autowired` thông thường lại sai?

Vì **DI chỉ xảy ra một lần** — lúc Spring tạo singleton. Khi đó Spring lấy prototype đúng
**1 lần**, gán vào field và giữ mãi tham chiếu đó. Dù prototype "đáng lẽ" phải tạo mới mỗi
lần lấy, singleton lại **luôn dùng lại cùng một instance** đã tiêm ban đầu → mất ý nghĩa
prototype.

**Cách sửa:** không giữ tham chiếu cố định. Thay vào đó tiêm `ApplicationContext`
(hoặc `ObjectProvider<T>`, hoặc dùng `@Lookup`) và gọi `getBean(...)` **bên trong method**
mỗi khi cần → mỗi lần gọi nhận một instance mới.
