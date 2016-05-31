package com.epam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Vladyslav_Nikolaienk on 3/17/2016.
 */
public interface FilePidProcessor {

    Map<String, Long> processBigFile(InputStream stream) throws IOException;

    //TODO: ugly method
    default void putPid(Map<String, Long> pidCounts, String line) {
        pidCounts.merge(line.split("\\s")[2], 1L, (a1, a2) -> a1 + a2);
    }

}
