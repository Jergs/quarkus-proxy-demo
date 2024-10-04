package at.yehor.havryliuk.rest.proxy.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RemoteResponseWrapper {

    private byte[] content;
    private String contentType;
}
