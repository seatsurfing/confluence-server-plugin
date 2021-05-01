package ut.de.seatsurfing.confluence;

import org.junit.Test;
import de.seatsurfing.confluence.api.MyPluginComponent;
import de.seatsurfing.confluence.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}