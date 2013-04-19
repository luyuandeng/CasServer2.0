package com.gzeport.app.casserver.service;

import java.util.List;

import com.gzeport.app.casserver.pojo.BaseFuncmodel;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.pojo.UserCert;

public interface UserService {

	public BaseUser findUserByUserCode(String username);

	public void updateUser(BaseUser user);

	public UserCert getUserCert(String sn);

	public BaseUser getUserByUserId(String userId);

	public List<BaseFuncmodel> getFuncModelsList(  String userId);

}
