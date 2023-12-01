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
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Builder
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    // JoinColumn : referencedColumnName 없을 시 기본 키를 컬럼으로 지정
    @ManyToOne
    @JoinColumn(name = "uid",nullable = false)
    private UserEntity uid;
    @Column(length = 31)
    private String username;
    @Column(nullable = false, length = 128)
    private String title;
    @Column(nullable = false, length = 1024)
    private String contents;
    @Column(nullable = false, length = 128)
    private String category;
    @ColumnDefault("0")
    private Integer view;

    public void updatePost(String title, String contents, String category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContents(String contents) {
        this.contents = contents;
    }
    public void updateCategory(String category) {
        this.category = category;
    }
    public void updateView(Long viewCount) { this.view += view; }

}
