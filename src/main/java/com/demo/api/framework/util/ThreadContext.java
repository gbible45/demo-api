/*
 * Copyright www.crossent.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of www.crossent.com.,LTD. ("Confidential Information").
 */
package com.demo.api.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    |_ ThreadContext.java
 *  Thread 별 Context를 생성하고 <code>String</code> key를 사용하여 읽고 쓸 수 있는 Utility
 *  class이다.
 * </pre>
 * @date : 2015. 12. 22. 오후 6:14:04
 * @version : 
 * @author : csupshin
 */
@Slf4j
public class ThreadContext {

	private static final ThreadContext THREAD_CONTEXT = new ThreadContext();

    private static final int INITIAL_CAPACITY = 16;

    private static final float LOAD_FACTOR = 0.75f;

    private final InheritableThreadLocal<? extends Map<String, Object>> inheritableThreadLocalMap = new InheritableThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            synchronized (this) {
                return new HashMap<String, Object>(INITIAL_CAPACITY,
                        LOAD_FACTOR);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Map<String, Object> childValue(Map<String, Object> parentValue) {
            if (parentValue == null) {
                return null;
            } else {
                return (Map<String, Object>) ((HashMap<String, Object>) parentValue)
                        .clone();
            }
        }
    };

    /**
     * 현재 Thread의 Contest에서 <code>key</code>로 등록되어 있는 값이 있는지 알아본다.
     *
     * @param key
     *            key.
     * @return 등록되어 있으면 <code>true</code>, 그렇지 않다면 <code>false</code>.
     */
    public static boolean containsKey(String key) {
        return THREAD_CONTEXT.inheritableThreadLocalMap.get().containsKey(key);
    }

    /**
     * 현재 Thread의 Context에서 <code>key</code>로 등록되어 있는 값을 읽어온다.
     *
     * @param key
     *            key.
     * @return 등록되어 있는 값. 없는 경우 <code>null</code>.
     */
    public static Object get(String key) {
        return THREAD_CONTEXT.inheritableThreadLocalMap.get().get(key);
    }

    /**
     * 현재 Thread의 Context에 <code>key</code>로 <code>value</code>등록한다.
     *
     * @param key
     *            key.
     * @param value
     *            등록할 값.
     */
    public static void put(String key, Object value) {
        THREAD_CONTEXT.inheritableThreadLocalMap.get().put(key, value);
    }

    /**
     * 현재 Thread의 Context에서 <code>key</code>로 등록되어 있는 값을 삭제한다.
     *
     * @param key
     *            key.
     * @return 삭제된 값. 없는 경우 <code>null</code>.
     */
    public static Object remove(String key) {
        return THREAD_CONTEXT.inheritableThreadLocalMap.get().remove(key);
    }

    /**
     * 현재 Thread의 Context에 등록되어 있는 모든 값을 삭제한다.
     *
     */
    public static void clear() {
        THREAD_CONTEXT.inheritableThreadLocalMap.get().clear();
    }
    
}
