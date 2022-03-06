package eskang.popshoplive.controller;

public class HttpResponseDTO<U> {

    private U data;

    public HttpResponseDTO(U data) {
        this.data = data;
    }

    public U getData() {
        return data;
    }
}
