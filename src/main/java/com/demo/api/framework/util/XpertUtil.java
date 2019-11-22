package com.demo.api.framework.util;

import com.demo.api.framework.domain.AbstractUser;
import com.demo.api.framework.domain.VersionedEntity;
import com.demo.api.framework.exception.MultiErrorException;
import com.demo.api.framework.exception.VersionException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class XpertUtil {

    private static ApplicationContext applicationContext;
    protected static MessageSource messageSource;

    @Autowired
    public XpertUtil(ApplicationContext applicationContext,
                     MessageSource messageSource) {

        XpertUtil.applicationContext = applicationContext;
        XpertUtil.messageSource = messageSource;
    }


    /**
     * Gets the reference to an application-context bean
     *
     * @param clazz	the type of the bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }


    /**
     * Gets a message from messages.properties
     *
     * @param messageKey	the key of the message
     * @param args			any arguments
     */
    public static String getMessage(String messageKey, Object... args) {

        // http://stackoverflow.com/questions/10792551/how-to-obtain-a-current-user-locale-from-spring-without-passing-it-as-a-paramete
        return messageSource.getMessage(messageKey, args,
                LocaleContextHolder.getLocale());
    }

    /**
     * Constructs a map of the key-value pairs,
     * passed as parameters
     *
     * @param keyValPair
     */
    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,V> mapOf(Object... keyValPair) {

        if(keyValPair.length % 2 != 0)
            throw new IllegalArgumentException("Keys and values must be in pairs");

        Map<K,V> map = new HashMap<K,V>(keyValPair.length / 2);

        for(int i = 0; i < keyValPair.length; i += 2){
            map.put((K) keyValPair[i], (V) keyValPair[i+1]);
        }

        return map;
    }


    /**
     * Creates a MultiErrorException out of the given parameters
     *
     * @param valid			the condition to check for
     * @param messageKey	key of the error message
     * @param args			any message arguments
     */
    public static MultiErrorException check(
            boolean valid, String messageKey, Object... args) {

        return XpertUtil.check(null, valid, messageKey, args);
    }


    /**
     * Creates a MultiErrorException out of the given parameters
     *
     * @param fieldName		the name of the field related to the error
     * @param valid			the condition to check for
     * @param messageKey	key of the error message
     * @param args			any message arguments
     */
    public static MultiErrorException check(
            String fieldName, boolean valid, String messageKey, Object... args) {

        return new MultiErrorException().check(fieldName, valid, messageKey, args);
    }


    /**
     * A convenient method for running code
     * after successful database commit.
     *
     * @param runnable
     */
    public static void afterCommit(Runnable runnable) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        runnable.run();
                    }
                });
    }

    /**
     * Gets the current-user
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    U getUser() {

        // get the authentication object
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();

        // get the user from the authentication object
        return getUser(auth);
    }

    /**
     * Gets user by U
     * [hyo] accessToken
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    U getUser(U user) {

        user.decorate(user); // decorate self
        Authentication auth = // make the authentication object
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // get the user from the authentication object
        return getUser(auth);
    }


    /**
     * Extracts the current-user from authentication object
     *
     * @param auth
     * @return
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    U getUser(Authentication auth) {

        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof AbstractUser<?,?>) {
                return (U) principal;
            }
        }
        return null;
    }

    /**
     * Gets the current-user
     */
    public static Object getCredentials() {

        // get the authentication object
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null) {
            return auth.getCredentials();
        } else {
            return null;
        }
    }

    /**
     * Signs a user in
     *
     * @param user
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    void logIn(U user) {
        Object credentials = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        }
        user.decorate(user); // decorate self
        Authentication authentication = // make the authentication object
                new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
    }


    /**
     * Signs a user in
     *
     * @param user
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    void logIn(U user, Object credentials) {
        user.decorate(user); // decorate self
        Authentication authentication = // make the authentication object
                new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
    }


    public static <U extends AbstractUser<U,ID>, ID extends Serializable> void setAuthorities(Object credentials) {
        U user = getUser();
        Authentication authentication = // make the authentication object
                new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // put that in the security context
    }

    /**
     * Signs a user out
     */
    public static void logOut() {
        SecurityContextHolder.getContext().setAuthentication(null); // set the authentication to null
    }


    /**
     * Throws a VersionException if the versions of the
     * given entities aren't same.
     *
     * @param original
     * @param updated
     */
    public static <U extends AbstractUser<U,ID>, ID extends Serializable>
    void validateVersion(VersionedEntity<U,ID> original, VersionedEntity<U,ID> updated) {

        if (original.getVersion() != updated.getVersion())
            throw new VersionException(original.getClass().getSimpleName());
    }


    public static boolean isInitializedJpaEntity(Object entity) {
        return entity != null && Hibernate.isInitialized(entity);
        //HibernateProxy proxy = HibernateProxy.class.cast(entity);
        //return !proxy.getHibernateLazyInitializer().isUninitialized();
    }

}
