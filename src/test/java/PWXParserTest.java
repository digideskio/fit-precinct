import de.konqi.fitapi.common.pwx.PwxFile;
import de.konqi.fitapi.common.pwx.PwxParser;
import de.konqi.fitapi.common.pwx.PwxWorkoutConverter;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;

/**
 * Created by konqi on 04.04.2016.
 */
public class PWXParserTest {
    @Test
    public void parseFile() throws XMLStreamException, NoSuchFieldException, IllegalAccessException {
        long start = System.currentTimeMillis();
        PwxParser pwxParser = new PwxParser();
        PwxFile pwxFile = pwxParser.parse(PWXParserTest.class.getResourceAsStream("20160403_152547_1.pwx"));

        PwxWorkoutConverter pwxWorkoutCoverter = new PwxWorkoutConverter(pwxFile);
        Workout workout = pwxWorkoutCoverter.getWorkout();
        HashMap<String, WorkoutData> samples = pwxWorkoutCoverter.getSamples();

        long duration = System.currentTimeMillis() - start;
        System.out.println("done in " + duration + "ms");
    }
}
