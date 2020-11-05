package com.cosmos.photonim.imbase.utils.http;

import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRequestResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;

import java.util.Map;

public interface IHttpHelper {
    int SUCCESS = 0;
    int ERROR_IO = 1;
    int ERROR_JSON = 2;

    <T extends JsonRequestResult> JsonResult post(String url, Map<String, String> formBodyMap, Class<T> jsonClass);

    <T extends JsonRequestResult> JsonResult post(String url, String picPath, Map<String, String> formBodyMap, Class<T> jsonClass);

    <T extends JsonRequestResult> JsonResult post(String url, Map<String, String> formBodyMap, Map<String, String> headerMap, Class<T> jsonClass);

    <T extends JsonRequestResult> JsonResult post(String url, String picPath, Map<String, String> formBodyMap, Map<String, String> headerMap, Class<T> jsonClass);

    Object getFile(String fileUrl, String saveFileUrl, IChatModel.OnGetFileListener onGetFileListener);
}
