package com.example.demo.Identifier;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Getter
public class UidPid implements Serializable {

    private static final long SerialVersionUID = -328293210239L;
    // postLikeEntity 식별자 클래스
    private UserEntity uid;
    private PostEntity pid;

    public UidPid(){

    }
    @Override
    public boolean equals(Object O) {
        if (this == O) {
            return true;
        }
        if (O == null || getClass() != O.getClass()) {
            return false;
        }
        UidPid uidPid = (UidPid) O;
        return Objects.equals(getUid(), uidPid.getUid()) && Objects.equals(getPid(),
                uidPid.getPid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid(), getPid());
    }
}
