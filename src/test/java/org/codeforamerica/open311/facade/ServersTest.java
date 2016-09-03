package org.codeforamerica.open311.facade;

import org.codeforamerica.open311.facade.data.City;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    }

    @AfterClass
    public static void testFinish() {
        System.out.println("[SERVERS TEST] Ends");
    }

}
