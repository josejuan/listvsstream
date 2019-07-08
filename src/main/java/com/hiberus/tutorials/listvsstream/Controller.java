package com.hiberus.tutorials.listvsstream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private Backend backend;

    @RequestMapping("/streaming")
    public Stream<String> streaming(
            @RequestParam(value = "from", required = true) int from,
            @RequestParam(value = "to", required = true) int to
    ) {
        return backend.streamingQuery(from, to)
                .map(Controller::rejoin)
                .map(Controller::resplit)
                .map(Controller::rejoin)
                .map(Controller::resplit)
                .map(Controller::rejoin)
                .map(Controller::resplit)
                .map(Controller::output);
    }

    @RequestMapping("/buffering")
    public List<String> buffering(
            @RequestParam(value = "from", required = true) int from,
            @RequestParam(value = "to", required = true) int to
    ) {
        return map(map(map(map(map(map(map(backend.bufferingQuery(from, to)
                , Controller::rejoin)
                , Controller::resplit)
                , Controller::rejoin)
                , Controller::resplit)
                , Controller::rejoin)
                , Controller::resplit)
                , Controller::output);
    }

    private <A, B> List<B> map(final List<A> xs, final Function<A, B> f) {
        final List<B> ys = new ArrayList<>(xs.size());
        for (final A x : xs)
            ys.add(f.apply(x));
        return ys;
    }

    private static Tuple<String, String> resplit(final String x) {
        final int i = x.indexOf(',');
        return new Tuple<>(x.substring(0, i), x.substring(i, x.length() - 1));
    }

    private static String rejoin(final Tuple<String, String> x) {
        return x.a + "," + x.b;
    }

    private static String output(final Tuple<String, String> x) {
        return x.a.length() + "." + x.b.length() + "." + x.a + "." + x.b;
    }

}
