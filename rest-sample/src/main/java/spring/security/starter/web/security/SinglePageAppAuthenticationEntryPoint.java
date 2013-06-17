package spring.security.starter.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class SinglePageAppAuthenticationEntryPoint extends DigestAuthenticationEntryPoint {

    @Override
   	public void commence(HttpServletRequest request,
   			HttpServletResponse response, AuthenticationException authException)
   			    throws IOException, ServletException {
   		super.commence(request, new StatusCodeMappingHttpResponse(response), authException);
   	}

       private static class StatusCodeMappingHttpResponse extends HttpServletResponseWrapper {
   		public StatusCodeMappingHttpResponse(HttpServletResponse response) {
   			super(response);
   		}

   		@Override
   		public void sendError(int sc, String msg) throws IOException {
   			if (sc == HttpServletResponse.SC_UNAUTHORIZED) {
   				sc = HttpServletResponse.SC_FORBIDDEN;
   			}
   			super.sendError(sc, msg);
   		}
   	}

}
