package com.example.planapp_nhom28_mobile;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import com.example.planapp_nhom28_mobile.Plan;

@Dao
public interface PlanDao {
    @Query("SELECT * FROM `Plan`")
    public List<Plan> getAll();

    @Insert
    void insertPlan(Plan plan);

    @Query("SELECT * FROM 'Plan' WHERE uid = :uid")
    Plan getPlanFromId(int uid);
}
