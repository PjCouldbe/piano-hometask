package io.piano.nlp.test;

import io.piano.nlp.domain.time.TimeRange;
import io.piano.nlp.shared.Token;
import io.piano.nlp.shared.TokenType;
import org.junit.Before;

import java.util.Date;

/**
 * Parent for all NLP tests containing base service methods
 *
 * Created by Dima on 03.06.2018.
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseNLPTest {
    private long lastTimeTemplate;

    protected static Token of(String text, TokenType type) {
        return new Token(text, type);
    }

    protected static Token of(String text, TokenType type, String posTag) {
        return new Token(text, type, posTag);
    }


    protected static TimeRange of(long from, long to) {
        return new TimeRange(new Date(from), new Date(to));
    }
    protected static TimeRange of(Date from, Date to) {
        return new TimeRange(from, to);
    }

    protected TimeRange roundDate(TimeRange tr) {
        tr.setBegin( roundDate(tr.getBegin()) );
        tr.setEnd( roundDate(tr.getEnd()) );
        return tr;
    }
    protected Date roundDate(Date date) {
        if (lastTimeTemplate == 0L) {
            lastTimeTemplate = (date.getTime() / 1000) * 1000;    //div 100, then '* 1000' for round number obtaining
        }
        date.setTime(lastTimeTemplate);
        return date;
    }

    @Before
    public void setUp() {
        lastTimeTemplate = 0L;
    }
}
