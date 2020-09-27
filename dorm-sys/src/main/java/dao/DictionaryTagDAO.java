package dao;

import model.DictionaryTag;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DictionaryTagDAO {
    public static List<DictionaryTag> query(String key) {
        List<DictionaryTag> lists = new ArrayList<>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = DBUtil.getConnction();
            String sql = "select concat(d.dictionary_key, dt.dictionary_tag_key) dictionary_tag_key," +
                    "       dictionary_tag_value" +
                    "   from dictionary_tag dt" +
                    "         join dictionary d on d.id = dt.dictionary_id" +
                    "   where d.dictionary_key = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1,key);
            rs = ps.executeQuery();
            while (rs.next()) {
                DictionaryTag tag = new DictionaryTag();
                tag.setDictionaryTagKey(rs.getString("dictionary_tag_key"));
                tag.setDictionaryTagValue(rs.getString("dictionary_tag_value"));
                lists.add(tag);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询数据字典标签出错",e);
        } finally {
            DBUtil.close(c,ps,rs);
        }
        return lists;
    }
}
