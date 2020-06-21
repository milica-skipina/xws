package tim2.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import feign.FeignException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthFilter extends ZuulFilter {
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
       RequestContext ctx = RequestContext.getCurrentContext();
       HttpServletRequest request = ctx.getRequest();
       String jwt = request.getHeader("Authorization");
       String newJwt = "";
       if (jwt == null) {
           System.out.println("MISSING JWT");
           //return null;
       } else if (!jwt.equals("") && jwt.startsWith("Bearer ")) {
                newJwt = jwt.substring(7);
       } else {
           newJwt = jwt;
       }

       ctx.addZuulRequestHeader("Data", newJwt);
        Set<String> headers = (Set<String>) ctx.get("ignoredHeaders");
        if (headers != null) {
            headers.remove("Authorization");
        }

        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
