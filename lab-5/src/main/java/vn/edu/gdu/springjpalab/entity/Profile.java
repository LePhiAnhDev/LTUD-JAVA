package vn.edu.gdu.springjpalab.entity;

import jakarta.persistence.*;

/**
 * Entity Profile - bảng "profiles".
 * Quan hệ Một-Một (1-1) với User. Đây là Inverse Side (mappedBy = "profile")
 * -> User giữ khóa ngoại "profile_id".
 */
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // mappedBy = "profile": trỏ tới field "profile" bên User (Owning Side).
    @OneToOne(mappedBy = "profile")
    private User user;

    protected Profile() {
    }

    public Profile(String bio, String avatarUrl) {
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
