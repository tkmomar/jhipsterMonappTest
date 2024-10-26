package sn.mycompany.monapptest.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Author getAuthorSample1() {
        return new Author().id(1L).lastname("lastname1").firstname("firstname1").style("style1");
    }

    public static Author getAuthorSample2() {
        return new Author().id(2L).lastname("lastname2").firstname("firstname2").style("style2");
    }

    public static Author getAuthorRandomSampleGenerator() {
        return new Author()
            .id(longCount.incrementAndGet())
            .lastname(UUID.randomUUID().toString())
            .firstname(UUID.randomUUID().toString())
            .style(UUID.randomUUID().toString());
    }
}
