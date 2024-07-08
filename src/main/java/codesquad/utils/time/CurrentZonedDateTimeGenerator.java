package codesquad.utils.time;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CurrentZonedDateTimeGenerator implements ZonedDateTimeGenerator {

    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
    
}
