
package com.assignment.movietime.network;

import retrofit2.Response;

/**
 * Created by Rashmi on 18/08/17.
 */

public interface NetworkCallback<T> {

    void onSuccess(T response);
    void onFailure(Throwable throwable, int failureCode);
}
