package io.km.notes.model;

public class SuccessResponse<T> {

    private T data;

    // Constructors
    public SuccessResponse(T data) {
        this.data = data;
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
