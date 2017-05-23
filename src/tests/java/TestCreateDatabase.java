import core.DBConnection;
import core.EventAttributeManager;
import core.TableCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import services.SolarDatabaseAPI;

import java.text.ParseException;

/**
 * Created by ahmetkucuk on 5/22/17.
 */

@RunWith(PowerMockRunner.class)
public class TestCreateDatabase {

    @Test
    public void testCreateTables() throws ParseException {
        //PowerMockito.when(QuerySolarDB.retrieveAll(null, null, 1)).thenReturn();
//        SolarDatabaseAPI mockApi = Mockito.mock(SolarDatabaseAPI.class);
//        Mockito.doNothing().when(mockApi).pullEvents(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
//        QuerySolarDB.retrieveInChunks(mockApi, "2000-07-07T02:00:00", "2010-07-13T02:00:00", 1);
        EventAttributeManager.init();
        DBConnection mockConnection = Mockito.mock(DBConnection.class);
        Mockito.doNothing().when(mockConnection).connect();
        Mockito.doNothing().when(mockConnection).closeConnection();
        Mockito.doReturn(true).when(mockConnection).executeCommand(Mockito.anyString());
        TableCreator tableCreator = new TableCreator(mockConnection);
        tableCreator.createTables();

    }
}
