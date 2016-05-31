package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vladyslav_Nikolaienk on 3/17/2016.
 */
public class BufferedReadFilePidProcessor implements FilePidProcessor {

    //TODO: What about closing stream?
    @Override
    public Map<String, Long> processBigFile(InputStream stream) throws IOException {
        Map<String, Long> pidCounts = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        line = br.readLine();
        while (line != null) {
            putPid(pidCounts, line);
            line = br.readLine();
        }
        return pidCounts;
    }

}
