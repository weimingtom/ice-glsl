package com.ice.util;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: jason
 * Date: 13-2-5
 */
public class Asset {

    public static String asser2Sting(AssetManager assets, String assetFile) {
        BufferedReader reader = null;

        try {
            StringBuilder sb = new StringBuilder();

            InputStream in = assets.open(assetFile);
            reader = new BufferedReader(new InputStreamReader(in));

            String line = reader.readLine();

            while (line != null) {
                sb.append(line).append('\n');
                line = reader.readLine();
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
