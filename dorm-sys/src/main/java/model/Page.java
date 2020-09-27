package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
@ToString
public class Page {
    private String searchText;
    private String sortOrder;
    private Integer pageSize;
    private Integer pageNumber;

    /**
     * request输入流只能获取请求体中数据，依赖程序自己解析
     * request.getParamenter可以获取请求体和url中的数据：k1=v1&k2=v2;
     * @param req
     * @return
     */

    public static Page parse(HttpServletRequest req) {
        Page page = new Page();
        page.searchText = req.getParameter("searchText");
        page.sortOrder = req.getParameter("sortOrder");
        page.pageSize = Integer.parseInt(req.getParameter("pageSize"));
        page.pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
        return page;
    }
}
