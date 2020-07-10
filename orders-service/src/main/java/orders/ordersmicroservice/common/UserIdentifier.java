package orders.ordersmicroservice.common;

import orders.ordersmicroservice.config.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserIdentifier {

    @Autowired
    private TokenUtils tokenUtils;

    private RegularExpressions regularExpressions;

    public UserIdentifier() {
        this.regularExpressions = new RegularExpressions();
    }

    /**
     * data[0] username , [1] name
     * @param request
     * @return
     */
    public String[] extractFromJwt(HttpServletRequest request) {
        String[] data = new String[2];
        String token = tokenUtils.getToken(request);
        data[0] = regularExpressions.isValidCharNum(tokenUtils.getUsernameFromToken(token)) ?
                tokenUtils.getUsernameFromToken(token) : "";
        data[1] = tokenUtils.getNameFromToken(token);
        return data;
    }

    public String roleFromJwt(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        return tokenUtils.getRoleFromToken(token);
    }
}
