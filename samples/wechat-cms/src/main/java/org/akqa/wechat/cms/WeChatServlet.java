package org.akqa.wechat.cms;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.dom4j.io.SAXReader;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Sample Navigation Component"),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/sample.navigation"),
        @Property(name = "sling.servlet.paths", value = "/wechat/entry"),
        @Property(name = "sling.servlet.methods", value = "html")
})
public class WeChatServlet extends SlingSafeMethodsServlet {

    @Reference
    private SlingRepository repository;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter responseWriter = response.getWriter();
        SAXReader reader=new SAXReader();
//        responseWriter.println(Calculator.add(4, 2));
//        try {
////            SAXParser saxParser = SAXParserFactoryImpl.newInstance().newSAXParser();
//
//        } catch (Exception e) {
//        }
        try {
            QueryManager queryManager = repository.loginAdministrative(null).getWorkspace().getQueryManager();
//            String input = request.getParameter("q");
//            responseWriter.println("keyword: " + input);
//            responseWriter.println("<br/>");
//            Query query = queryManager.createQuery("select * from nt:unstructured where title='"+input+"'", "sql");
//            Query query = queryManager.createQuery("/jcr:root/content/wechat/posts/element(*, nt:unstructured)[jcr:contains(jcr:title,'" + input + "')] order by @created descending", "xpath");
//            Query query = queryManager.createQuery("/jcr:root/content/wechat/posts/element(*, nt:unstructured)", "xpath");
            doQuery(responseWriter, queryManager, "SELECT * FROM nt:unstructured where title = 'test2'", Query.SQL);
//            doQuery(responseWriter, queryManager, "//element(*,nt:unstructured)", Query.XPATH);
        } catch (RepositoryException e) {
            responseWriter.println(e.getMessage());
        }
        responseWriter.println("<br/>");
        responseWriter.println("查询结束！");
    }

    private void doQuery(PrintWriter responseWriter, QueryManager queryManager, String queryStat, String mode) throws RepositoryException {
        Query query = queryManager.createQuery(queryStat, mode);
        NodeIterator nodes = query.execute().getNodes();
        if (nodes != null) {
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                responseWriter.println(node.getProperty("title").getString());
                responseWriter.println("<br/>");
            }
        }
    }
}
