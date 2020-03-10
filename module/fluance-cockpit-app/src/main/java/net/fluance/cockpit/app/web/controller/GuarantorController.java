package net.fluance.cockpit.app.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.jdbc.guarantor.GuarantorDetail;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_GUARANTOR)
public class GuarantorController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(GuarantorController.class);

	@Autowired
	private GuarantorRepository guarantorRepository;
	
	@ApiOperation(value = "Guarantor Details", response = GuarantorDetail.class, tags="Guarantor API")
	@RequestMapping(value = "/{guarantorid}", method = RequestMethod.GET)
	public ResponseEntity<?> getGuarantorDetail(@PathVariable Integer guarantorid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getGuarantorDetail...");
		getLogger().debug("Parameters : id = " + guarantorid);
		try {
			GuarantorDetail guarantor = guarantorRepository.findOne(guarantorid);
			super.systemLog(request, MwAppResourceType.GuarantorDetail, guarantorid.toString());
			return new ResponseEntity<>(guarantor, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(msg);
		return new ResponseEntity<>(grp, status);
	}
}
