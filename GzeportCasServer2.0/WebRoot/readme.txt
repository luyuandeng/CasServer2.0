CAS取消https验证




服务器端：
 
1.找到cas\WEB-INF\spring-configuration\ticketGrantingTicketCookieGenerator.xml，将 p:cookieSecure="true" 改为 p:cookieSecure="false" 如下：
 
<bean id="ticketGrantingTicketCookieGenerator" class="org.jasig.cas.web.support.CookieRetrievingCookieGenerator"
        p:cookieSecure="false"
        p:cookieMaxAge="-1"
        p:cookieName="CASTGC"
        p:cookiePath="/cas" /> 
2.找到cas\WEB-INF\spring-configuration\warnCookieGenerator.xml ，将 p:cookieSecure="改为 p:cookieSecure="false"，改完如下：
 
<bean id="warnCookieGenerator" class="org.jasig.cas.web.support.CookieRetrievingCookieGenerator"
        p:cookieSecure="false"
        p:cookieMaxAge="-1"
        p:cookieName="CASPRIVACY"
        p:cookiePath="/cas" /> 
3.找到cas\WEB-INF\deployerConfigContext.xml，在文件中找到“HttpBasedServiceCredentialsAuthenticationHandler”bean，然后添加：p:requireSecure="false"，改完如下：
 
<bean class="org.jasig.cas.authentication.handler.support.HttpBasedServiceCredentialsAuthenticationHandler"
                    p:httpClient-ref="httpClient" p:requireSecure="false" />