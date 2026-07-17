package vn.edu.gdu.springjpalab.entity;

import jakarta.persistence.*;

/**
 * Entity User - bảng "users".
 * Quan hệ Một-Một (1-1) với Profile. Đây là Owning Side (có @JoinColumn)
 * -> giữ cột khóa ngoại "profile_id".
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // cascade = ALL: lưu User thì Profile được lưu theo.
    // @JoinColumn -> tạo cột "profile_id" trong bảng users.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    protected User() {
    }

    public User(String username, Profile profile) {
        this.username = username;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
