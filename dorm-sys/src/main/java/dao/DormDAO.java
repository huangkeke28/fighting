package dao;

import model.DictionaryTag;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DormDAO {
    public static List<DictionaryTag> query(int key) {
        List<DictionaryTag> lists = new ArrayList<>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DBUtil.getConnction();
            String sql = "select d.id, d.dorm_no from dorm d join building b on b.id = d.building_id where b.id = ?";
            ps = c.prepareStatement(sql);
            ps.setInt(1,key);
            rs = ps.executeQuery();
            while (rs.next()) {
                DictionaryTag tag = new DictionaryTag();
                tag.setDictionaryTagKey(rs.getString("id"));
                tag.setDictionaryTagValue(rs.getString("dorm_no"));
                lists.add(tag);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询宿舍标签出错",e);
        } finally {
            DBUtil.close(c,ps,rs);
        }
        return lists;
    }
}
