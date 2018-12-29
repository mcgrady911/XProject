package com.mcgrady.core.http;

/**
 * <p>类说明</p>
 *
 * @author: mcgrady
 * @date: 2018/6/21
 */

public interface ApiResponse<T> {

    boolean isSuccess();

    boolean checkReLogin();

    T getData();
}
