package com.trybisz;

public class TaskResult {
    private final int _id;
    private final int _result;
    public TaskResult(int id, int result){
        this._id = id;
        this._result = result;
    }
    public int id(){
        return this._id;
    }
    public int result(){
        return this._result;
    }
}
