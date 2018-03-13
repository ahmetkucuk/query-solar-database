
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import hek.services.SolarDatabaseAPI;

import java.text.ParseException;

/**
 * Created by ahmetkucuk on 12/17/16.
 */
@RunWith(PowerMockRunner.class)
public class TestQuerySolarDB {

    @Test
    public void testRetrieveInChunks() throws ParseException {
        //PowerMockito.when(task.QuerySolarDB.retrieveAll(null, null, 1)).thenReturn();
        SolarDatabaseAPI mockApi = Mockito.mock(SolarDatabaseAPI.class);
        Mockito.doNothing().when(mockApi).pullEvents(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
    }

}
