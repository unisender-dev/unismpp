package com.smpp.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rado on 3/3/15.
 * Based on http://www.smsitaly.com/Download/ETSI_GSM_03.38.pdf
 *
 * http://kb.dynmark.com/knowledgebase/articles/83496-gsm-alphabet-character-set
 */

public class Gsm0338 {

    // HashMap's used for encoding and decoding
    protected static HashMap<Character, Byte> defaultEncodeMap = new HashMap();
    protected static HashMap<Character, Byte> extEncodeMap = new HashMap();

    private static final byte ESC_CHARACTER = 27;

    // Data to populate the hashmaps with
    private static final Object[][] gsmCharacters = {
            { '@',      new Byte((byte) 0x00) },
            { '£',      new Byte((byte) 0x01) },
            { '$',      new Byte((byte) 0x02) },
            { '¥',      new Byte((byte) 0x03) },
            { 'è',      new Byte((byte) 0x04) },
            { 'é',      new Byte((byte) 0x05) },
            { 'ù',      new Byte((byte) 0x06) },
            { 'ì',      new Byte((byte) 0x07) },
            { 'ò',      new Byte((byte) 0x08) },
            { 'Ç',      new Byte((byte) 0x09) },
            { '\n',     new Byte((byte) 0x0a) },
            { 'Ø',      new Byte((byte) 0x0b) },
            { 'ø',      new Byte((byte) 0x0c) },
            { '\r',     new Byte((byte) 0x0d) },
            { 'Å',      new Byte((byte) 0x0e) },
            { 'å',      new Byte((byte) 0x0f) },
            { '\u0394', new Byte((byte) 0x10) },
            { '_',      new Byte((byte) 0x11) },
            { '\u03A6', new Byte((byte) 0x12) },
            { '\u0393', new Byte((byte) 0x13) },
            { '\u039B', new Byte((byte) 0x14) },
            { '\u03A9', new Byte((byte) 0x15) },
            { '\u03A0', new Byte((byte) 0x16) },
            { '\u03A8', new Byte((byte) 0x17) },
            { '\u03A3', new Byte((byte) 0x18) },
            { '\u0398', new Byte((byte) 0x19) },
            { '\u039E', new Byte((byte) 0x1a) },
            { '\u001B', new Byte((byte) 0x1b) }, // 27 is Escape character
            { 'Æ',      new Byte((byte) 0x1c) },
            { 'æ',      new Byte((byte) 0x1d) },
            { 'ß',      new Byte((byte) 0x1e) },
            { 'É',      new Byte((byte) 0x1f) },
            { '\u0020', new Byte((byte) 0x20) },
            { '!',      new Byte((byte) 0x21) },
            { '\"',     new Byte((byte) 0x22) },
            { '#',      new Byte((byte) 0x23) },
            { '¤',      new Byte((byte) 0x24) },
            { '%',      new Byte((byte) 0x25) },
            { '&',      new Byte((byte) 0x26) },
            { '\'',     new Byte((byte) 0x27) },
            { '(',      new Byte((byte) 0x28) },
            { ')',      new Byte((byte) 0x29) },
            { '*',      new Byte((byte) 0x2a) },
            { '+',      new Byte((byte) 0x2b) },
            { ',',      new Byte((byte) 0x2c) },
            { '-',      new Byte((byte) 0x2d) },
            { '.',      new Byte((byte) 0x2e) },
            { '/',      new Byte((byte) 0x2f) },
            { '0',      new Byte((byte) 0x30) },
            { '1',      new Byte((byte) 0x31) },
            { '2',      new Byte((byte) 0x32) },
            { '3',      new Byte((byte) 0x33) },
            { '4',      new Byte((byte) 0x34) },
            { '5',      new Byte((byte) 0x35) },
            { '6',      new Byte((byte) 0x36) },
            { '7',      new Byte((byte) 0x37) },
            { '8',      new Byte((byte) 0x38) },
            { '9',      new Byte((byte) 0x39) },
            { ':',      new Byte((byte) 0x3a) },
            { ';',      new Byte((byte) 0x3b) },
            { '<',      new Byte((byte) 0x3c) },
            { '=',      new Byte((byte) 0x3d) },
            { '>',      new Byte((byte) 0x3e) },
            { '?',      new Byte((byte) 0x3f) },
            { '¡',      new Byte((byte) 0x40) },
            { 'A',      new Byte((byte) 0x41) },
            { 'B',      new Byte((byte) 0x42) },
            { 'C',      new Byte((byte) 0x43) },
            { 'D',      new Byte((byte) 0x44) },
            { 'E',      new Byte((byte) 0x45) },
            { 'F',      new Byte((byte) 0x46) },
            { 'G',      new Byte((byte) 0x47) },
            { 'H',      new Byte((byte) 0x48) },
            { 'I',      new Byte((byte) 0x49) },
            { 'J',      new Byte((byte) 0x4a) },
            { 'K',      new Byte((byte) 0x4b) },
            { 'L',      new Byte((byte) 0x4c) },
            { 'M',      new Byte((byte) 0x4d) },
            { 'N',      new Byte((byte) 0x4e) },
            { 'O',      new Byte((byte) 0x4f) },
            { 'P',      new Byte((byte) 0x50) },
            { 'Q',      new Byte((byte) 0x51) },
            { 'R',      new Byte((byte) 0x52) },
            { 'S',      new Byte((byte) 0x53) },
            { 'T',      new Byte((byte) 0x54) },
            { 'U',      new Byte((byte) 0x55) },
            { 'V',      new Byte((byte) 0x56) },
            { 'W',      new Byte((byte) 0x57) },
            { 'X',      new Byte((byte) 0x58) },
            { 'Y',      new Byte((byte) 0x59) },
            { 'Z',      new Byte((byte) 0x5a) },
            { 'Ä',      new Byte((byte) 0x5b) },
            { 'Ö',      new Byte((byte) 0x5c) },
            { 'Ñ',      new Byte((byte) 0x5d) },
            { 'Ü',      new Byte((byte) 0x5e) },
            { '§',      new Byte((byte) 0x5f) },
            { '¿',      new Byte((byte) 0x60) },
            { 'a',      new Byte((byte) 0x61) },
            { 'b',      new Byte((byte) 0x62) },
            { 'c',      new Byte((byte) 0x63) },
            { 'd',      new Byte((byte) 0x64) },
            { 'e',      new Byte((byte) 0x65) },
            { 'f',      new Byte((byte) 0x66) },
            { 'g',      new Byte((byte) 0x67) },
            { 'h',      new Byte((byte) 0x68) },
            { 'i',      new Byte((byte) 0x69) },
            { 'j',      new Byte((byte) 0x6a) },
            { 'k',      new Byte((byte) 0x6b) },
            { 'l',      new Byte((byte) 0x6c) },
            { 'm',      new Byte((byte) 0x6d) },
            { 'n',      new Byte((byte) 0x6e) },
            { 'o',      new Byte((byte) 0x6f) },
            { 'p',      new Byte((byte) 0x70) },
            { 'q',      new Byte((byte) 0x71) },
            { 'r',      new Byte((byte) 0x72) },
            { 's',      new Byte((byte) 0x73) },
            { 't',      new Byte((byte) 0x74) },
            { 'u',      new Byte((byte) 0x75) },
            { 'v',      new Byte((byte) 0x76) },
            { 'w',      new Byte((byte) 0x77) },
            { 'x',      new Byte((byte) 0x78) },
            { 'y',      new Byte((byte) 0x79) },
            { 'z',      new Byte((byte) 0x7a) },
            { 'ä',      new Byte((byte) 0x7b) },
            { 'ö',      new Byte((byte) 0x7c) },
            { 'ñ',      new Byte((byte) 0x7d) },
            { 'ü',      new Byte((byte) 0x7e) },
            { 'à',      new Byte((byte) 0x7f) }
    };

    private static final Object[][] gsmExtensionCharacters = {
            { '\n', new Byte((byte) 0x0a) },
            { '^',  new Byte((byte) 0x14) },
            { ' ',  new Byte((byte) 0x1b) }, // reserved for future extensions
            { '{',  new Byte((byte) 0x28) },
            { '}',  new Byte((byte) 0x29) },
            { '\\', new Byte((byte) 0x2f) },
            { '[',  new Byte((byte) 0x3c) },
            { '~',  new Byte((byte) 0x3d) },
            { ']',  new Byte((byte) 0x3e) },
            { '|',  new Byte((byte) 0x40) },
            { '€',  new Byte((byte) 0x65) }
    };

// static section that populates the encode and decode HashMap objects
    static {
        // default alphabet
        int len = gsmCharacters.length;
        for (int i = 0; i < len; i++) {
            Object[] map = gsmCharacters[i];
            defaultEncodeMap.put((Character) map[0], (Byte) map[1]);
        }

        // extended alphabet
        len = gsmExtensionCharacters.length;
        for (int i = 0; i < len; i++) {
            Object[] map = gsmExtensionCharacters[i];
            extEncodeMap.put((Character) map[0], (Byte) map[1]);
        }
    }

    public static boolean isGsm0338(String isoString) {
        for (int i = 0; i < isoString.length(); ++i) {

            if (defaultEncodeMap.containsKey(isoString.charAt(i)))
                continue;

            if (extEncodeMap.containsKey(isoString.charAt(i)))
                continue;

            return false;
        }

        return true;
    }

    public static byte[] encodeInGsm0338(String isoString) {

        byte[] result;
        int len = isoString.length();
        List<Byte> gsmBytes = new ArrayList<>();

        for (int i = 0; i < len; ++i) {

            if (defaultEncodeMap.containsKey(isoString.charAt(i))) {
                gsmBytes.add(defaultEncodeMap.get(isoString.charAt(i)));
                continue;
            }

            if (extEncodeMap.containsKey(isoString.charAt(i))) {
                gsmBytes.add(ESC_CHARACTER);
                gsmBytes.add(extEncodeMap.get(isoString.charAt(i)));
                continue;
            }

            return null;
        }

        if (!gsmBytes.isEmpty()) {
            result = new byte[gsmBytes.size()];

            for (int i =0; i < gsmBytes.size(); i++) {
                result[i] = gsmBytes.get(i);
            }

            return result;

        } else
            return null;
    }
}
