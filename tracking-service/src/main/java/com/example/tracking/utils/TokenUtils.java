package com.example.tracking.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
public class TokenUtils {

    @Value("tim2")
    private String APP_NAME;

    @Value("somesecret")
    public String SECRET;

    @Value("300000000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    static final String AUDIENCE_MOBILE = "mobile";
    static final String AUDIENCE_TABLET = "tablet";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private String generateAudience() {
//
        return AUDIENCE_WEB;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getNameFromToken(String token) {
        String name;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            name = (String) claims.get("name");
        } catch (Exception e) {
            name = null;
        }
        return name;
    }

    public String getIdFromToken(String token) {
        String id;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            id = (String) claims.get("id");
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    /**
     * vraca bas rolu korisnika
     * @param token
     * @return
     */
    public String getRoleFromToken(String token) {
        String role;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            role = (String) claims.get("role");
            role = getRoleFromParsingArray(role);
        } catch (Exception e) {
            role = null;
        }
        return role;
    }

    /**
     * vraca role i permisije
     * @param token - jwt
     * @return
     */
    public ArrayList<String> getAllAuthorities(String token) {
        String role;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            role = (String) claims.get("role");
        } catch (Exception e) {
            return null;
        }
        String[] rls = role.split("\\|");
        return new ArrayList<String>(Arrays.asList(rls));
    }

    private String getRoleFromParsingArray(String roles) {
        String[] rls = roles.split("\\|");
        String role = "";
        for (int i=0; i< rls.length; i++) {
            if (rls[i].contains("ROLE_")) {
                role = rls[i];
                break;
            }
        }
        return role;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    // Funkcija za preuzimanje JWT tokena iz zahteva
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    // Funkcija za citanje svih podataka iz JWT tokena
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

}

