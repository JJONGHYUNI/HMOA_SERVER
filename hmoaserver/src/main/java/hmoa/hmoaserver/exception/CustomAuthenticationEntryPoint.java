package hmoa.hmoaserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static ExceptionResponseDto errorResponse=
            new ExceptionResponseDto(
                    Code.UNAUTHORIZED_MEMBER.name(),
                    Code.UNAUTHORIZED_MEMBER.getMessage()
            );

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(401);
        JSONObject json = new JSONObject();
        json.put("code",errorResponse.getCode());
        json.put("message", errorResponse.getMessage());
        response.getWriter().print(json);
    }
}
