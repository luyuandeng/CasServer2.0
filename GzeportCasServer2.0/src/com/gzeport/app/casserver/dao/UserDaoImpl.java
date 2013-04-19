package com.gzeport.app.casserver.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.gzeport.app.casserver.pojo.BaseFuncmodel;
import com.gzeport.app.casserver.pojo.BaseUser;
import com.gzeport.app.casserver.pojo.UserCert;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao  {

	 protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * @功能: 查找用户根据用户登录帐号 
	 * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午4:14:17
	 */
	@Override
	public BaseUser findUserByUserCode(String username) {
		if(username!=null)
			username =username.toUpperCase();
		String hql ="from BaseUser u  where u.userCode =? ";
		List<BaseUser> listUser =this.getHibernateTemplate().find(hql,username) ;
		if(listUser!=null&&listUser.size()>0){
			BaseUser u =listUser.get(0);
			return u;
		}
		return null;
	}
	/**
	 * @功能: 更新用户 
	 * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午4:14:04
	 */
	@Override
	public void updateUser(BaseUser user) {
		this.getHibernateTemplate().update(user);
	}
	/**
	 * @功能: 证书登录方式取证书备案表信息 
	 * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午4:13:31
	 */
	@Override
	public UserCert getUserCert(String sn) {
		String hql ="from UserCert userCert where userCert.certThumbPrint=?";
		List<UserCert> userList= this.getHibernateTemplate().find(hql, sn);
		if(userList!=null&&userList.size()>0){
			UserCert uc =userList.get(0);
			return uc;
		}
		 return null;
	}
	 /**
	  * @功能: 通过用户ID加载用户信息 
	  * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午4:13:06
	  */
	@Override
	public BaseUser getUserByUserId(String userId) {
		if(userId!=null&&!"".equals(userId)){
			return this.getHibernateTemplate().load(BaseUser.class, Long.valueOf(userId));	
		}
		return null;
	}
	/**
	 * @功能:获取权限菜单列表 
	 * @编码: luyd luyuandeng@gzeport.com 2013-1-8 下午4:12:45
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseFuncmodel> getFuncModelsList(final String userId) {
		
		List<BaseFuncmodel> listmodels = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " select distinct tb.*  from portal.T_BASE_FUNCMODEL tb,  (select r.FUNC_ID FUNC_ID, f.FUNC_CODE FUNC_CODE"
						+ " from portal.T_ROLE_FUNCMODEL r, portal.T_BASE_FUNCMODEL f	 where 1=1 and  (r.ROLE_ID in"
						+ "  (select userrole.ROLE_ID    as ROLE_ID   from portal.T_USER_ROLE userrole, portal.T_BASE_ROLE role"
						+ "   where (role .ROLE_ENABLE = 1 and  userrole.ROLE_ID = role.ROLE_ID)    and (userrole.USER_ID = ?)))  and f.FUNC_ID = r.FUNC_ID) ttb"
						+ "  where tb.FUNC_ID = ttb.FUNC_ID ";
				/*if (!StringUtils.isEmpty(systemType)) {
					sql+="and tb.FUNC_SYS_TYPE = '"+systemType+"' ";
				}*/

				Query query  = session.createSQLQuery(sql).addEntity(BaseFuncmodel.class);
				query.setLong(0,Long.valueOf(userId));
				List list = query.list();
				return list;
			}
		});
		logger.info("用户模块信息："+listmodels.size());
		return listmodels;
	}
	

}
