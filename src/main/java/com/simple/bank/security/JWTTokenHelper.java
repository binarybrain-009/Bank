package com.simple.bank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JWTTokenHelper {
    @Value("${jwt.auth.app}")
    private String appName;

    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    @Value("${jwt.auth.expires_in}")
    private Integer expiresIn;

    final private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * get all claims from the token
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token){
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * generate username from the token
     * @param token
     * @return username
     */
    public String getUsernameFromToken(String token){
        String username;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;

    }


    /**
     * generate jwt token
     * @param username
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public String generateToken(String username) throws InvalidKeyException, NoSuchAlgorithmException {
        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, secretKey)
                .compact();
    }

    /**
     * generate expiration date
     * @return
     */
    private Date generateExpirationDate(){
        return new Date(new Date().getTime() + expiresIn*10000);
    }

    /**
     * validate the token
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (
                username != null &&
                        username.equals(userDetails.getUsername()) &&
                        !isTokenExpired(token)
        );
    }

    /**
     * check whether token is expired or not
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token){
        Date expireDate = getExpirationDate(token);
        return expireDate.before(new Date());
    }

    /**
     * get the expiration data for the token
     * @param token
     * @return date
     */
    private Date getExpirationDate(String token){
        Date expireDate;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expireDate = claims.getExpiration();
        }catch (Exception e){
            expireDate = null;
        }
        return expireDate;
    }

    /**
     * get the issue data from the token
     * @param token
     * @return date
     */
    public Date getIssuedAtDateFromToken(String token){
        Date issueAt;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        }catch (Exception e){
            issueAt = null;
        }
        return issueAt;
    }

    /**
     * get the token from the http request
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request){
        String authHeader = getAuthHeaderFromHeader(request);
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * get auth header from the http request header
     * @param request
     * @return
     */
    public String getAuthHeaderFromHeader(HttpServletRequest request){
        return request.getHeader("Authorization");
    }


}
