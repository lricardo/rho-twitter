package pt.rhosystems.rhotwitter;

import org.junit.Test;

import pt.rhosystems.rhotwitter.utilities.ExpiringList;

public class ExpiringListTests {

    /**
     * Test the clearExpired functionality
     * @throws InterruptedException
     */
    @Test
    public void testClearExpired() throws InterruptedException {

        ExpiringList<String> l = new ExpiringList<>(3000);

        for (int i = 0; i < 10; i++)
            l.add("Test String" + i);

        System.out.println(l.size());
        assert l.size() == 10;

        Thread.sleep(3000);
        l.add("Test String");

        System.out.println(l.size());
        assert l.size() == 1;

    }
}
