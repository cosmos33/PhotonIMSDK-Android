package com.cosmos.photonim.imbase.utils.http.jsons;

public class JsonResult<T extends JsonRequestResult> {
    private T result;
    private int httpErrorCode;

    public JsonResult(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public void set(T t) {
        result = t;
    }

    public T get() {
        return result;
    }

    public boolean success() {
        return httpErrorCode == 0 && result.success();
    }
}
