package netutils.http;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;

import android.text.TextUtils;

public final class URIBuilder {
    private String scheme;
//    private String encodedSchemeSpecificPart;
    private String encodedAuthority;
    private String userInfo;
    private String encodedUserInfo;
    private String host;
    private int port;
    private String path;
    private String encodedPath;
    private String encodedQuery;
    private List<NameValuePair> queryParams;
    private String fragment;
    private String encodedFragment;
    public static URI build(String url,Charset charset){
    	URIBuilder uriBuilder = new URIBuilder();
    	URI uri = null;
    	 try {
    		 uriBuilder.digestURI(new URI(url.replaceAll(" ", "%20")));
    		 uri =  new URI(uriBuilder.buildString(charset));
       } catch (URISyntaxException e) {
    	   uri = null;
       }
		return uri;
    	
    }
    private URIBuilder(){};
    private void digestURI(final URI uri) {
    	  this.scheme = uri.getScheme();
//          this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
          this.encodedAuthority = uri.getRawAuthority();
          this.host = uri.getHost();
          this.port = uri.getPort();
          this.encodedUserInfo = uri.getRawUserInfo();
          this.userInfo = uri.getUserInfo();
          this.encodedPath = uri.getRawPath();
          this.path = uri.getPath();
          this.encodedQuery = uri.getRawQuery();
          this.queryParams = parseQuery(uri.getRawQuery());
          this.encodedFragment = uri.getRawFragment();
          this.fragment = uri.getFragment();
    }
    private String buildString(Charset charset) {
        final StringBuilder sb = new StringBuilder();
        if (this.scheme != null) {
            sb.append(this.scheme).append(':');
        }
//        if (this.encodedSchemeSpecificPart != null) {
//            sb.append(this.encodedSchemeSpecificPart);
//        } else {
            if (this.encodedAuthority != null) {
                sb.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                sb.append("//");
                if (this.encodedUserInfo != null) {
                    sb.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    sb.append(encodeUserInfo(this.userInfo, charset)).append("@");
                }
                if (InetAddressUtils.isIPv6Address(this.host)) {
                    sb.append("[").append(this.host).append("]");
                } else {
                    sb.append(this.host);
                }
                if (this.port >= 0) {
                    sb.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                sb.append(normalizePath(this.encodedPath));
            } else if (this.path != null) {
                sb.append(encodePath(normalizePath(this.path), charset));
            }
            if (this.encodedQuery != null) {
                sb.append("?").append(this.encodedQuery);
            } else if (this.queryParams != null) {
                sb.append("?").append(encodeQuery(this.queryParams, charset));
            }
//        }
        if (this.encodedFragment != null) {
            sb.append("#").append(this.encodedFragment);
        } else if (this.fragment != null) {
            sb.append("#").append(encodeFragment(this.fragment, charset));
        }
        return sb.toString();
    }

    private String encodeUserInfo(final String userInfo, Charset charset) {
        return URLEncodedUtils.encUserInfo(userInfo, charset);
    }

    private String encodePath(final String path, Charset charset) {
        return URLEncodedUtils.encPath(path, charset).replace("+", "20%");
    }

    private String encodeQuery(final List<NameValuePair> params, Charset charset) {
        return URLEncodedUtils.format(params, charset);
    }

    private String encodeFragment(final String fragment, Charset charset) {
        return URLEncodedUtils.encFragment(fragment, charset);
    }
    private List<NameValuePair> parseQuery(final String query) {
        if (!TextUtils.isEmpty(query)) {
            return URLEncodedUtils.parse(query);
        }
        return null;
    }
//    private String buildString(Charset charset) {
//        final StringBuilder sb = new StringBuilder();
//        if (this.scheme != null) {
//            sb.append(this.scheme).append(':');
//        }
//        if (this.encodedSchemeSpecificPart != null) {
//        	String strEncoded = encodePath(this.encodedSchemeSpecificPart, charset);
//            sb.append(strEncoded);
//        } 
//        if (this.encodedFragment != null) {
//            sb.append("#").append(this.encodedFragment);
//        } else if (this.fragment != null) {
//            sb.append("#").append(encodeFragment(this.fragment, charset));
//        }
//        return sb.toString();
//    }
//
//    private String encodePath(final String path, Charset charset) {
//        return URLEncodedUtils.encPath(path, charset).replace("+", "20%");
//    }
//
//    private String encodeFragment(final String fragment, Charset charset) {
//        return URLEncodedUtils.encFragment(fragment, charset);
//    }
    private static String normalizePath(String path) {
        if (path == null) {
            return null;
        }
        int n = 0;
        for (; n < path.length(); n++) {
            if (path.charAt(n) != '/') {
                break;
            }
        }
        if (n > 1) {
            path = path.substring(n - 1);
        }
        return path;
    }

}
