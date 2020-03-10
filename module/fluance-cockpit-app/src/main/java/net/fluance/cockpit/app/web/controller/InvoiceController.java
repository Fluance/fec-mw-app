package net.fluance.cockpit.app.web.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.InvoiceService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_INVOICES)
public class InvoiceController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private LogService patientAccessLogService;
	
	@ApiOperation(value = "Invoice Details", response = Invoice.class, tags = "Invoice API")
	@RequestMapping(value = "/{invoiceid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getInvoiceById(@PathVariable Long invoiceid, HttpServletRequest request, HttpServletResponse response) {
		try{
			Invoice invoice = invoiceRepo.findOne(invoiceid);
			if(invoice == null) {
				return new ResponseEntity<>(new GenericResponsePayload("Invoice not found."), HttpStatus.NOT_FOUND);
			} else {
				patientAccessLogService.log(MwAppResourceType.INVOICE, invoice, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
				return new ResponseEntity<>(invoice, HttpStatus.OK);
			}
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Invoice List", response = InvoiceList.class, responseContainer = "List", tags = "Invoice API")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getInvoiceList(@RequestParam Long visitnb, @RequestParam(required=false) Long guarantorid, @RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder, @RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, HttpServletRequest request, HttpServletResponse response) {
		try{
			List<InvoiceList> invoiceList = invoiceService.getInvoiceList(visitnb, guarantorid, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.INVOICE_LIST_BY_GUARANTOR, invoiceList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, visitnb, request.getRequestURI(), (guarantorid != null ? guarantorid.toString() : null));
			return new ResponseEntity<>(invoiceList, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Invoice List Count", response = Count.class, tags = "Invoice API")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getInvoiceListCount(@RequestParam Long visitnb, @RequestParam(required=false) Long guarantorid, @RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder, @RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, HttpServletRequest request, HttpServletResponse response) {
		try{
			Integer count = invoiceService.getInvoiceListCount(visitnb, guarantorid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
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