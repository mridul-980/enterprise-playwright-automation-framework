package tests.base;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MavenTestNgSmokeTest {

    @Test(groups = "smoke")
    public void testNgSmokeTestRuns() {
        Assert.assertEquals("framework-smoke", "framework-smoke");
    }
}
