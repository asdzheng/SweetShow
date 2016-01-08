package com.asdzheng.sweetshow.http;

import com.asdzheng.sweetshow.bean.BaseResponse;

/**
 * Created by asdzheng on 2016/1/8.
 */
interface  HttpResponse {

    public void responseSuccese(String taskid, BaseResponse resp);

    public void responseError(String taskid, BaseResponse resp);

}
