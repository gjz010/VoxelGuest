package com.thevoxelbox.voxelguest.modules.asshat.mute;

import com.thevoxelbox.voxelguest.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Monofraps
 */
public class MuteTest
{
    private static final String TEST_BAN_PLAYER = "PeanutSmurf";
    private static final String TEST_BAN_REASON = "Coz you suck!";
    private Mutelist mutelist = null;

    @Before
    public void setUp() throws Exception
    {
        Persistence.getInstance().initialize(File.createTempFile("voxelguest_test_db", ".db"));
        mutelist = new Mutelist();
        assertTrue(mutelist.mute(TEST_BAN_PLAYER, TEST_BAN_REASON));
    }

    @After
    public void tearDown() throws Exception
    {
        mutelist = null;
        Persistence.getInstance().shutdown();
    }

    @Test(expected = InvalidStateException.class)
    public void testExceptionOnDoubleMute() throws Exception
    {
        assertFalse(mutelist.mute(TEST_BAN_PLAYER, TEST_BAN_REASON));
    }

    @Test(expected = InvalidStateException.class)
    public void testFalseOnDoubleUnmute() throws Exception
    {
        assertTrue(mutelist.unmute(TEST_BAN_REASON));
        assertFalse(mutelist.unmute(TEST_BAN_REASON));
    }

    @Test
    public void testMute() throws Exception
    {
        assertTrue(mutelist.isPlayerMuted(TEST_BAN_PLAYER));
    }

    @Test
    public void testMutereason() throws Exception
    {
        assertEquals(mutelist.whyIsPlayerMuted(TEST_BAN_PLAYER), TEST_BAN_REASON);
    }

    @Test
    public void testUnmute() throws Exception
    {
        assertTrue(mutelist.unmute(TEST_BAN_PLAYER));
        assertFalse(mutelist.isPlayerMuted(TEST_BAN_PLAYER));
    }
}
