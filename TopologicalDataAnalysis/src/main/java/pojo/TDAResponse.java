package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class TDAResponse {
    private List<TDAOneFileResponse> tdaResponse;

    public TDAResponse() {
        this.tdaResponse = new ArrayList<>();
    }

    public TDAResponse(TDAOneFileResponse oneFileResponse) {
        this();
        addElement(oneFileResponse);
    }

    public void addElement(TDAOneFileResponse fileResponse) {
        tdaResponse.add(fileResponse);
    }

    public TDAOneFileResponse getOnlyTDAResponse() {
        if (tdaResponse.isEmpty()) {
            throw new NullPointerException("Список результатов пуст");
        }
        return tdaResponse.get(0);
    }

    @JsonGetter("tdaResponse")
    public List<TDAOneFileResponse> getTdaResponse() {
        return tdaResponse;
    }
}
