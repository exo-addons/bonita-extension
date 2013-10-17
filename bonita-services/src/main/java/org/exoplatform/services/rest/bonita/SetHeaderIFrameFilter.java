package org.exoplatform.services.rest.bonita;


import org.exoplatform.web.filter.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Romain Denarie
 * Date: 09/10/13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class SetHeaderIFrameFilter implements Filter {


	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		String value="ALLOW-FROM http://"+ BonitaService.HOST+":"+BonitaService.PORT;
		((HttpServletResponse)servletResponse).addHeader("X-Frame-Options", value);
		filterChain.doFilter(servletRequest,servletResponse);
	}
}
