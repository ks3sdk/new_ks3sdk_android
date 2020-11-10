package com.ks3.demo.main;

import com.ksyun.ks3.model.ObjectMetadata;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Data {
    private File file = null;
    private ObjectMetadata meta = null;
    private InputStream content = null;
    private Closeable closer = null;

    public Data() {
    }

    public void close() {
        if (this.closer != null) {
            try {
                this.closer.close();
                this.closer = null;
            } catch (IOException var2) {
                ;
            }
        }
        if (content != null) {
            try {
                this.content.close();
                this.content = null;
            } catch (IOException var2) {

            }
        }

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public InputStream getContent() {
        return this.content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public ObjectMetadata getMeta() {
        return this.meta;
    }

    public void setMeta(ObjectMetadata meta) {
        this.meta = meta;
    }

    public Closeable getCloser() {
        return this.closer;
    }

    public void setCloser(Closeable closer) {
        this.closer = closer;
    }
}
