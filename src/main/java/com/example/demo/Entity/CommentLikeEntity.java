package com.example.demo.Entity;

import com.example.demo.Identifier.UidCid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@Table(name = "commentLike")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UidCid.class)
@ToString
public class CommentLikeEntity extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "uid",nullable = false)
    private UserEntity uid;
    @Id
    @ManyToOne
    @JoinColumn(name = "cid",nullable = false)
    private CommentEntity cid;
    private boolean likes;
    private boolean hate;

    public void updateLikes(boolean likes) {
        this.likes = likes;
    }

    public void updateHates(boolean hate) {
        this.hate = hate;
    }

}
