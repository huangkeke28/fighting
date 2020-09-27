package servlet;

import dao.DictionaryTagDAO;
import model.DictionaryTag;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/dict/tag/query")
public class DictionaryTagQueryServlet extends AbstractBaseServlet{
    @Override
    public Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //什么时候通过输入流解析数据（请求体里面是json格式的数据字符串）  什么时候通过getParamater读取数据
        String key = req.getParameter("dictionaryKey");
        List<DictionaryTag> tags = DictionaryTagDAO.query(key);
        return tags;
    }
}
