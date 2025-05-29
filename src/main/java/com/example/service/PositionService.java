package com.example.service;

import com.example.model.PositionBean;
import java.util.List;

public interface PositionService {
    List<PositionBean> getAllPositions();
    PositionBean getPositionById(int positionId);
    void addPosition(PositionBean positionBean);
    void updatePosition(PositionBean positionBean);
    void deletePosition(int positionId);
}
