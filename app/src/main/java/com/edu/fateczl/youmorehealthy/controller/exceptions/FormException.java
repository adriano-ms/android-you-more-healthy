package com.edu.fateczl.youmorehealthy.controller.exceptions;

import com.edu.fateczl.youmorehealthy.R;

public class FormException extends RuntimeException {

    private final int msgId;

    public FormException(){
        msgId = R.string.form_exception;
    }

    public int getMsgId() {
        return msgId;
    }
}
