/**
 * 
 */
package net.fluance.cockpit;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mock.web.MockHttpServletRequest;

import net.fluance.app.security.service.xacml.BalanaXacmlPDP;
import net.fluance.app.security.service.xacml.XacmlPDP;
import net.fluance.app.security.util.JwtHelper;
import net.fluance.cockpit.app.web.Wso2IsMockController;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceListRepository;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.GroupnameRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.LabDataRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;


@Configuration
@ComponentScan(basePackages = {"net.fluance.cockpit.app", "net.fluance.app.test"})
@EnableJpaRepositories
@EnableAutoConfiguration
@PropertySource({"classpath:webapps/conf/core.properties","classpath:webapps/conf/security.properties"})
public class TestConfig {
	
	@Value("${authorized.username}")
	private String authorizedUsername;
	@Value("${authorized.password}")
	private String authorizedPassword;
	@Value("${unauthorized.username}")
	private String unauthorizedUsername;
	@Value("${unauthorized.password}")
	private String unauthorizedPassword;
	
	@Value("${oauth2.token.url}")
	private String oauth2TokenUrl;
	@Value("${oauth2.token.flow}")
	private String oauth2TokenFlow;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * @return the authorizedUsername
	 */
	public String getAuthorizedUsername() {
		return authorizedUsername;
	}

	/**
	 * @return the authorizedPassword
	 */
	public String getAuthorizedPassword() {
		return authorizedPassword;
	}

	/**
	 * @return the unauthorizedUsername
	 */
	public String getUnauthorizedUsername() {
		return unauthorizedUsername;
	}

	/**
	 * @return the unauthorizedPassword
	 */
	public String getUnauthorizedPassword() {
		return unauthorizedPassword;
	}

	/**
	 * @return the oauth2TokenUrl
	 */
	public String getOauth2TokenUrl() {
		return oauth2TokenUrl;
	}

	/**
	 * @return the oauth2TokenFlow
	 */
	public String getOauth2TokenFlow() {
		return oauth2TokenFlow;
	}

	@Bean
	public XacmlPDP xacmlPdp() {
		return new BalanaXacmlPDP();
	}
	
	@Bean
	public InvoiceRepository invoiceRepository() {
		return Mockito.mock(InvoiceRepository.class);
	}

	@Bean
	public InvoiceListRepository invoiceListRepository() {
		return Mockito.mock(InvoiceListRepository.class);
	}

	@Bean
	public PatientInListRepository patientInListRepository() {
		return Mockito.mock(PatientInListRepository.class);
	}

	@Bean
	public PatientRepository patientRepository() {
		return Mockito.mock(PatientRepository.class);
	}

	@Bean
	public PatientContactRepository patientContactRepository() {
		return Mockito.mock(PatientContactRepository.class);
	}
	@Bean
	public PatientNextOfKinRepository patientNextOfKinRepository() {
		return Mockito.mock(PatientNextOfKinRepository.class);
	}
	@Bean
	public PatientNextOfKinContactRepository patientNextOfKinContactRepository() {
		return Mockito.mock(PatientNextOfKinContactRepository.class);
	}
	
	@Bean
	@Primary
	public VisitListRepository visitListRepository() {
		return Mockito.mock(VisitListRepository.class);
	}

	@Bean
	public VisitDetailRepository visitDetailRepository() {
		return Mockito.mock(VisitDetailRepository.class);
	}

	@Bean
	public DiagnosisRepository diagnosisListRepository() {
		return Mockito.mock(DiagnosisRepository.class);
	}

	@Bean
	public TreatmentRepository treatmentListRepository() {
		return Mockito.mock(TreatmentRepository.class);
	}

	@Bean
	public GuarantorListRepository guarantorListRepository() {
		return Mockito.mock(GuarantorListRepository.class);
	}

	@Bean
	public VisitPolicyListRepository policyListRepository() {
		return Mockito.mock(VisitPolicyListRepository.class);
	}

	@Bean
	public VisitPolicyDetailRepository policyDetailRepository() {
		return Mockito.mock(VisitPolicyDetailRepository.class);
	}

	@Bean
	public GroupnameRepository groupnameRepository() {
		return Mockito.mock(GroupnameRepository.class);
	}
	
	@Bean
	public LabDataRepository labDataRepository() {
		return Mockito.mock(LabDataRepository.class);
	}
	
	@Bean
	public HttpServletRequest httpServletRequest() {
		return new MockHttpServletRequest();
	}
	
	@Bean
	public JwtHelper jwtHelper(){
		return new JwtHelper();
	}
	
//	@Bean
//    public InvoiceRepository invoiceRepository() {
//        return Mockito.mock(InvoiceRepository.class);
//    }
	
//	@Bean
//    public InvoiceListRepository invoiceListRepository() {
//        return Mockito.mock(InvoiceListRepository.class);
//    }
	
	@Bean
	public Wso2IsMockController wso2IsMockController() {
		return new Wso2IsMockController();
	}
	
}
