package com.ktp.project.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by LinHon 2018/8/20
 */
@Entity
@Table(name = "chat_ignore")
public class ChatIgnore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 主用户ID
     */
    @Column(name = "left_uid")
    private int leftUid;

    /**
     * 副用户ID
     */
    @Column(name = "right_uid")
    private int rightUid;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftUid() {
        return leftUid;
    }

    public void setLeftUid(int leftUid) {
        this.leftUid = leftUid;
    }

    public int getRightUid() {
        return rightUid;
    }

    public void setRightUid(int rightUid) {
        this.rightUid = rightUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
