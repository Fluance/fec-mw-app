package net.fluance.cockpit.app.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.service.domain.LockService;
import net.fluance.cockpit.core.model.jpa.Lock;
import net.fluance.cockpit.core.model.jpa.LockStatus;
import net.fluance.cockpit.core.model.jpa.NotYoursException;

@RestController
@RequestMapping("/locks")
public class LockController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(LockController.class);

	@Autowired
	LockService lockService;
	
	@ApiOperation(value = "create/refresh Lock", response = Lock.class, tags = "Locks API", notes="Lock a resource or just refresh your existing lock")
	@RequestMapping(value = "/lock", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> lock(@RequestParam Long resourceid, @RequestParam String resourcetype, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Generating a new Lock for the " + resourcetype + " : " + resourceid + " ...");
		try{
			resourcetype = resourcetype.toLowerCase();
			Lock lock = lockService.lock(resourceid, resourcetype, request);
			return new ResponseEntity<>(lock, HttpStatus.OK);
		} catch (NotYoursException nye){
			return new ResponseEntity<>(nye.getLock(), HttpStatus.FORBIDDEN);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Unlock", tags = "Locks API", response = Lock.class, notes="Unlock a resource that was locked by the same USER")
	@RequestMapping(value = "/unlock", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> unLock(@RequestParam Long resourceid, @RequestParam String resourcetype, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Deleting the Lock for the " + resourcetype + " : " + resourceid + " ...");
		try{
			resourcetype = resourcetype.toLowerCase();
			lockService.unLock(resourceid, resourcetype, request);
			return new ResponseEntity<>(new LockStatus(false), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Verify", response = LockStatus.class, tags = "Locks API", notes="Use this to check if a resource is locked by ANY user or not")
	@RequestMapping(value = "/verify", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> verify(@RequestParam Long resourceid, @RequestParam String resourcetype, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Verify the Lock for the " + resourcetype + " : " + resourceid + " ...");
		try{
			resourcetype = resourcetype.toLowerCase();
			LockStatus isLocked = lockService.verify(resourceid, resourcetype);
			return new ResponseEntity<>(isLocked, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Refresh", response = Lock.class, tags = "Locks API", notes="Refresh the expiration date of an existing Lock")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> refresh(@RequestParam Long resourceid, @RequestParam String resourcetype, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Deleting the Lock for the " + resourcetype + " : " + resourceid + " ...");
		try{
			resourcetype = resourcetype.toLowerCase();
			Lock lock = lockService.refresh(resourceid, resourcetype, request);
			return new ResponseEntity<>(lock, HttpStatus.OK);
		} catch (NotYoursException nye){
			return new ResponseEntity<>(nye.getLock(), HttpStatus.FORBIDDEN);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	// No DataException is expected here as we are just reading, so just exit with error 500 if happen
	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(msg);
		return new ResponseEntity<>(grp, status);
	}
}
