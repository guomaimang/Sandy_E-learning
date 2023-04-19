package tech.hirsun.project.comp3334.sandy_elearning.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int resultCode;
    private String message;
    private T data;

    public Result() {
    }
    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
