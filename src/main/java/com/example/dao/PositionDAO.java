package com.example.dao;

import com.example.model.PositionBean;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PositionDAO {
    void addPosition(PositionBean position);
    PositionBean getPositionById(@Param("id") int id);
    List<PositionBean> getAllPositions();
    void updatePosition(PositionBean position);
    void deletePosition(@Param("id") int id);
}
