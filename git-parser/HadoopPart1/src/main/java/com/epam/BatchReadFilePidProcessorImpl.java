package com.epam;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vladyslav_Nikolaienk on 3/17/2016.
 */
public class BatchReadFilePidProcessorImpl implements FilePidProcessor {

    //TODO: some happens
    @Override
    public Map<String, Long> processBigFile(InputStream stream) throws IOException {
        Map<String,Long> pidCounts = new HashMap<>();
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        String raw = writer.toString();

        //TODO: Migradte to java8
        for (String line : raw.split("\\n")) {
            String[] split = line.split("\\s+");
            String key = split[2];
            pidCounts.merge(key, 1l, (a1, a2) -> a1 + a2);
        }
        return pidCounts;
    }
}
