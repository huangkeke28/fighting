import org.junit.Assert;
import org.junit.Test;
import util.DBUtil;

import javax.sql.DataSource;
import java.sql.Connection;

public class DBUtilTest {
    @Test
    public void t1(){
        //Connection c = DBUtil.getConnction();
        Assert.assertNotNull(DBUtil.getConnction());
    }
}
