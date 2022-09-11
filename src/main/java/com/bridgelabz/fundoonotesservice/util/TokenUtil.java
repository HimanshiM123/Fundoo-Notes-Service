package com.bridgelabz.fundoonotesservice.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TokenUtil {
    public final String TOKEN_SECRET = "LoginToken";

    public String crateToken(Long id){
        try {
            //to set algorithm
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            String token = JWT.create().
                    withClaim("user_id", id)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }
    public Long decodeToken(String token){
        Long userid;
        //for verification algorithm
        Verification verification = null;
        try {
            verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (IllegalArgumentException e){
            // TODO Auto generated catch block
            e.printStackTrace();
        }
        JWTVerifier jwtVerifier = verification.build();
        // to decode token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Claim claim = decodedJWT.getClaim("user_id");
        userid = claim.asLong();
        return userid;
    }
}
