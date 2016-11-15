package netutils.engine;

public class NetConstants {
	 //-------------------------   网络请求异常码    --------------------------------
    /**
     *  网络请求异常码 ClientProtocolException
     */
    public static final int CLIENT_PROTOCOL_EXCEPTION = 601;
    /**
     *  网络请求异常码 IOException
     */
    public static final int IO_EXCEPTION = 602;
    /**
     *  网络请求异常码 Exception
     */
    public static final int EXEPTION = 603;
    /**
     *  网络请求异常码 ProtocolException
     */
    public static final int PROTOCOL_EXCEPTION = 604;
    /**
     *  网络请求异常码 MalformedURLException
     */
    public static final int MALFORMED_URL_EXCEPTION = 605;
    /**
     *  网络请求异常码UnsupportedEncodingException
     */
    public static final int UNSUPPORTED_ENCODING_EXCEPTION = 606;
    /**
     *  网络请求异常码ParseException
     */
    public static final int PARSE_EXCEPTION = 607;
    /**
     *  无网络连接
     */
    public static final int CONNTION_NULL_EXCEPTION = 608;
    /**
     *  已经下载完成
     */
    public static final int DOWNLOAD_COMPLETE = 416;
    /**
     * 用户主动停止下载异常码
     */
    public static final int USER_STOP_DOWNLOAD_THREAD = 703;
    /**
     * 下载时，文件创建与读写操作时发生的IO异常
     */
    public static final int DOWNLOAD_FILE_IOEXCEPTION = 704;
    /**
     * JSONException
     */
    public static final int JSON_EXCEPTION = 700;
    /**
     *  errorNo=0
     */
    public static final int URL_ERROR = 404;
    /**
     * UnknownHostException
     */
    public static final int UNKNOWN_HOST_EXCEPTION = 609;
    /**
     * ConnectTimeoutException \ SocketTimeoutException
     */
    public static final int TIMEOUT_EXCEPTION = 700;
    /**
     * NullPointerException
     */
    public static final int NULL_POINTER_EXCEPTION = 701;
    /**
     *  无法连接到服务器errorNo=0
     */
    public static final int CONNECTION_ERROR = 0;
    /**
     * 未知网络异常
     */
    public static final int UNKNOWN_NET_ERROR = 702;
}
