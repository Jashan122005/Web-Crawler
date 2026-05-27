package com.udacity.webcrawler.testing;

import java.io.IOException;
import java.io.StringWriter;

/**
 * A StringWriter that tracks whether it has been closed.
 */
public final class CloseableStringWriter extends StringWriter {

    private boolean closed = false;

    @Override
    public void close() throws IOException {

        if (closed) {

            throw new IOException("stream is closed");
        }

        closed = true;

        super.close();
    }

    /**
     * Returns whether this writer has been closed.
     */
    public boolean isClosed() {

        return closed;
    }
}