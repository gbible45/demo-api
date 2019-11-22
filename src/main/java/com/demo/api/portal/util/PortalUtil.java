package com.demo.api.portal.util;

import com.demo.api.portal.users.domain.PortalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class PortalUtil {

    private static String portalApiPrefix;

    @Autowired
    public void setPortalApiPrefix(@Value("${portal.api.prefix:/api/portal}") String portalApiPrefix) {
        PortalUtil.portalApiPrefix = portalApiPrefix;
    }

    public static String getPortalApiPrefix() {
        return portalApiPrefix;
    }

    private static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        PortalUtil.dataSource = dataSource;
    }

    public PortalUtil() {}

    private static URL getTargetURL(String target) throws MalformedURLException {
        return URI.create(target).toURL();
    }

    public static List<PortalUser> selectUserEmailsByLikeEmail(String likeEmail) throws SQLException {
        List<PortalUser> portalUsers = new ArrayList();
	    Connection connection = null;
	    Statement statement = null;
        try {
	        connection = dataSource.getConnection();
	        statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, email FROM portal_user WHERE email like '" + likeEmail + "%'");
            while (resultSet.next()) {
                PortalUser portalUser = new PortalUser(resultSet.getString("email"));
                portalUser.setIdForClient(Long.valueOf(String.valueOf(resultSet.getString("id"))));
                portalUsers.add(portalUser);
            }
        } catch (SQLException se) {
	        throw se;
        } finally {
	        databaseClose(connection, statement);
        }
        return portalUsers;
    }

    public static void destroyUserData(PortalUser portalUser) throws SQLException {
	    Connection connection = null;
	    Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("delete from portal_user_roles where portal_user_id = " + portalUser.getId());
        } catch (SQLException se) {
	        throw se;
        } finally {
	        databaseClose(connection, statement);
        }
    }

    public static void destroyUserAllData(PortalUser portalUser) throws SQLException {
	    Connection connection = null;
	    Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("delete from portal_user_roles where portal_user_id = " + portalUser.getId());
            statement.executeUpdate("delete from portal_user where id = " + portalUser.getId());
        } catch (SQLException se) {
	        throw se;
        } finally {
	        databaseClose(connection, statement);
        }
    }

	public static void databaseClose(Connection connection, Statement statement) throws SQLException {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException se) {
			throw se;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

    public static String encodeToBase64(Object src) {
        return Base64.getMimeEncoder(76, "\r\n".getBytes()).encodeToString(src.toString().getBytes());
    }
}