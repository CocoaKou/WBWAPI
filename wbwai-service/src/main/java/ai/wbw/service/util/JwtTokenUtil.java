package ai.wbw.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecResolver;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.*;

/**
 * 无状态token的生成和解析
 */
public class JwtTokenUtil {

    /** token秘钥，请勿泄露，请勿随便修改*/
    public static final String SECRET = "test123";

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static CompressionCodecResolver CODECRESOLVER = new DefaultCompressionCodecResolver();
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    /**
     * JWT生成Token.
     *
     * JWT构成: header, payload, signature
     *
     * @param id 登录成功后用户userId, 参数userId不可传空
     * @param expire 过期时间，单位秒
     */
    public static String createToken(String id,int expire) throws Exception {
        Calendar now = Calendar.getInstance();
        //将签名时间提前60秒,避免因服务器时间差造成token在签名时间前使用导致签名校验失败。
        now.add(Calendar.SECOND, -60);
        Date iatDate = now.getTime();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, expire);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        JWTCreator.Builder builder = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP")
                .withClaim("id", null == id ? null : id)
                .withIssuedAt(iatDate); // sign time
                if(expire != 0) {
                    builder.withExpiresAt(expiresDate); // expire time
                }
                String token = builder.sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) throws Exception{
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    /**
     * 根据Token获取userId
     *
     * @param token
     * @return user_id
     */
    public static String getAppUID(String token) throws Exception{
        Map<String, Claim> claims = verifyToken(token);
        if(claims == null){
            throw new Exception("token error!");
        }
        Claim user_id_claim = claims.get("userId");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
            throw new Exception("token verify error!");
        }
        return user_id_claim.asString();
    }

    public static void main(String[] args){
        try {
            long beginTime1 = System.currentTimeMillis();
            String str = createToken("123L",3600 * 24 *30);
            System.out.println("token is:"+str);

            String tk = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNjU4MzAxODc1LCJ1c2VySWQiOiIxMjMiLCJpYXQiOjE2NTU3MDk4MTV9.LjYvEGi0D9PQLoGbcYeD5mcMniaQyslXY_xszEmp5BE";
            System.out.println("value is:"+verifyToken("1234"));
//            String tokens = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNjU4Mjg2Njc4LCJ1c2VySWQiOiIxMjMiLCJpYXQiOjE2NTU2OTQ2MTh9.Og2u5CzJB2UDMR7juxLfRZodojo0Fr2DIysgZUx0Qww";
//            System.out.println("time1:" + (System.currentTimeMillis() - beginTime1));
//            long beginTime2 = System.currentTimeMillis();
//            getAppUID(str);
//            System.out.println("time2:" + (getAppUID(tokens)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析JWT的Payload
     */
    public static String parseJwtPayload(String jwt){
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);
                String token = tokenSeq!=null?tokenSeq.toString():null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            compressionCodec = CODECRESOLVER.resolveCompressionCodec(header);
        }
        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, io.jsonwebtoken.lang.Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        return payload;
    }

    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * 分割字符串进SET
     */
    public static Set<String> split(String str) {
        return split(str, ",");
    }

    /**
     * 分割字符串进SET
     */
    public static Set<String> split(String str, String separator) {

        Set<String> set = new LinkedHashSet();
        if (StringUtils.isEmpty(str))
            return set;
        for (String s : str.split(separator)) {
            set.add(s);
        }
        return set;
    }
}