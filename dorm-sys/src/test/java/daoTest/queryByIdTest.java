package daoTest;

import dao.StudentDAO;
import model.Student;
import org.junit.Test;

public class queryByIdTest {
    @Test
    public void t1() {
        Student s1 = StudentDAO.queryById(3);
        Student s2 = StudentDAO.queryById(8);
        Student s3 = StudentDAO.queryById(10);
        System.out.println(s1.toString());
        System.out.println(s2.toString());
        System.out.println(s3.toString());
    }
}
