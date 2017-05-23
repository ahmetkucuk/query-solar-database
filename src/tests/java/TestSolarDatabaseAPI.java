import core.DBConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import services.SolarDatabaseAPI;

import java.sql.SQLException;

/**
 * Created by ahmetkucuk on 5/23/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DBConnection.class)
public class TestSolarDatabaseAPI {

    @Test
    public void testQueueDownload() throws Exception {

//        PowerMockito.mockStatic(DBConnection.class);
//        PowerMockito.when(DBConnection.getNewConnection()).thenThrow(new Exception());
//        SolarDatabaseAPI mockApi = Mockito.mock(SolarDatabaseAPI.class);
//        SolarDatabaseAPI api = new SolarDatabaseAPI();
//        api.pullEvents("2000-07-07T02:00:00", "2010-07-13T02:00:00", 1);
//        api.pullEvents("2000-08-07T02:00:00", "2010-09-13T02:00:00", 1);
    }
}
