package com.mty.youtubeapiactivity.base

import com.mty.youtubeapiactivity.result.Resource
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()
                if (body!=null) return Resource.success(body)
            } else {
                return Resource.error(response.message(), response.body())
            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
        return Resource.error(null, null)
    }
}