package com.mty.youtubeapiactivity.result

class Resource <T>(val data: T?, val msg: String?, val status: Status) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(data, null, Status.SUCCESS)
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(data, msg, Status.ERROR)
        }

        fun <T> loading(): Resource<T> {
            return Resource(null, null, Status.LOADING)
        }
    }
}