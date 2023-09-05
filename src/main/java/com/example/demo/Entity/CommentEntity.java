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
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    @ManyToOne
    // referencedColumnName 없을 시 기본 키를 컬럼으로 지정
    @JoinColumn(name = "username", referencedColumnName = "username")
    private UserEntity username;
    @ManyToOne
    @JoinColumn(name = "pid")
    private PostEntity pid;
    @Column(nullable = false, length = 512)
    private String contents;

    public void updateComment(String contents) {
        this.contents = contents;
    }

}
