package net.fluance.cockpit.app.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "springFox.swagger.enable", havingValue = "true")
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getApiInfo())
				.useDefaultResponseMessages(true)
				.globalResponseMessage(RequestMethod.GET, getStatusCodes())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(getPaths())
				.build();
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> getPaths() {
		return or(
				regex("/physician.*"),
				regex("/guarantors.*"),
				regex("/synlab.*"),
				regex("/servicefees.*"),
				regex("/visits.*"),
				regex("/invoices.*"),
				regex("/companies.*"),
				regex("/patients.*"),
				regex("/appointments.*"),
				regex("/logs.*"),
				regex("/radiology.*"),
				regex("/pictures.*"),
				regex("/notes.*"),
				regex("/locks.*"),
				regex("/notescategories.*"),
				regex("/search.*"),
				regex("/patientlist.*"),
				regex("/weather.*"),
				regex("/ical.*"),
				regex("/whiteboards.*"),
				regex("/doctors.*"),
				regex("/surgeryboard.*"));
	}

	private List<ResponseMessage> getStatusCodes() {
		List<ResponseMessage> statusCodes = new ArrayList<>();
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		statusCodes.add(new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseModel(new ModelRef("Error")).build());
		return statusCodes;
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Middleware APIs",
				"Cockpit-APP API's Specifications",
				"FEC-4.x",
				"© 2018 FLUANCE",
				new Contact(
						"Fluance",
						"http://fluance.net/",
						"info@fluance.ch"),
				"© 2018 FLUANCE",
				"https://fluance.net/");
	}

}