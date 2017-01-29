
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import services.SolarDatabaseAPI;

import java.text.ParseException;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by ahmetkucuk on 12/17/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(QuerySolarDB.class)
public class TestQuerySolarDB {

    @Test
    public void testRetrieveInChunks() throws ParseException {
        //PowerMockito.when(QuerySolarDB.retrieveAll(null, null, 1)).thenReturn();
        SolarDatabaseAPI mockApi = Mockito.mock(SolarDatabaseAPI.class);
        Mockito.doNothing().when(mockApi).pullEvents(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
        QuerySolarDB.retrieveInChunks(mockApi, "2000-07-07T02:00:00", "2010-07-13T02:00:00", 1);
    }

}
