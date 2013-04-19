package com.gzeport.app.casserver.web.flow;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import net.netca.jce.SimplePKCS7SignedData;
import net.netca.secuInter.CertInfo;
import net.netca.util.encoders.Base64;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

import com.gzeport.app.casserver.pojo.BaseCompany;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.pojo.UserCert;
import com.gzeport.app.casserver.service.UserService;
import com.gzeport.casserver.Md5PwdEncoder;
public class GzeportUserCheckAction  {
	
	private UserService userService;
	
	@NotNull
    private CentralAuthenticationService centralAuthenticationService;
	
	 protected Logger logger = LoggerFactory.getLogger(getClass());
	 
	/**
	 * @功能: 验证 
	 * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午2:05:00
	 */
	public final String check(final RequestContext context,final UsernamePasswordCredentials credentials,
			final MessageContext messageContext) throws Exception {
      
		HttpServletRequest request = WebUtils.getHttpServletRequest(context);
	       
	    String certLogin =StringUtils.trimToEmpty( request.getParameter("certThumbPrint"));  //certLogin��Ϊ����֤���¼
	    if(!"".equals(certLogin)){
	    	   return this.certUserCheck(context, credentials, messageContext); 
	      }
	    if (!"".equals( String.valueOf(credentials.getUsername()))) {
	    	   Md5PwdEncoder md5PwdEncoder = new Md5PwdEncoder();
	    	   String entryPwd = md5PwdEncoder.encode(StringUtils.trimToEmpty( credentials.getPassword()));
	    	   BaseUser user=	this.userService.findUserByUserCode(credentials.getUsername());
	    	   if (user != null) {
	        	 //  BaseCompany company = user.getBaseCompany();
	        	   String password = user.getUserPwd();
	                if (!entryPwd.equals(password)) {  //密码验证
	                	context.getFlowScope().put("login_failed_num_to_show_validation_code",context.getFlowScope().getInteger("errorTimes")==null?1:context.getFlowScope().getInteger("login_failed_num_to_show_validation_code")+1 );
	                	/*if(context.getFlowScope().getInteger("errorTimes")>3){
		                	 user.setIsUse("1");	//������������������3���ʻ�
		                	  this.userManager.updateObject(user);
		                  }*///登录错误 次数限制
	                    messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_PASSWORD_ERROR").defaultText("INVALID_USER_PASSWORD_ERROR").build());
	                    return "error";
	                }
	                String isUse = user.getIsUse();
	                if ("0".equals(isUse)) {  //未启用
	                    messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_UNUSE").defaultText("INVALID_USER_UNUSE").build());
	                    return "error";
	                }
	                
	                if (user.getBaseCompany()==null) { //未注册公司
	                	messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_UNCONNCOMPANY").defaultText("INVALID_USER_UNCONNCOMPANY").build());
	                    return "error";
	                }else{   
	                	BaseCompany company =user.getBaseCompany();
	                	String  companyState=StringUtils.trimToEmpty(String.valueOf(company.getCompanyState()));
	                	if( "0" .equals(companyState) ){  //公司
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_UNUSE").defaultText("INVALID_USER_COMPANY_UNUSE").build());
		                    return "error";
	                	}
	                	if( "4".equals(companyState)){  //欠费
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_NOPLAY_OFF").defaultText("INVALID_USER_COMPANY_NOPLAY_OFF").build());
	                		return "error";
	                	}
	                	if("1".equals(companyState) ){   //登记开发期
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_STATE_BEFORE").defaultText("INVALID_USER_COMPANY_STATE_BEFORE").build());
	                		return "error";
	                	}
	                }
        		 Date loginTime=new Date(System.currentTimeMillis());
        		 user.setLastLoginTime(loginTime);
        		 this.userService.updateUser(user); ///更新登录时间

        		 return "success";
	    	   }else{  //未找到用户
	                messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_NOT_FOUND").defaultText("INVALID_USER_NOT_FOUND").build());
	                return "error";
	    	   }   
		} else {
        	messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_NOT_FOUND").defaultText("INVALID_USER_NOT_FOUND").build());
            return "error";
        }
	}
	 /**
	  * @功能: 证书登录方式验证  
	  * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午2:07:28
	  */
	public final String certUserCheck(final RequestContext context,final UsernamePasswordCredentials credentials,
			final MessageContext messageContext) throws Exception {
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
	    String  loginModel=  StringUtils.trimToEmpty(request.getParameter("loginModel"));
	    loginModel =loginModel.endsWith("")?"CNGZEPORT":loginModel;
	    BaseUser  userInfo =null;
	    if("gdnetCat".equals( loginModel)){
        	request.getSession().removeAttribute("certThumb");
            String sn="1000000060241";
        	  sn=  request.getParameter("certThumbPrint");
        	  
        	  UserCert userCert = this.userService.getUserCert(sn);
			if (userCert == null) {
				 messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
	             return "error";			
			}
			request.getSession().setAttribute("certThumb",userCert.getCertThumbprint());
			String userId = String.valueOf(userCert.getUserid());
			if (userId == null || "".equals(userId)) {
				messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
	             return "error";	
			}
			 userInfo = this.userService.getUserByUserId(userId);
        }	
	    
	    if("netCat".equals(loginModel)){
	    	request.getSession().removeAttribute("certThumb");
	    	String argStr=  request.getParameter("randomStr");
	    	String argSign=  request.getParameter("procContent");
	        SimplePKCS7SignedData sd=new SimplePKCS7SignedData(new ByteArrayInputStream(Base64.decode(new String(argSign))));//װ��SimplePKCS7SignedData���������֤
	        X509Certificate certs[]=null;
	        String sn =null;
	        try {// ������֤��Ϣ
				if (sd.verify()) {
					byte b[] = sd.getContent();
					String signData = new String(b, "UTF-16LE");
					if (signData.equals(argStr)) {
						certs = sd.getSignCertificates();
						CertInfo certInfo = new CertInfo();
						certInfo.setCert(certs[0]);
						sn = certInfo.getThumbprint();
						UserCert userCert = this.userService.getUserCert(sn);
						if (userCert == null) {
						messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
				             return "error";
						}
						request.getSession().setAttribute("certThumb",userCert.getCertThumbprint());
						String userId = String.valueOf(userCert.getUserid());
						if (userId == null||"".equals(userId)) {
							messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
				             return "error";
						}
						  userInfo = this.userService.getUserByUserId(userId);
					} else {
					}
				}
				} catch (Exception e) {
				}
	    }
	    if("CNGZEPORT".equals(loginModel)){
	   		 String sn =StringUtils.trimToEmpty(request.getParameter("certThumbPrint"));
	   		 List userInf =   getAllStringBySplit(new StringBuffer(sn), "||") ;
	   		 String caCode="";
	   		 for(int i=0;i<userInf.size();i++){
	   			 if(i==5){
	   			 caCode=String.valueOf(userInf.get(i));
	   			 }
	   		 }
			if ("".equals(caCode)) {
				messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
		             return "error";
			}
	   		UserCert userCert = this.userService.getUserCert( caCode);
			if (userCert == null) {
				messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
	             return "error";
			}
			request.getSession().setAttribute("certThumb",userCert.getCertThumbprint());
			String userId = String.valueOf(userCert.getUserid());
			if (userId == null && "".equals(userId)) {
				messageContext.addMessage(new MessageBuilder().error().code("INVAILD_USER_CERT_ERROR").defaultText("INVAILD_USER_CERT_ERROR").build());
	             return "error";
			}
			 userInfo = this.userService.getUserByUserId(userId);
	    }
	    if(userInfo==null){
       	 messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_NOT_FOUND").defaultText("INVALID_USER_NOT_FOUND").build());
            return "error";
       }
	   if (!"".equals( StringUtils.trimToEmpty(userInfo.getUserCode()))) {
	        BaseUser user=	this.userService.findUserByUserCode(userInfo.getUserCode());
	        if (user != null) {
	                String isUse = user.getIsUse();
	                if ("0".equals(isUse)) {  //未启用�
	                    messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_UNUSE").defaultText("INVALID_USER_UNUSE").build());
	                    return "error";
	                }
	                if (user.getBaseCompany()==null) { // 公司未注册
	                	messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_UNCONNCOMPANY").defaultText("INVALID_USER_UNCONNCOMPANY").build());
	                    return "error";
	                }else{   
	                	BaseCompany company =user.getBaseCompany();
	                	String  companyState=StringUtils.trimToEmpty(String.valueOf(company.getCompanyState()));
	                	if( "0" .equals(companyState) ){  //公司禁用
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_UNUSE").defaultText("INVALID_USER_COMPANY_UNUSE").build());
		                    return "error";
	                	}
	                	if( "4".equals(companyState)){  //欠费
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_NOPLAY_OFF").defaultText("INVALID_USER_COMPANY_NOPLAY_OFF").build());
	                		return "error";
	                	}
	                	if("1".equals(companyState) ){   //登记开发期 
	                		messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_COMPANY_STATE_BEFORE").defaultText("INVALID_USER_COMPANY_STATE_BEFORE").build());
	                		return "error";
	                	}
	                }
       		 Date loginTime=new Date(System.currentTimeMillis());
       		 user.setLastLoginTime(loginTime);
       		 this.userService.updateUser(user); //更新最后登录时间
       		 return "success";
	        }else{     //未找到用户
	        	messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_NOT_FOUND").defaultText("INVALID_USER_NOT_FOUND").build());
	             return "error";
	         }
	    }else {
	       	messageContext.addMessage(new MessageBuilder().error().code("INVALID_USER_NOT_FOUND").defaultText("INVALID_USER_NOT_FOUND").build());
	        return "error";
	       }
	}
	
	 private void populateErrorsInstance(final TicketException e, final MessageContext messageContext) {

	        try {
	            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());
	        } catch (final Exception fe) {
	            logger.error(fe.getMessage(), fe);
	        }
	    }


    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public static List getAllStringBySplit(StringBuffer strBuffer,
			String delimiter, boolean bool) {
		String str = strBuffer.toString();
		List result = new ArrayList();
		if (str == null)
			return null;
		if (delimiter == null) {
			result.add(str);
			return result;
		}
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++)
				if (bool)
					result.add((new StringBuilder(String.valueOf(str.substring(i, i + 1)))).append(delimiter).toString());
				else
					result.add(str.substring(i, i + 1));
		} else {
			int pos = 0;
			int delPos = 0;
			int m = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				if (bool)
					result.add((new StringBuilder(String.valueOf(str.substring(pos, delPos)))).append(delimiter).toString());
				else
					result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()
					&& !"".equals(StringUtils.trimToEmpty(str.substring(pos).trim())))
				if (bool)
					result.add((new StringBuilder(String.valueOf(str.substring(pos).trim()))).append(delimiter).toString());
				else
					result.add(str.substring(pos).trim());
		}
		return result;
	}

	public static List getAllStringBySplit(StringBuffer strBuffer,
			String delimiter) {
		return getAllStringBySplit(strBuffer, delimiter, false);
	}
	
}

