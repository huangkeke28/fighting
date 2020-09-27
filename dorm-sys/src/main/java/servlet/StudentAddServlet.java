package servlet;

import dao.StudentDAO;
import model.Student;
import org.omg.PortableServer.IdAssignmentPolicyOperations;
import util.JSONUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/student/add")
public class StudentAddServlet extends AbstractBaseServlet{
    @Override
    public Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Student s = JSONUtil.read(req.getInputStream(),Student.class);
        int num = StudentDAO.insert(s);
        return null;
    }
}
