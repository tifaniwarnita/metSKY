package com.tifaniwarnita.metsky.controllers;

import android.location.Location;
import android.util.Xml;

import com.tifaniwarnita.metsky.models.Cuaca;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tifani on 3/12/2016.
 */
public class WeatherMeteoXmlParser {

    private static final String ns = null;

    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List daftarCuaca = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("weather")) {
                daftarCuaca.add(readWeather(parser));
            } else {
                skip(parser);
            }
        }
        return daftarCuaca;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private static Cuaca readWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "weather");
        String kota = null;
        String level = null;
        String latitude = null;
        String longitude = null;
        String cuaca = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("kota")) {
                kota = readKota(parser);
            } else if (name.equals("level")) {
                level = readLevel(parser);
            } else if (name.equals("cuaca")) {
                cuaca = readCuaca(parser);
            } else if (name.equals("lat")) {
                latitude = readLatitude(parser);
            } else if (name.equals("lon")) {
                longitude = readLongitude(parser);
            } else {
                skip(parser);
            }
        }
        Location lokasi = new Location("");
        lokasi.setLatitude(Double.parseDouble(latitude));
        lokasi.setLongitude(Double.parseDouble(longitude));
        return new Cuaca(kota, level, cuaca, lokasi);
    }

    // Processes kota tags in the feed.
    private static String readKota(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kota");
        String kota = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kota");
        return kota;
    }

    // Processes level tags in the feed.
    private static String readLevel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "level");
        String level = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "level");
        return level;
    }

    // Processes latitude tags in the feed.
    private static String readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "lat");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lat");
        return latitude;
    }

    // Processes longitude tags in the feed.
    private static String readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "lon");
        String longitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "lon");
        return longitude;
    }

    // Processes longitude tags in the feed.
    private static String readCuaca(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "cuaca");
        String longitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cuaca");
        return longitude;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
