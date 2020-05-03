package pojo;

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

    public List<TDAOneFileResponse> getTdaResponse() {
        return tdaResponse;
    }
}
