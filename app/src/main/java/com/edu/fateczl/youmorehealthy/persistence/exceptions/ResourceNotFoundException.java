package com.edu.fateczl.youmorehealthy.persistence.exceptions;

import com.edu.fateczl.youmorehealthy.R;

public class ResourceNotFoundException extends RuntimeException {

    private final int msgId;

    public ResourceNotFoundException(){
        this.msgId = R.string.db_exception_not_found;
    }

    public int getMsgId(){
        return msgId;
    }
}
