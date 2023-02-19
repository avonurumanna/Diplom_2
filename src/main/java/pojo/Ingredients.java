package pojo;

import java.util.List;

public class Ingredients {

    private List<Data> data;
    private boolean success;

    public Ingredients(List<Data> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
