package com.ktp.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "hatch_user_role")
public class HatchUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "hatch_role_id")
    private Integer hatchRoleRd;

    @Column(name = "current")
    private Boolean current;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHatchRoleRd() {
        return hatchRoleRd;
    }

    public void setHatchRoleRd(Integer hatchRoleRd) {
        this.hatchRoleRd = hatchRoleRd;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
