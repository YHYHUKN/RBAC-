package com.rbac.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtil {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    @Value("${jwt.secret:YWRtaW5yYmFjand0c2VjcmV0a2V5MDEyMzQ1Njc4OWFiY2RlZmc=}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String generateToken(String username, List<String> roles) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", username);
            claims.put("roles", String.join(",", roles));
            long now = System.currentTimeMillis();
            claims.put("iat", now / 1000);
            claims.put("exp", (now + expiration) / 1000);
            String header = base64Url(HEADER.getBytes(StandardCharsets.UTF_8));
            String payload = base64Url(mapper.writeValueAsBytes(claims));
            String sign = sign(header + "." + payload);
            return header + "." + payload + "." + sign;
        } catch (Exception e) {
            throw new RuntimeException("生成token失败", e);
        }
    }

    private String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return base64Url(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public JwtUser parseToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new RuntimeException("token格式错误");
            String expected = sign(parts[0] + "." + parts[1]);
            if (!expected.equals(parts[2])) throw new RuntimeException("签名校验失败");
            Map<String, Object> claims = mapper.readValue(Base64.getUrlDecoder().decode(parts[1]), Map.class);
            Long exp = ((Number) claims.get("exp")).longValue();
            if (exp * 1000 < System.currentTimeMillis()) throw new RuntimeException("token已过期");
            JwtUser user = new JwtUser();
            user.setUsername((String) claims.get("sub"));
            String rolesStr = claims.get("roles") == null ? "" : (String) claims.get("roles");
            user.setRoles(rolesStr.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(rolesStr.split(","))));
            return user;
        } catch (Exception e) {
            throw new RuntimeException("token解析失败: " + e.getMessage());
        }
    }
}
