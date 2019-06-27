package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author djcken
 * @date 2018/5/28
 */
@Entity
@Table(name = "mall_sort")
public class MallSort {

    private int id;
    private String sortName;
    private int sortState;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ms_name")
    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    @Column(name = "ms_state")
    public int getSortState() {
        return sortState;
    }

    public void setSortState(int sortState) {
        this.sortState = sortState;
    }
}
