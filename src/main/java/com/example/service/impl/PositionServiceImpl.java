package com.example.service.impl;

import com.example.dao.PositionDAO;
import com.example.model.PositionBean;
import com.example.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionDAO positionDAO;

    @Override
    public List<PositionBean> getAllPositions() {
        return positionDAO.getAllPositions();
    }

    @Override
    public PositionBean getPositionById(int positionId) {
        return positionDAO.getPositionById(positionId);
    }

    @Override
    @Transactional
    public void addPosition(PositionBean positionBean) {
        // Business logic, e.g., validation, can be added here
        positionDAO.addPosition(positionBean);
    }

    @Override
    @Transactional
    public void updatePosition(PositionBean positionBean) {
        // Business logic, e.g., validation, can be added here
        positionDAO.updatePosition(positionBean);
    }

    @Override
    @Transactional
    public void deletePosition(int positionId) {
        // Business logic, e.g., check if position is in use before deleting
        positionDAO.deletePosition(positionId);
    }
}
