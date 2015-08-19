package de.konqi.fitapi.common;

/**
 * Created by konqi on 19.08.2015.
 */
public class GenericResponse<T> {
    T value;
    boolean success = false;
    String message = "";

    public T getValue() {
        return value;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder<B>  {
        GenericResponse<B> response;

        public Builder(boolean success) {
            response = new GenericResponse<>();
            response.success = success;
        }

        public Builder<B> withValue(B value) {
            response.value = value;
            return this;
        }

        public Builder<B> withMessage(String message) {
            response.message = message;
            return this;
        }

        public GenericResponse<B> build() {
            return response;
        }

    }
}
