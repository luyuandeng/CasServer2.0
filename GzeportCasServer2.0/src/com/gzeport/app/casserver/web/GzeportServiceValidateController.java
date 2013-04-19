package com.gzeport.app.casserver.web;

/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketValidationException;
import org.jasig.cas.ticket.proxy.ProxyHandler;
import org.jasig.cas.util.StringHelp;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.Cas20ProtocolValidationSpecification;
import org.jasig.cas.validation.ValidationSpecification;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.gzeport.app.casserver.pojo.BaseCompany;
import com.gzeport.app.casserver.pojo.BaseFuncmodel;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.service.UserService;
import com.gzeport.app.casserver.utils.DESPlus;


/**
 */
public class GzeportServiceValidateController extends AbstractController {

    /** View if Service Ticket Validation Fails. */
    private static final String DEFAULT_SERVICE_FAILURE_VIEW_NAME = "casServiceFailureView";

    /** View if Service Ticket Validation Succeeds. */
    private static final String DEFAULT_SERVICE_SUCCESS_VIEW_NAME = "casServiceSuccessView";

    /** Constant representing the PGTIOU in the model. */
    private static final String MODEL_PROXY_GRANTING_TICKET_IOU = "pgtIou";

    /** Constant representing the Assertion in the model. */
    private static final String MODEL_ASSERTION = "assertion";
    
    private static final String LONIN_USER = "loginuser";
    public static String getLoninUser() {
		return LONIN_USER;
	}

	private static final String USER_COMPANY = "userCompany";
    private static final String USER_FUN_SYSMODEL = "userfunmodels";

    /** The CORE which we will delegate all requests to. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** The validation protocol we want to use. */
    @NotNull
    private Class<?> validationSpecificationClass = Cas20ProtocolValidationSpecification.class;

    /** The proxy handler we want to use with the controller. */
    @NotNull
    private ProxyHandler proxyHandler;

    /** The view to redirect to on a successful validation. */
    @NotNull
    private String successView = DEFAULT_SERVICE_SUCCESS_VIEW_NAME;

    /** The view to redirect to on a validation failure. */
    @NotNull
    private String failureView = DEFAULT_SERVICE_FAILURE_VIEW_NAME;

    /** Extracts parameters from Request object. */
    @NotNull
    private ArgumentExtractor argumentExtractor;

    @NotNull
    private   UserService userService;
    
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
    private  SimpleDateFormat simpleDateFormat  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
     /**
      * @功能: 请求 
      * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午2:09:29
      */
    protected Credentials getServiceCredentialsFromRequest(final HttpServletRequest request) {
        final String pgtUrl = request.getParameter("pgtUrl");
        if (org.springframework.util.StringUtils.hasText (pgtUrl)) {
            try {
                return new HttpBasedServiceCredentials(new URL(pgtUrl));
            } catch (final Exception e) {
                logger.error("Error constructing pgtUrl", e);
            }
        }

        return null;
    }

    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) {
        binder.setRequiredFields("renew");
    }
    
    /**
     * @功能:通过注入的usermanger获取数据处理成客户端�?��返回的权限数
     * @编码: luyd luyuandeng@gzeport.com  2012-5-18下午02:22:56 
     */
    @SuppressWarnings("unchecked")
	protected final ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final WebApplicationService service = this.argumentExtractor.extractService(request);
        final String serviceTicketId = service != null ? service.getArtifactId() : null;

        if (service == null || serviceTicketId == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Could not process request; Service: %s, Service Ticket Id: %s", service, serviceTicketId));
            }
            return generateErrorView("INVALID_REQUEST", "INVALID_REQUEST", null);
        }

        try {
            final Credentials serviceCredentials = getServiceCredentialsFromRequest(request);
            String proxyGrantingTicketId = null;

            // XXX should be able to validate AND THEN use
            if (serviceCredentials != null) {
                try {
                    proxyGrantingTicketId = this.centralAuthenticationService
                        .delegateTicketGrantingTicket(serviceTicketId,
                            serviceCredentials);
                } catch (final TicketException e) {
                    logger.error("TicketException generating ticket for: "
                        + serviceCredentials, e);
                }
            }

            final Assertion assertion = this.centralAuthenticationService.validateServiceTicket(serviceTicketId, service);

            final ValidationSpecification validationSpecification = this.getCommandClass();
            final ServletRequestDataBinder binder = new ServletRequestDataBinder(validationSpecification, "validationSpecification");
            initBinder(request, binder);
            binder.bind(request);

            if (!validationSpecification.isSatisfiedBy(assertion)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("ServiceTicket [" + serviceTicketId + "] does not satisfy validation specification.");
                }
                return generateErrorView("INVALID_TICKET", "INVALID_TICKET_SPEC", null);
            }
            onSuccessfulValidation(serviceTicketId, assertion);
            
            Principal principal = assertion.getChainedAuthentications().get(assertion.getChainedAuthentications().size()-1).getPrincipal();
            BaseUser user=  this.userService.findUserByUserCode(principal.getId());
            Map userMap = new HashMap();
            Map companyMap = new HashMap();
            if(user!=null){
            	 userMap.put("userId",  user.getUserId()!=null?user.getUserId().toString():"");
                 userMap.put("userPwd", StringUtils.trimToEmpty(user.getUserPwd()));
                 userMap.put("userCode", StringUtils.trimToEmpty(user.getUserCode()));
                 userMap.put("userName", StringUtils.trimToEmpty(user.getUserName()));
                 userMap.put("isUse", StringUtils.trimToEmpty(user.getIsUse()));
                 userMap.put("customsCode", StringUtils.trimToEmpty(user.getCustomsCode()));
                 userMap.put("customsCodeExt", StringUtils.trimToEmpty(user.getCustomsCodeExt()));
                 userMap.put("company", StringUtils.trimToEmpty(user.getBaseCompany()!=null?user.getBaseCompany().getCompanyId():""));
                 userMap.put("userLoginType", StringUtils.trimToEmpty(user.getUserLoginType()));
                 userMap.put("createCompany",StringUtils.trimToEmpty(user.getCreateCompany()));
                 userMap.put("userSex", StringUtils.trimToEmpty(user.getUserSex()));
                 userMap.put("businessType", StringUtils.trimToEmpty(user.getBusinessType()));
                 userMap.put("createUser",StringUtils.trimToEmpty(user.getCreateUser()));
                 userMap.put("createTime", StringUtils.trimToEmpty(user.getCreateTime()!=null?simpleDateFormat.format(user.getCreateTime()):null));
                 userMap.put("workUnit", StringUtils.trimToEmpty(user.getWorkUnit()));
                 userMap.put("cardId", StringUtils.trimToEmpty(user.getCardId()));
                 userMap.put("cardType", StringUtils.trimToEmpty(user.getCardType()));
                 userMap.put("regRoleId", StringUtils.trimToEmpty(user.getRegRoleId()));
                 userMap.put("customsCodeExt", StringUtils.trimToEmpty(user.getCustomsCodeExt()));
                 userMap.put("userTel", StringUtils.trimToEmpty(user.getUserTel()));
                 userMap.put("userDpt", StringUtils.trimToEmpty(user.getUserDpt()));
                 userMap.put("userDuty", StringUtils.trimToEmpty(user.getUserDuty()));
                 userMap.put("birthday", StringUtils.trimToEmpty(user.getBirthday()!=null?simpleDateFormat.format(user.getBirthday()):null));
                 userMap.put("EMail", StringUtils.trimToEmpty(user.getEMail()));
                 userMap.put("certificate", StringUtils.trimToEmpty(user.getCertificate()));
                 //userMap.put("clientsDesc", StringUtils.trimToEmpty(user.getClientsDesc()));
                 userMap.put("isModifyPwd", StringUtils.trimToEmpty(user.getIsModifyPwd()));
                 userMap.put("lastLoginTime",StringUtils.trimToEmpty(user.getLastLoginTime()!=null?simpleDateFormat.format(user.getLastLoginTime()):null)); 
                 userMap.put("attachmentid", StringUtils.trimToEmpty(user.getAttachmentid()!=null?user.getAttachmentid().toString():"")); 
                 userMap.put("modifyPwdTime", StringUtils.trimToEmpty(user.getModifyPwdTime()!=null?simpleDateFormat.format(user.getModifyPwdTime()):null));
              
                 String desUserCode = DESPlus.encrypt(user.getUserCode().toLowerCase()    
     					+ "*1900*" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                 userMap.put("desUserCode",desUserCode);    //DES 加密后的用户�?2012-7-27 add
                 
                 BaseCompany company = user.getBaseCompany();
                 if(company!=null){
              	   //  companyMap.put("brokerType", StringUtils.trimToEmpty(company.getBrokerType()));
                      // companyMap.put("coClass", StringUtils.trimToEmpty(company.getCoClass()));
                       companyMap.put("companyId", StringUtils.trimToEmpty(company.getCompanyId()));
                       companyMap.put("companyName", StringUtils.trimToEmpty(company.getCompanyName()));
                       companyMap.put("companyState", StringUtils.trimToEmpty(company.getCompanyState()));
                       companyMap.put("typeId",StringUtils.trimToEmpty(company.getTypeId()!=null?company.getTypeId().toString():""));
                       companyMap.put("customsCode", StringUtils.trimToEmpty(company.getCustomsCode()));
                       //companyMap.put("customsName", StringUtils.trimToEmpty(company.getCustomsName()));
                       companyMap.put("englist", StringUtils.trimToEmpty(company.getEnglist()));
                       companyMap.put("engName", StringUtils.trimToEmpty(company.getEngName()));
                       companyMap.put("eportCard", StringUtils.trimToEmpty(company.getEportCard()));
                       companyMap.put("orgCode", StringUtils.trimToEmpty(company.getOrgCode()));
                       
                 }
            }
           Map funSysModel  = null;
           ArrayList modelList =null;
           if(user!=null){
        	   List<BaseFuncmodel> list=  this.userService.getFuncModelsList( String.valueOf(user.getUserId()));
        	     modelList = new ArrayList();
               if(list!=null&&list.size()>0){
            	   for(BaseFuncmodel model :list){
                	   funSysModel=new HashMap();
                	   funSysModel.put("funcCode",  StringHelp.replaceBlank(model.getFuncCode()));
                	   funSysModel.put("funcId", StringUtils.trimToEmpty(model.getFuncId()));
                	   funSysModel.put("funcIslast", StringUtils.trimToEmpty(model.getFuncIslast()));
                	   funSysModel.put("funcIsmenu", StringUtils.trimToEmpty(model.getFuncIsmenu()));
                	   funSysModel.put("funcmodel", StringUtils.trimToEmpty(model.getBaseFuncmodel()!=null?model.getBaseFuncmodel().getFuncId():""));
                	   funSysModel.put("funcResume", StringUtils.trimToEmpty(model.getFuncResume()));
                	   funSysModel.put("funcName", StringUtils.trimToEmpty(model.getFuncName()));
                	   funSysModel.put("funcSysType", StringUtils.trimToEmpty(model.getFuncSysType()));
                	   funSysModel.put("funcUrl",  StringHelp.replaceBlank(model.getFuncUrl()));
                	   funSysModel.put("funcUseType", StringUtils.trimToEmpty(model.getFuncUseType()));
                	   modelList.add(funSysModel);
                   }
               }
           }
            final ModelAndView success = new ModelAndView(this.successView);
            success.addObject(MODEL_ASSERTION, assertion);
/*            
logger.info("LOGIN_USER:::========"+userMap);logger.info("LOGIN_COMPANY:::========"+companyMap); logger.info("LOGIN_FUNMODELLIST:::========"+modelList);
*/
            success.addObject(LONIN_USER,userMap);
            success.addObject(USER_COMPANY,companyMap);
            success.addObject(USER_FUN_SYSMODEL,modelList);
            
            if (serviceCredentials != null && proxyGrantingTicketId != null) {
                final String proxyIou = this.proxyHandler.handle(serviceCredentials, proxyGrantingTicketId);
                success.addObject(MODEL_PROXY_GRANTING_TICKET_IOU, proxyIou);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Successfully validated service ticket: %s", serviceTicketId));
            }

            return success;
        } catch (final TicketValidationException e) {
            return generateErrorView(e.getCode(), e.getCode(), new Object[] {serviceTicketId, e.getOriginalService().getId(), service.getId()});
        } catch (final TicketException te) {
            return generateErrorView(te.getCode(), te.getCode(),
                new Object[] {serviceTicketId});
        } catch (final UnauthorizedServiceException e) {
            return generateErrorView(e.getMessage(), e.getMessage(), null);
        }
    }

    protected void onSuccessfulValidation(final String serviceTicketId, final Assertion assertion) {
    }

    private ModelAndView generateErrorView(final String code, final String description, final Object[] args) {
        final ModelAndView modelAndView = new ModelAndView(this.failureView);
        final String convertedDescription = getMessageSourceAccessor().getMessage(description, args, description);
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", convertedDescription);

        return modelAndView;
    }

    private ValidationSpecification getCommandClass() {
        try {
            return (ValidationSpecification) this.validationSpecificationClass.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public final void setArgumentExtractor(final ArgumentExtractor argumentExtractor) {
        this.argumentExtractor = argumentExtractor;
    }

    /**
     * @param validationSpecificationClass The authenticationSpecificationClass
     * to set.
     */
    public final void setValidationSpecificationClass(final Class<?> validationSpecificationClass) {
        this.validationSpecificationClass = validationSpecificationClass;
    }

    /**
     * @param failureView The failureView to set.
     */
    public final void setFailureView(final String failureView) {
        this.failureView = failureView;
    }

    /**
     * @param successView The successView to set.
     */
    public final void setSuccessView(final String successView) {
        this.successView = successView;
    }

    /**
     * @param proxyHandler The proxyHandler to set.
     */
    public final void setProxyHandler(final ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }
}
