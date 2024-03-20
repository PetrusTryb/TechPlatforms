package com.trybisz;



public class Task {
    private final int _id;
    private final int _query;
    public Task(int id, int query) {
        this._id = id;
        this._query = query;
    }
    public int id(){
        return this._id;
    }
    public int query(){
        return this._query;
    }
}