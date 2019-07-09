package com.hiberus.tutorials.listvsstream;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class Backend {

    private XoRoShiRo128PlusRandom rg = new XoRoShiRo128PlusRandom(ThreadLocalRandom.current().nextLong());

    private static String int2pk(int pk) {
        final String s = Integer.toString(pk);
        return "0000000".substring(0, 7 - s.length()) + s;
    }

    Tuple<String, String> create(int i) {
        return new Tuple<>(int2pk(i), Long.toString(rg.nextLong()));
    }

    public Stream<Tuple<String, String>> streamingQuery(int from, int to) {
        return IntStream.range(from, to + 1).mapToObj(this::create);
    }

    public List<Tuple<String, String>> bufferingQuery(int from, int to) {
        final List<Tuple<String, String>> xs = new ArrayList<>(to - from + 1);
        for (int i = from; i <= to; i++)
            xs.add(create(i));
        return xs;
    }
}
