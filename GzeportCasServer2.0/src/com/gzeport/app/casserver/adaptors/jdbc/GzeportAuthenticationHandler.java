package com.gzeport.app.casserver.adaptors.jdbc;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.util.StringUtils;

public final class GzeportAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    
    protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials) throws AuthenticationException {
        final String username = getPrincipalNameTransformer().transform(credentials.getUsername());
        final String password = credentials.getPassword();
      //  final String encryptedPassword = this.getPasswordEncoder().encode(password);
        
        log.info("username:"+username+"password:"+password);
        if(StringUtils.hasText( username) &&StringUtils.hasText(password)){
        	  return true;
        }
        return false;
    }
}
