package com.demo.api.framework.exception;

import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.FieldError;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.framework.util.XpertUtil;
import com.demo.api.framework.validation.FieldError;
import com.demo.api.portal.users.domain.PortalUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionHandler {

    private final Log log = LogFactory.getLog(getClass());


    /**
     * Handles constraint violation exceptions
     *
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Map<String, Object> handleConstraintViolationException(ConstraintViolationException ex) {

        Collection<FieldError> errors = FieldError.getErrors(ex.getConstraintViolations());

        log.warn("ConstraintViolationException: " + errors.toString());
        return XpertUtil.mapOf("status", HttpStatus.BAD_REQUEST.value(),
                "exception", "ConstraintViolationException", "errors", errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Collection<FieldError> errors = FieldError.getErrors(ex.getBindingResult().getFieldErrors());
        log.warn("MethodArgumentNotValidException: " + errors.toString());
        return XpertUtil.mapOf("status", HttpStatus.BAD_REQUEST.value(),
                "exception", "MethodArgumentNotValidException", "errors", errors);
    }

    /**
     * Handles multi-error exceptions
     *
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(MultiErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, Object> handleMultiErrorException(MultiErrorException ex) {

        List<FieldError> errors = ex.getErrors();

        log.warn("MultiErrorException: " + errors.toString());
        return XpertUtil.mapOf("status", HttpStatus.BAD_REQUEST.value(),
                "exception", "MultiErrorException",
                "message", ex.getMessage(), "errors", errors);
    }

    /**
     * Handles access-denied exceptions,
     * typically from spring-security when a user
     * tries to access a protected resource or method
     *
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody Map<String, Object>
    handleAuthorizationException(AccessDeniedException ex) {
        log.warn("User does not have proper rights:", ex);
        PortalUser portalUser = XpertUtil.getUser();
        if (portalUser != null && !StringUtils.isEmpty(portalUser.getName())) {
            return XpertUtil.mapOf("status", HttpStatus.FORBIDDEN.value(),
                    "exception", "AccessDeniedException",
                    "message", "access_denied");
        } else {
            return XpertUtil.mapOf("status", HttpStatus.FORBIDDEN.value(),
                    "exception", "AccessDeniedException",
                    "message", "mi_not_login");
        }
    }


    /**
     * Handles version exceptions
     *
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(VersionException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public @ResponseBody Map<String, Object> handleVersionException(VersionException ex) {
        log.warn("VersionException:", ex);
        return XpertUtil.mapOf("status", HttpStatus.CONFLICT.value(),
                "exception", "VersionException", "message", ex.getMessage());
    }

    /**
     * Handles ObjectOptimisticLockingFailure exceptions
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public @ResponseBody
    Map<String, Object> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        log.warn("ObjectOptimisticLockingFailureException: " + ex);
        return XpertUtil.mapOf("status", HttpStatus.EXPECTATION_FAILED.value(),
                "exception", "ObjectOptimisticLockingFailureException", "errors", ex.getMessage());
    }

    /**
     * Handles any other exceptions
     *
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Map<String, Object> handleOtherException(Exception ex) {
        log.error("Internal server error:", ex);
        return XpertUtil.mapOf("status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "exception", ex.getClass().getSimpleName(), "message", ex.getMessage());
    }

	/**
	 * Handles any IOExceptions
	 *
	 * @param ex the IOExceptions
	 * @return the error response
	 */
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(IOException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Map<String, Object> handleOtherIOException(IOException ex) {
		return XpertUtil.mapOf("status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"exception", ex.getClass().getSimpleName(), "message", ex.getMessage());
	}

	/**
	 * Handles any SQLException
	 *
	 * @param ex the SQLException
	 * @return the error response
	 */
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(SQLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Map<String, Object> handleOtherSQLException(SQLException ex) {
		log.warn("SQLException: " + ex);
		String exception = XpertUtil.getMessage("paasxpert.portal.portalCommon.internalServer");
		String message = XpertUtil.getMessage("paasxpert.portal.portalCommon.internalServerMsg");
		return XpertUtil.mapOf("status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"exception", exception, "message", message);
	}

    /**
     * Handles Portal BizException (Client Error)
     * @param ex the exception
     * @return the error response
     */
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(BizException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST) //CLIENT_ERROR
    public @ResponseBody Map<String, Object> handleBizException(BizException ex) {
        log.warn("BizException: " + ex);
        if (ex.getResponse() != null) {
            return XpertUtil.mapOf("status", ex.getResponse().getStatusCode().value(),
                    "exception", "BizException", "message", ex.getMessage(), "body", ex.getResponse().getBody().toString());
        } else {
            return XpertUtil.mapOf("status", ex.getHttpStatus(),
                    "exception", "BizException", "message", ex.getMessage());
        }
    }

}
