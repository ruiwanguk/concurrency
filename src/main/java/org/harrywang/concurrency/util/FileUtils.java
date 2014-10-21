package org.harrywang.concurrency.util;

import java.io.*;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class FileUtils {


    public static long countLine(File file) throws IOException {

        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }

            return (count == 0 && !empty) ? 1 : count;
        }
    }
}
