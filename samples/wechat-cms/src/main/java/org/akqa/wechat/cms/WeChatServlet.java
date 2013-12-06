package org.akqa.wechat.cms;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Service(value=javax.servlet.Servlet.class)
@Properties({
        @Property(name="service.description", value="Sample Navigation Component"),
        @Property(name="sling.servlet.resourceTypes", value="sling/sample.navigation"),
        @Property(name="sling.servlet.paths", value="/wechat/entry"),
        @Property(name="sling.servlet.methods", value="html")
})
public class WeChatServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter responseWriter = response.getWriter();
        responseWriter.println("get通过啦");
    }
}
