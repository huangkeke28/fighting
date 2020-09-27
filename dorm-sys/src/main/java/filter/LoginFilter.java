package filter;


import model.Response;
import util.JSONUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

//过滤器：http请求的url匹配过滤器路径规则，才会过滤
@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //首页：重定向
        //后端资源：处登录接口外，其他没有登录的返回没有登录的json信息
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        //获取session信息
        HttpSession session = req.getSession(false);//没有session时返回null；
        if (session == null) {
            //获取当前http请求的路径
            String uri = req.getServletPath();
            if ("/public/page/main.html".equals(uri)) {
                //没有登录重定向
                String schema = req.getScheme();//http
                String host = req.getServerName();//服务器域名或ip
                int port = req.getServerPort();//服务器端口号
                String contextPath = req.getContextPath();//项目部署名；
                String basePath = schema + "://" + host + ":" + port + contextPath;
                resp.sendRedirect(basePath + "/public/index.html");
                return;
            } else if (!"/user/login".equals(uri) && !uri.startsWith("/public/") && !uri.startsWith("/static/")) {
                req.setCharacterEncoding("UTF-8");
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("application/json");
                Response r = new Response();
                r.setCode("301");//不是响应状态码 是响应体的字段
                r.setMessage("未授权的http请求");
                PrintWriter pw = resp.getWriter();
                pw.println(JSONUtil.write(r));
                pw.flush();
                return;
            }
        }
        chain.doFilter(request,response);//过滤器向下调用 再次过滤
    }

    @Override
    public void destroy() {

    }
}
