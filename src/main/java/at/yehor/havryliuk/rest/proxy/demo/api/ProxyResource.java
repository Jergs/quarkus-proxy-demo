package at.yehor.havryliuk.rest.proxy.demo.api;

import at.yehor.havryliuk.rest.proxy.demo.service.ProxyService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ProxyResource {

    private final ProxyService proxyService;

    @GET
    public Response proxyMain() throws IOException, URISyntaxException {
        return proxyService.modifyOriginalContent("");
    }

    @GET
    @Path("/{path}/")
    public Response proxyWithPath(@PathParam("path") String path) throws IOException, URISyntaxException {
        return proxyService.modifyOriginalContent(path);
    }

    @GET
    @Path("/{path}/{params:.+}")
    public Response proxyWithPathAndParams(@PathParam("path") String path,
                                           @PathParam("params") String params) throws IOException, URISyntaxException {
        return proxyService.modifyOriginalContent(path + "/" + params);
    }

}
