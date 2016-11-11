package org.codeforamerica.open311.facade;

import org.codeforamerica.open311.facade.data.City;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by miblon on 9/1/16.
 */
public class ServersTest {
    private static Servers servers = new Servers();

    @BeforeClass
    public static void testInitialization() {
        System.out.println("[SERVERS TEST] Starts");
    }

    @Test
    public void getServersTest() {
        assertNotNull(servers.getCollection());
        assertEquals(City.SAN_FRANCISCO.getDiscoveryUrl(), "http://mobile311.sfgov.org/open311/discovery.xml");
        assertNull(City.SAN_FRANCISCO.getUpload());
    }

    @Test
    public void getAdvancedServersTest() {
        assertNotNull(servers.getCollection());
        assertEquals("https://www.open311.io/api/v2/discovery.json", City.EINDHOVEN.getDiscoveryUrl());
        assertEquals("https://www.open311.io/files", City.EINDHOVEN.getUpload().getUrl());
        assertEquals(5.466667, City.EINDHOVEN.getMap().getLon(),0.1);
        assertEquals(51.433333, City.EINDHOVEN.getMap().getLat(),0.1);
        assertEquals(10, City.EINDHOVEN.getMap().getZoom());
    }

    @AfterClass
    public static void testFinish() {
        System.out.println("[SERVERS TEST] Ends");
    }

}
