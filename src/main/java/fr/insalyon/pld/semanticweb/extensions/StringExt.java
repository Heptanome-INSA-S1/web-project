package fr.insalyon.pld.semanticweb.extensions;

import fr.insalyon.pld.semanticweb.tools.IntGenerator;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Supplier;

import static fr.insalyon.pld.semanticweb.tools.Kotlin.mutableListOf;

public class StringExt {

    public URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> splitOfLength(String self, Integer length) {

        final List<String> result = mutableListOf();
        final IntGenerator startIndex = new IntGenerator(length);

        Supplier<Integer> endIndex = () -> {
            if (startIndex.current() + length > self.length()) {
                return self.length();
            } else {
                return startIndex.current() + length;
            }
        };

        while (startIndex.current() < self.length()) {
            String currentPart = self.substring(startIndex.current(), endIndex.get());
            result.add(currentPart);
            startIndex.next();
        }

        return result;

    }

    public static String toUrlParameter(String self) {

        return toUrlParameter(self, "UTF-8");

    }

    public static String toUrlParameter(String self, String charset) {

        try {
            return URLEncoder.encode(self.replace("\"", ""), charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
