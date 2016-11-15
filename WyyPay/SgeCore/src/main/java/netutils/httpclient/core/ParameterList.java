
package netutils.httpclient.core;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import netutils.httpclient.core.ParameterList.Parameter;

/**
 * Parameter map knows offers convenience methods for chaining add()s as well as
 * URL encoding.
 * @author liyusheng
 *
 */
public class ParameterList extends ArrayList<Parameter> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static abstract class Parameter {
        String name;
    }

    public static class StringParameter extends Parameter {
        String value;

        public StringParameter(String name, String value) {
            super();
            if (name == null) {
                throw new RuntimeException("args can not be null");
            }
            if( value == null){
            	value = "";
            }
            this.name = name;
            this.value = value;
        }

    }

    public static class InputStreamParameter extends Parameter {
        InputStream value;

        String fileName;

        public InputStreamParameter(String name, InputStream value, String fileName) {
            super();
            if (name == null || value == null || fileName == null) {
                throw new RuntimeException("args can not be null");
            }
            this.name = name;
            this.value = value;
            this.fileName = fileName;
        }

    }

    public static class FileParameter extends Parameter {
        File value;

        public FileParameter(String name, File value) {
            super();
            if (name == null || value == null) {
                throw new RuntimeException("args can not be null");
            }
            this.name = name;
            this.value = value;
        }
    }

    public static class HeaderParameter extends Parameter {
        String value;

        public HeaderParameter(String name, String value) {
            super();
            if (name == null || value == null) {
                throw new RuntimeException("args can not be null");
            }
            this.name = name;
            this.value = value;
        }
    }

    /**
     * Returns URL encoded data
     * 
     * @return URL encoded String
     */
    public String urlEncode() {
        StringBuilder sb = new StringBuilder();
        int indexString = 0;
        for (int i = 0; i < size(); i++) {
            Parameter param = get(i);
            if (param instanceof StringParameter) {
                StringParameter stringParam = (StringParameter) param;
                if (indexString > 0) {
                    sb.append("&");
                }
                indexString ++;
                sb.append(stringParam.name);
                sb.append("=");
//                    sb.append(URLEncoder.encode(stringParam.value, RequestHandler.UTF8));
                    sb.append(android.net.Uri.encode(stringParam.value));
            }
        }
        return sb.toString();
    }

    /**
     * Return a URL encoded byte array in UTF-8 charset.
     * 
     * @return URL encoded bytes
     */
    public byte[] urlEncodedBytes() {
        byte[] bytes = null;
        try {
            bytes = urlEncode().getBytes(RequestHandler.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
    public boolean hasMultiPart(){
        for (int i = 0; i < size(); i++) {
            Parameter param = get(i);
            if((param instanceof InputStreamParameter)||(param instanceof FileParameter)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<HeaderParameter> getHeaderParams() {
        ArrayList<HeaderParameter> list = new ArrayList<HeaderParameter>();
        for (int i = 0; i < size(); i++) {
            Parameter param = get(i);
            if (param instanceof HeaderParameter) {
                list.add((HeaderParameter) param);
            }
        }
        return list;
    }

}
