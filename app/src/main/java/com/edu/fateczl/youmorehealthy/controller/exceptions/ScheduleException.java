package com.edu.fateczl.youmorehealthy.controller.exceptions;

import com.edu.fateczl.youmorehealthy.R;

public class ScheduleException extends RuntimeException {

    private final int msgId;

    public ScheduleException(){
        msgId = R.string.schedule_exception;
    }

    public int getMsgId() {
        return msgId;
    }
}
