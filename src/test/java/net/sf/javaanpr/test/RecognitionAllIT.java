package net.sf.javaanpr.test;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import org.xml.sax.SAXException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.*;

/**
 * Created by Emil on 06/03/2017.
 */

@RunWith(Parameterized.class)
public class RecognitionAllIT {

    CarSnapshot carSnap;
    String snapName;

    public RecognitionAllIT(File file, String plateExpected) throws IOException {
            snapName = plateExpected;
            carSnap = new CarSnapshot(new FileInputStream(file));
    }

    @Parameters
    public static Collection<Object[]> data() {
        Properties properties = new Properties();
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        try {
            InputStream resultsStream = new FileInputStream(new File(resultsPath));
            properties.load(resultsStream);
            resultsStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();

        Collection<Object[]> dataForOneImage= new ArrayList();
        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected });
        }
        return dataForOneImage;
    }

    @Test
    public void testPlate() {
        Intelligence intel;
        try {
            intel = new Intelligence();
            String plateCorrect = snapName;
            String numberPlate = intel.recognize(carSnap, false);
            System.out.println("numberPlate = " + numberPlate);
            assertThat(numberPlate, is(equalTo(plateCorrect)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
