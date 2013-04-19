package com.gzeport.app.casserver.service;

import java.util.List;

import com.gzeport.app.casserver.dao.UserDao;
import com.gzeport.app.casserver.pojo.BaseFuncmodel;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.pojo.UserCert;

public class UserServiceImpl implements UserService {
	private UserDao userDao;
	
	/**
	 * @����: ��ѯ�û� 
	 * @����: luyd luyuandeng@gzeport.com 2013-1-7 ����5:54:49
	 */
	@Override
	public BaseUser findUserByUserCode(String username) {
		return this.userDao.findUserByUserCode(  username);
	}
	/**
	 * @����: �����û� 
	 * @����: luyd luyuandeng@gzeport.com 2013-1-7 ����5:55:15
	 */
	@Override
	public void updateUser(BaseUser user) {
		this.userDao.updateUser(user);
	}
	@Override
	public UserCert getUserCert(String sn) {
		return this.userDao.getUserCert(sn);
	}
	@Override
	public BaseUser getUserByUserId(String userId) {
		return this.userDao.getUserByUserId(userId);
	}
	

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Override
	public List<BaseFuncmodel> getFuncModelsList( String userId) {
		return this.userDao.getFuncModelsList(userId );
	}

}
