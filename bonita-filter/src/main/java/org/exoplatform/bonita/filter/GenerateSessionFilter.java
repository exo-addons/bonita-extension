package org.exoplatform.bonita.filter;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Romain Denarie
 * Date: 07/10/13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class GenerateSessionFilter implements Filter {



	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(GenerateSessionFilter.class.getName());

	protected static final String APISESSION = "apiSession";

	/**
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {



		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();

			//if APISession not exists in httpSession
			APISession apiSession = (APISession) session.getAttribute(APISESSION);
			if (apiSession == null) {


				//getUserName
				String userName = httpRequest.getParameter("username");
				if (userName != null) {
					String defaultPassword="!p@ssw0rd!";

					//create APISession
					apiSession = TenantAPIAccessor.getLoginAPI().login(userName,defaultPassword);

					//put it in httpSession
					session.setAttribute(APISESSION, apiSession);
				}

			}
			String redirect=httpRequest.getParameter("redirect");
			((HttpServletResponse)response).sendRedirect(redirect);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();

		}



	}

	@Override
	public void destroy() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

}