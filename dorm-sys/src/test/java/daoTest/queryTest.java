package daoTest;

import dao.StudentDAO;
import model.Page;
import model.Student;
import org.junit.Test;

import java.util.List;

public class queryTest {

    @Test
    public void t1() {
        Page p = new Page();
        p.setPageNumber(6);
        p.setPageSize(8);
        p.setSearchText("");
        p.setSortOrder("asc");
        List<Student> s = StudentDAO.query(p);
        for (Object i : s) {
            System.out.println(i.toString());
        }
    }
}
