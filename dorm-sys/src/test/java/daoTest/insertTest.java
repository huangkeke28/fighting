package daoTest;

import dao.StudentDAO;
import model.Student;
import org.junit.Test;

public class insertTest {
    @Test
    public void t1() {
        Student s = new Student();
        s.setStudentName("你是谁");
        s.setStudentGraduateYear("2020");
        s.setStudentMajor("英语系");
        s.setBuildingName("男生楼-3");
        s.setDormId(2);
        int num = StudentDAO.insert(s);
        System.out.println(num);
    }
}
