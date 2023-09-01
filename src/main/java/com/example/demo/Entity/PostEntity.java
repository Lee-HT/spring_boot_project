package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private UserEntity username;
    @Column(nullable = false, length = 128)
    private String title;
    @Column(nullable = false, length = 1024)
    private String contents;
    @Column(nullable = false, length = 128)
    private String category;

    public void updatePost(String title, String contents, String category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

}
