package com.ktp.project.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by LinHon 2018/8/21
 */
@Entity
@Table(name = "loan_tran_record")
public class LoanTranSeqno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "user_id")
    private int userId;

    /**
     * 项目编号
     */
    @Column(name = "project_id")
    private int projectId;

    @Column(name = "tran_seq_no")
    private String tranSeqNo;

    @Column(name = "type")
    private int type;

    @Column(name = "create_time")
    private Timestamp createTime;


}
