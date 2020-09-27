package dao;

import model.DictionaryTag;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BuildingDAO {
    public static List<DictionaryTag> query() {
        List<DictionaryTag> lists = new ArrayList<>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DBUtil.getConnction();
            String sql = "select b.id as dictionary_tag_key, b.building_name as dictionary_tag_value from dictionary_tag dt join building b on b.id = dt.id";
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DictionaryTag tag = new DictionaryTag();
                tag.setDictionaryTagKey(rs.getString("dictionary_tag_key"));
                tag.setDictionaryTagValue(rs.getString("dictionary_tag_value"));
                lists.add(tag);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询宿舍楼标签出错",e);
        } finally {
            DBUtil.close(c,ps,rs);
        }
        return lists;
    }

}
