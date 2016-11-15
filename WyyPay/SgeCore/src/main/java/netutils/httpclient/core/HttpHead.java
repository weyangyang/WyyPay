package netutils.httpclient.core;

/**
 * An HTTP HEAD request.
 * @author liyusheng
 *
 */
public class HttpHead extends HttpMethod {

    /**
     * Constructs an HTTP HEAD request.
     * 
     * @param path Partial URL
     * @param params Name-value pairs to be appended to the URL
     */
    public HttpHead(String path, ParameterList params) {
        super(path, params);
        this.methodType = MethodType.HEAD;
    }

}
