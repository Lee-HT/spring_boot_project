package com.example.demo.Entity;

import com.example.demo.Identifier.UidPid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Table(name = "postLike")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UidPid.class)
@ToString
public class PostLikeEntity extends BaseTimeEntity{

    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private UserEntity uid;
    @Id
    @ManyToOne
    @JoinColumn(name = "pid")
    private PostEntity pid;
    private boolean good;
    private boolean hate;
}
