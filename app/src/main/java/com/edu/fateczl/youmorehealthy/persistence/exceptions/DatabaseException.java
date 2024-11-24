package com.edu.fateczl.youmorehealthy.persistence.exceptions;

public class DatabaseException extends RuntimeException {

    private final int msgId;

    public DatabaseException(int msgId){
        this.msgId = msgId;
    }

    public int getMsgId(){
        return msgId;
    }
}
