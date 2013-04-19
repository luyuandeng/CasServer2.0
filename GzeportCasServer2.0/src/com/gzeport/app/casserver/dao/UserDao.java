package com.gzeport.app.casserver.dao;

import java.util.List;

import com.gzeport.app.casserver.pojo.BaseFuncmodel;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.pojo.UserCert;

public interface UserDao    {

	public abstract BaseUser findUserByUserCode(String username);

	public abstract void updateUser(BaseUser user);

	public abstract UserCert getUserCert(String sn);

	public abstract BaseUser getUserByUserId(String userId);

	public abstract List<BaseFuncmodel> getFuncModelsList(String userId);

}
