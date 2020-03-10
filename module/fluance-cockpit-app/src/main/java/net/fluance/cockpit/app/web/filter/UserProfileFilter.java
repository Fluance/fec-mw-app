package net.fluance.cockpit.app.web.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.commons.net.HttpUtils;

@Component
//@ComponentScan("net.fluance.cockpit.app")
public class UserProfileFilter extends GenericFilterBean {

	private static final Logger LOGGER = LogManager.getLogger(UserProfileFilter.class);

	@Autowired
	UserProfileLoader userProfileLoader;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		User user = (User) request.getAttribute(User.USER_KEY);
		if(user == null) {
			LOGGER.warn("No user provided by request (as attribute). " + User.USER_KEY + " attribute = " + user);
		} else {
			UserProfile userProfile = userProfileLoader.loadProfile(user.getUsername(), user.getDomain(), user.getAccessToken());
			request.setAttribute(UserProfile.USER_PROFILE_KEY, userProfile);
		}
		chain.doFilter(request, response);
	}

	/**
	 * Send a real http requestusing the complete URL and the access token
	 * 
	 * @param fullUri
	 * @param token
	 *            accessToken
	 * @return CloseableHttpResponse
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws HttpException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	protected CloseableHttpResponse sendRequest(String fullUri, String token) throws URISyntaxException,
			KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException {
		//TODO : change to full URL
		URI uri = HttpUtils.buildUri(fullUri);
		HttpGet get = HttpUtils.buildGet(uri, null);
		get.setHeader("Authorization", "Bearer " + token);
		CloseableHttpResponse response = HttpUtils.sendGet(get, true);
		return response;
	}

}
