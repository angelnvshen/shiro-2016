package own.stu.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import own.stu.shiro.dao.UserDao;
import own.stu.shiro.model.User;
import own.stu.shiro.util.DbUtil;

import java.sql.Connection;

/**
 * Created by dell on 2016/9/20.
 */
public class MyRealm extends AuthorizingRealm {

    private UserDao userDao=new UserDao();
    private DbUtil dbUtil=new DbUtil();
    /**
     * 为当前登录的用户授予角色和权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userName = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Connection con = null;
        try {
            con = dbUtil.getCon();
            info.setRoles(userDao.getRoles(con, userName));
            info.setStringPermissions(userDao.getPermissions(con, userName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return info;
    }

    /**
     * 验证当前登录的用户
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();

        Connection con = null;

        try {
            con = dbUtil.getCon();
            User user = userDao.getByUserName(con, userName);
            if(user != null){
                AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), "xxx");
                return info;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
