package com.example.demo.Identifier;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.UserEntity;
import java.util.Objects;
import lombok.Getter;

@Getter
public class UidCid {

    private static final Long SerialVersionUID = -21218428941L;
    // CommentLikeEntity
    private UserEntity uid;
    private CommentEntity cid;

    public UidCid(){

    }
    @Override
    public boolean equals(Object O) {
        if (this == O) {
            return true;
        }
        if (O == null || O.getClass() == this.getClass()) {
            return false;
        }
        UidCid uidCid = (UidCid) O;
        return Objects.equals(this.getCid(), uidCid.getCid()) || Objects.equals(this.getUid(),
                uidCid.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid(), getCid());
    }

}
