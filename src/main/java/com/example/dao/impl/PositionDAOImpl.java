package com.example.dao.impl;

import com.example.dao.PositionDAO;
import com.example.model.PositionBean;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDAOImpl implements PositionDAO {

    @Override
    public void addPosition(PositionBean position) {
        String sql = "INSERT INTO positions (position_name, level) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, position.getPositionName());
            pstmt.setInt(2, position.getLevel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PositionBean getPositionById(int id) {
        String sql = "SELECT * FROM positions WHERE id = ?";
        PositionBean position = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    position = new PositionBean();
                    position.setId(rs.getInt("id"));
                    position.setPositionName(rs.getString("position_name"));
                    position.setLevel(rs.getInt("level"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return position;
    }

    @Override
    public List<PositionBean> getAllPositions() {
        String sql = "SELECT * FROM positions ORDER BY id";
        List<PositionBean> positions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PositionBean position = new PositionBean();
                position.setId(rs.getInt("id"));
                position.setPositionName(rs.getString("position_name"));
                position.setLevel(rs.getInt("level"));
                positions.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    @Override
    public void updatePosition(PositionBean position) {
        String sql = "UPDATE positions SET position_name = ?, level = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, position.getPositionName());
            pstmt.setInt(2, position.getLevel());
            pstmt.setInt(3, position.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePosition(int id) {
        String sql = "DELETE FROM positions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
