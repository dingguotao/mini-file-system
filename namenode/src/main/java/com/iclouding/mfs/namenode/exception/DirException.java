package com.iclouding.mfs.namenode.exception;

import com.iclouding.mfs.common.exception.BaseException;

public class DirException extends BaseException {

    public DirException() {
        super();
    }

    public DirException(String message) {
        super(message);
    }

    public DirException(String message, Throwable cause) {
        super(message, cause);
    }

}
