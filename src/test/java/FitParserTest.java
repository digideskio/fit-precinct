import de.konqi.fitapi.common.fit.FitData;
import de.konqi.fitapi.common.fit.FitHeader;
import de.konqi.fitapi.common.fit.FitParser;
import de.konqi.fitapi.common.fit.FitWorkoutConverter;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by konqi on 22.04.2016.
 */
public class FitParserTest {
    @Test
    public void parseFile() throws IOException {
        long start = System.currentTimeMillis();
        FitParser parser = new FitParser();
        InputStream resourceAsStream = FitParserTest.class.getResourceAsStream("20160403_120310_3.fit");
        FitHeader header = parser.parseHeader(resourceAsStream);
        System.out.println(header.toString());
        FitData fitData = parser.readRecords(resourceAsStream, header.getDataSize());

        long duration = System.currentTimeMillis() - start;
        System.out.println("parser done in " + duration + "ms");

        FitWorkoutConverter fitWorkoutConverter = new FitWorkoutConverter(fitData);
        Workout workout = fitWorkoutConverter.getWorkout();
        HashMap<String, WorkoutData> dataMap = fitWorkoutConverter.getSamples();

        duration = System.currentTimeMillis() - start;
        System.out.println("conversion done in " + duration + "ms");

        System.out.println(workout);
    }
}
