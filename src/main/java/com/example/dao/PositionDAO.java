package com.example.dao;

import com.example.model.PositionBean;
import java.util.List;

public interface PositionDAO {
    void addPosition(PositionBean position);
    PositionBean getPositionById(int id);
    List<PositionBean> getAllPositions();
    void updatePosition(PositionBean position);
    void deletePosition(int id);
}
