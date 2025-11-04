//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package webapp.mcpserver.handle;

import io.netty.buffer.ByteBufInputStream;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.CookieImpl;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.ContextAsyncListener;
import org.noear.solon.core.handle.Cookie;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.core.util.IoUtil;
import org.noear.solon.core.util.MultiMap;
import org.noear.solon.server.ServerProps;
import org.noear.solon.server.handle.AsyncContextState;
import org.noear.solon.server.handle.ContextBase;
import org.noear.solon.server.util.DecodeUtils;
import org.noear.solon.server.util.RedirectUtils;
import org.noear.solon.web.vertx.RequestInputStream;
import org.noear.solon.web.vertx.ResponseOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class QuarkusContext extends ContextBase {
    static final Logger log = LoggerFactory.getLogger(QuarkusContext.class);
    private HttpServerRequest _request;
    private HttpServerResponse _response;
    private Buffer _requestBody;
    private boolean _loadMultipartFormData = false;
    private URI _uri;
    private String _url;
    private long contentLength = -2L;
    private InputStream bodyAsStream;
    private MultiMap<String> _paramMap;
    private MultiMap<String> _cookieMap;
    private MultiMap<String> _headerMap;
    private ResponseOutputStream responseOutputStream;
    private ByteArrayOutputStream _outputStreamTmp;
    private int _status = 200;
    private boolean _headers_sent = false;
    private boolean _allows_write = true;
    protected final AsyncContextState asyncState = new AsyncContextState();

    protected HttpServerRequest innerGetRequest() {
        return this._request;
    }

    public HttpServerResponse innerGetResponse() {
        return this._response;
    }

    public QuarkusContext(HttpServerRequest request, Buffer requestBody) {
        this._request = request;
        this._requestBody = requestBody;
        this._response = request.response();
    }

    private void loadMultipartFormData() {
        if (!this._loadMultipartFormData) {
            this._loadMultipartFormData = true;
            if (this.isMultipartFormData()) {
                DecodeUtils.decodeMultipart(this, new ByteBufInputStream(this._requestBody.getByteBuf()), this._fileMap);
            }

        }
    }

    public boolean isHeadersSent() {
        return this._headers_sent;
    }

    public Object request() {
        return _request;
    }

    public String remoteIp() {
        return _request.remoteAddress().host();
    }

    public int remotePort() {
        return _request.remoteAddress().port();
    }

    public int localPort() {
        return _request.localAddress().port();
    }

    public String method() {
        return _request.method().name();
    }

    public String protocol() {
        return "http";
    }

    public URI uri() {
        if (this._uri == null) {
            this._uri = this.parseURI(this.url());
        }

        return this._uri;
    }

    public boolean isSecure() {
        return false;
    }

    public String url() {
        if (this._url == null) {
            String tmp = _request.absoluteURI();
            int idx = tmp.indexOf(63);
            if (idx < 0) {
                this._url = tmp;
            } else {
                this._url = tmp.substring(0, idx);
            }
        }

        return this._url;
    }

    public long contentLength() {
        if (this.contentLength < -1L) {
            this.contentLength = DecodeUtils.decodeContentLengthLong(this);
        }

        return this.contentLength;
    }

    public String queryString() {
        return _request.query();
    }

    public InputStream bodyAsStream() throws IOException {
        if (this.bodyAsStream != null) {
            return this.bodyAsStream;
        } else {
            if (_requestBody == null) {
                this.bodyAsStream = new ByteArrayInputStream(new byte[0]);
            } else {
                this.bodyAsStream = new RequestInputStream(_requestBody.getByteBuf(), ServerProps.request_maxBodySize);
            }

            return this.bodyAsStream;
        }
    }

    public String body(String charset) throws IOException {
        try {
            return super.body(charset);
        } catch (Exception var3) {
            Exception e = var3;
            throw DecodeUtils.status4xx(this, e);
        }
    }

    public MultiMap<String> paramMap() {
        this.paramsMapInit();
        return this._paramMap;
    }

    private void paramsMapInit() {
        if (this._paramMap == null) {
            this._paramMap = new MultiMap();

            try {
                DecodeUtils.decodeFormUrlencoded(this, false);
                if (this.autoMultipart()) {
                    this.loadMultipartFormData();
                }

                Iterator var4 = _request.params().iterator();

                Map.Entry<String,String> kv;
                while(var4.hasNext()) {
                    kv = (Map.Entry)var4.next();
                    this._paramMap.add((String)kv.getKey(), (String) kv.getValue());
                }

                var4 = _request.formAttributes().iterator();

                while(var4.hasNext()) {
                    kv = (Map.Entry)var4.next();
                    this._paramMap.add((String)kv.getKey(), (String) kv.getValue());
                }
            } catch (Exception var3) {
                Exception e = var3;
                throw DecodeUtils.status4xx(this, e);
            }
        }

    }

    public MultiMap<UploadedFile> fileMap() {
        if (this.isMultipartFormData()) {
            this.loadMultipartFormData();
        }

        return this._fileMap;
    }

    public MultiMap<String> cookieMap() {
        if (this._cookieMap == null) {
            this._cookieMap = new MultiMap(false);
            DecodeUtils.decodeCookies(this, this.header("Cookie"));
        }

        return this._cookieMap;
    }

    public MultiMap<String> headerMap() {
        if (this._headerMap == null) {
            this._headerMap = new MultiMap();
            Iterator var1 = _request.headers().iterator();

            while(var1.hasNext()) {
                Map.Entry<String, String> kv = (Map.Entry)var1.next();
                this._headerMap.add((String)kv.getKey(), kv.getValue());
            }
        }

        return this._headerMap;
    }

    public Object response() {
        return this._response;
    }

    protected void contentTypeDoSet(String contentType) {
        if (this.charset != null && contentType != null && contentType.length() > 0 && contentType.indexOf(";") < 0) {
            this.headerSet("Content-Type", contentType + ";charset=" + this.charset);
        } else {
            this.headerSet("Content-Type", contentType);
        }
    }

    private ResponseOutputStream responseOutputStream() {
        if (this.responseOutputStream == null) {
            this.responseOutputStream = new ResponseOutputStream(this._response, 512);
        }

        return this.responseOutputStream;
    }

    public OutputStream outputStream() throws IOException {
        this.sendHeaders(false);
        if (this._allows_write) {
            return this.responseOutputStream();
        } else {
            if (this._outputStreamTmp == null) {
                this._outputStreamTmp = new ByteArrayOutputStream();
            } else {
                this._outputStreamTmp.reset();
            }

            return this._outputStreamTmp;
        }
    }

    public void output(byte[] bytes) {
        try {
            OutputStream out = this.outputStream();
            if (this._allows_write) {
                out.write(bytes);
            }
        } catch (Throwable var3) {
            Throwable ex = var3;
            throw new RuntimeException(ex);
        }
    }

    public void output(InputStream stream) {
        try {
            OutputStream out = this.outputStream();
            if (this._allows_write) {
                IoUtil.transferTo(stream, out);
            }
        } catch (Throwable var3) {
            Throwable ex = var3;
            throw new RuntimeException(ex);
        }
    }

    public void headerSet(String name, String val) {
        this._response.headers().set(name, val);
    }

    public void headerAdd(String name, String val) {
        this._response.headers().add(name, val);
    }

    public String headerOfResponse(String name) {
        return this._response.headers().get(name);
    }

    public Collection<String> headerValuesOfResponse(String name) {
        return this._response.headers().getAll(name);
    }

    public Collection<String> headerNamesOfResponse() {
        return this._response.headers().names();
    }

    public void cookieSet(Cookie cookie) {
        CookieImpl c = new CookieImpl(cookie.name, cookie.value);
        if (cookie.maxAge >= 0) {
            c.setMaxAge((long)cookie.maxAge);
        }

        if (Utils.isNotEmpty(cookie.domain)) {
            c.setDomain(cookie.domain);
        }

        if (Utils.isNotEmpty(cookie.path)) {
            c.setPath(cookie.path);
        }

        c.setSecure(cookie.secure);
        c.setHttpOnly(cookie.httpOnly);
        this._response.addCookie(c);
    }

    public void redirect(String url, int code) {
        url = RedirectUtils.getRedirectPath(url);
        this.headerSet("Location", url);
        this.statusDoSet(code);
    }

    public int status() {
        return this._status;
    }

    protected void statusDoSet(int status) {
        this._status = status;
    }

    public void contentLength(long size) {
        if (!this._headers_sent) {
            this._response.putHeader("Content-Length", String.valueOf(size));
        }

    }

    public void flush() throws IOException {
        if (this._allows_write) {
            this.outputStream().flush();
        }

    }

    public void close() throws IOException {
        this._response.close();
    }

    public void innerCommit() throws IOException {
        try {
            if (!this.getHandled() && this.status() < 200) {
                this.status(404);
                this.sendHeaders(true);
                this.flush();
                this._response.send();
            } else {
                this.sendHeaders(true);
                this.flush();
                this._response.send();
            }
        } finally {
            if (!this._response.ended()) {
                this._response.end();
            }

        }

    }

    private void sendHeaders(boolean isCommit) throws IOException {
        if (!this._headers_sent) {
            this._headers_sent = true;
            if ("HEAD".equals(this.method())) {
                this._allows_write = false;
            }

            if (this.sessionState() != null) {
                this.sessionState().sessionPublish();
            }

            this._response.setStatusCode(this.status());
            if (!isCommit && this._allows_write) {
                if (!this._response.headers().contains("Content-Length")) {
                    this._response.setChunked(true);
                }
            } else {
                this._response.setChunked(true);
            }
        }

    }

    public boolean asyncSupported() {
        return true;
    }

    public boolean asyncStarted() {
        return this.asyncState.isStarted;
    }

    public void asyncListener(ContextAsyncListener listener) {
        this.asyncState.addListener(listener);
    }

    public void asyncStart(long timeout, Runnable runnable) {
        if (!this.asyncState.isStarted) {
            this.asyncState.isStarted = true;
            this.asyncState.asyncDelay(timeout, this, this::innerCommit);
            if (runnable != null) {
                runnable.run();
            }

            this.asyncState.onStart(this);
        }

    }

    public void asyncComplete() {
        if (this.asyncState.isStarted) {
            try {
                this.innerCommit();
            } catch (Throwable var5) {
                Throwable e = var5;
                log.warn("Async completion failed", e);
                this.asyncState.onError(this, e);
            } finally {
                this.asyncState.onComplete(this);
            }
        }

    }
}
