package com.example.providerrestdoc;

import lombok.Data;

@Data
public class Response {
    private BeerCheckStatus status;
    public Response(BeerCheckStatus status) {
        this.status = status;
    }
}
