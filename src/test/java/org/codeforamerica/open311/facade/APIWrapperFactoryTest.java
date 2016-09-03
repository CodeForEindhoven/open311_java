package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codeforamerica.open311.facade.data.City;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.caching.NoCache;
import org.codeforamerica.open311.internals.network.HTTPNetworkManager;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the {@link APIWrapperFactory} class.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class APIWrapperFactoryTest {
    @BeforeClass
    public static void testInitialization() {
        System.out.println("[API WRAPPER FACTORY TEST] Starts\n");
    }

    @AfterClass
    public static void testFinish() {
        System.out.println("[API WRAPPER FACTORY TEST] Ends");
    }

    @Test
    public void getMockNetworkDiscovery() throws APIWrapperException, ClassNotFoundException {
        APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO,
                EndpointType.TEST).setNetworkManager(new MockNetworkManager(Format.XML))
                .build();
        assertEquals(wrapper.getWrapperInfo(),
                "https://open311.sfgov.org/dev/v2 - TEST");
    }

    @Test
    public void getHTTPNetworkDiscovery() throws APIWrapperException, ClassNotFoundException {
        // TODO GetDiscovery test for all Cities

        for (City d : City.values()) {
            try {
                APIWrapper wrapper = new APIWrapperFactory(d,
                        EndpointType.PRODUCTION).setNetworkManager(new HTTPNetworkManager())
                        .setCache(new NoCache())
                        //.withLogs()
                        .build();
                System.out.println("Processing City: " + d);
                assertNotNull(wrapper.getEndpointUrl());
            } catch (Exception e) {
                System.out.println("Skipped " + d + ", error: " + e.getMessage());
            }
        }
    }

    @Test
    public void buildFromEndpointTest() throws APIWrapperException, ClassNotFoundException {
        APIWrapper wrapper = new APIWrapperFactory("https://www.endpoint.com",
                "jurisdictionId").setNetworkManager(new MockNetworkManager(Format.XML))
                .build();
        assertEquals(wrapper.getWrapperInfo(),
                "https://www.endpoint.com - UNKNOWN");
    }
}
