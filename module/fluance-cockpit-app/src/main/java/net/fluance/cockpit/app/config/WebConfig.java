/**
 * 
 */
package net.fluance.cockpit.app.config;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.security.util.OAuth2Helper;
import net.fluance.app.web.servlet.filter.CORSFilter;
import net.fluance.app.web.util.RequestHelper;
import net.fluance.cockpit.app.web.filter.DomainPermissionFilter;
import net.fluance.cockpit.app.web.filter.EntitelmentFilter;
import net.fluance.cockpit.app.web.filter.HeadersFilter;
import net.fluance.cockpit.app.web.filter.UserProfileFilter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Value("${app.header.from-public-network}")
	private String fromPublicNetworkHeader;
	
	public static final String API_MAIN_URI_WEATHER = "/weather";
	public static final String API_MAIN_URI_COMPANIES = "/companies";
	public static final String API_MAIN_URI_PATIENTS = "/patients";
	public static final String API_MAIN_URI_PHYSICIAN = "/physician";
	public static final String API_MAIN_URI_VISITS = "/visits";
	public static final String API_MAIN_URI_PATIENTLIST = "/patientlist";
	public static final String API_MAIN_URI_PATIENTDOC = "/patientdoc";
	public static final String API_MAIN_URI_INVOICES = "/invoices";
	public static final String API_MAIN_URI_RADIOLOGY = "/radiology";
	public static final String API_MAIN_URI_LAB = "/lab";
	public static final String API_MAIN_URI_GUARANTOR = "/guarantors";
	public static final String API_MAIN_URI_APPOINTMENTS = "/appointments";
	public static final String API_MAIN_URI_SERVICEFEES = "/servicefees";
	public static final String API_MAIN_URI_ACCESSLOGGER = "/logs";
	public static final String API_MAIN_URI_SYNLAB = "/synlab";
	public static final String API_MAIN_URI_NOTES = "/notes";
	public static final String API_MAIN_URI_SEARCH = "/search";
	public static final String API_MAIN_URI_LOCKS = "/locks";
	public static final String API_MAIN_URI_PICTURES = "/pictures";
	public static final String API_MAIN_URI_NOTESCATEGORIES = "/notescategories";
    public static final String API_MAIN_URI_WHITEBOARD ="/whiteboards";
    public static final String API_MAIN_URI_SURGERYBOARD ="/surgeryboard";
    public static final String API_MAIN_URI_DOCTORS ="/doctors";
    public static final String API_MAIN_PATIENT_NOTES ="/patient-notes";    
    
	private static final int CORS_FILTER_ORDER = 0;
	private static final int ENTITLEMENT_FILTER_ORDER = 10;
	private static final int DOMAIN_PERMISSION_FILTER = 20;
	private static final int USERPROFILE_FILTER_ORDER = 30;
	private static final int HEADERS_FILTER_ORDER = 40;
	
	private static String[] ALL_ENDPOINTS = {API_MAIN_URI_WEATHER + "/*", API_MAIN_URI_PATIENTLIST + "/*", API_MAIN_URI_VISITS + "/*", API_MAIN_URI_INVOICES + "/*"
			, API_MAIN_URI_COMPANIES + "/*" ,API_MAIN_URI_PATIENTS + "/*" ,API_MAIN_URI_APPOINTMENTS + "/*" ,API_MAIN_URI_LAB + "/*"
			, API_MAIN_URI_GUARANTOR + "/*", API_MAIN_URI_RADIOLOGY + "/*", API_MAIN_URI_ACCESSLOGGER + "/*", API_MAIN_URI_SYNLAB + "/*", API_MAIN_URI_SERVICEFEES + "/*", API_MAIN_URI_NOTES + "/*", API_MAIN_URI_PICTURES + "/*",
			API_MAIN_URI_LOCKS + "/*", API_MAIN_URI_NOTESCATEGORIES + "/*", API_MAIN_URI_SEARCH + "/*", API_MAIN_URI_PHYSICIAN + "/*", API_MAIN_URI_WHITEBOARD + "/*", API_MAIN_URI_DOCTORS + "/*",
			API_MAIN_PATIENT_NOTES + "/*", API_MAIN_URI_SURGERYBOARD + "/*"};

	@Bean
	public CORSFilter corsFilter() {
		CORSFilter corsFilter = new CORSFilter();
		if(corsFilter.getFromPublicNetworkHeader() == null) {
			corsFilter.setFromPublicNetworkHeader(fromPublicNetworkHeader);
		}
		return corsFilter;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*").allowedOrigins("*");
	}
	
	@Bean
	public FilterRegistrationBean corsFilterBean(CORSFilter corsFilter) {
		FilterRegistrationBean corsFilterRegistrationBean = new FilterRegistrationBean();
		corsFilterRegistrationBean.setFilter(corsFilter);
		corsFilterRegistrationBean.setOrder(CORS_FILTER_ORDER);
		corsFilterRegistrationBean.addUrlPatterns(ALL_ENDPOINTS);
		corsFilterRegistrationBean.setEnabled(true);
		return corsFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean entitelmentFilterRegistrationBean(EntitelmentFilter entitelmentFilter) {
		FilterRegistrationBean entitlementFilterRegistrationBean = new FilterRegistrationBean();
		entitlementFilterRegistrationBean.setFilter(entitelmentFilter);
		entitlementFilterRegistrationBean.setOrder(ENTITLEMENT_FILTER_ORDER);
		entitlementFilterRegistrationBean.addUrlPatterns(ALL_ENDPOINTS);
		entitlementFilterRegistrationBean.setEnabled(true);
		entitlementFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		return entitlementFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean domainPermissionFilterRegistrationBean(DomainPermissionFilter domainPermissionFilter) {
		FilterRegistrationBean entitlementFilterRegistrationBean = new FilterRegistrationBean();
		entitlementFilterRegistrationBean.setFilter(domainPermissionFilter);
		entitlementFilterRegistrationBean.setOrder(DOMAIN_PERMISSION_FILTER);
		entitlementFilterRegistrationBean.addUrlPatterns(ALL_ENDPOINTS);
		entitlementFilterRegistrationBean.setEnabled(true);
		entitlementFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		return entitlementFilterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean headersFilterRegistrationBean(HeadersFilter headersFilter) {
		FilterRegistrationBean headersFilterRegistrationBean = new FilterRegistrationBean();
		headersFilterRegistrationBean.setFilter(headersFilter);
		headersFilterRegistrationBean.setOrder(HEADERS_FILTER_ORDER);
		headersFilterRegistrationBean.addUrlPatterns(ALL_ENDPOINTS);
		headersFilterRegistrationBean.setEnabled(true);
		headersFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		return headersFilterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean userProfileLoaderRegistrationBean(UserProfileFilter userProfileLoader) {
		FilterRegistrationBean userProfileLoaderRegistrationBean = new FilterRegistrationBean();
		userProfileLoaderRegistrationBean.setFilter(userProfileLoader);
		userProfileLoaderRegistrationBean.setOrder(USERPROFILE_FILTER_ORDER);
		userProfileLoaderRegistrationBean.addUrlPatterns(ALL_ENDPOINTS);
		userProfileLoaderRegistrationBean.setEnabled(true);
		userProfileLoaderRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
		return userProfileLoaderRegistrationBean;
	}
	
	@Bean
	public UserProfileLoader userProfileLoader(){
		return new UserProfileLoader();
	}

	@Bean
	public OAuth2Helper oAuth2Helper() {
		return new OAuth2Helper();
	}

	@Bean
	public RequestHelper requestHelper() {
		return new RequestHelper();
	}
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//	    registry.addInterceptor(new BasicLoggingInterceptor());
//	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseRegisteredSuffixPatternMatch(true);
	}
}
