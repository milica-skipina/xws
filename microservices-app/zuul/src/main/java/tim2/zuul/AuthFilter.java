package tim2.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import feign.FeignException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private RestTemplate restTemplate;

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
           jwt = request.getHeader("authorization");        // za restTemplejt
           newJwt = jwt != null ? jwt : "";
           //System.out.println("MISSING JWT");
           //return null;
       }
       if (jwt != null && !jwt.equals("") && jwt.startsWith("Bearer ")) {
                newJwt = jwt.substring(7);
       } else {
           newJwt = jwt;
       }

        String path = request.getRequestURI().substring(request.getContextPath().length());
       if(path.contains("/ws")) {
           String url = "http://authpoint/auth/verifyAgent";
           HttpHeaders httpHeaders = new HttpHeaders();
           httpHeaders.set("Authorization", "Bearer " + newJwt);
           HttpEntity header = new HttpEntity(httpHeaders);
           try {
               ResponseEntity<String> agentJwt = restTemplate.exchange(url, HttpMethod.GET, header, String.class);
               newJwt = agentJwt.getBody();
           } catch (Exception e) {
               e.printStackTrace();
               newJwt = "";
           }
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
