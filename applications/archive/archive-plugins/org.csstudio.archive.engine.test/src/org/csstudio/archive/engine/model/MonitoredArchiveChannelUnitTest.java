/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.archive.engine.model;

import static org.junit.Assert.assertTrue;

import org.csstudio.vtype.pv.PV;
import org.csstudio.vtype.pv.PVPool;
import org.csstudio.vtype.pv.local.LocalPVFactory;
import org.junit.Test;

/** JUnit test of the {@link MonitoredArchiveChannel}
 *
 *  <p>Depending on timing, this test does not always pass...
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class MonitoredArchiveChannelUnitTest
{
    private static final String PV_NAME = "loc://demo(47)";

    @Test
    public void testHandleNewValue() throws Exception
    {
        PVPool.addPVFactory(new LocalPVFactory());

        final PV pv = PVPool.getPV(PV_NAME);
        final MonitoredArchiveChannel channel = new MonitoredArchiveChannel(PV_NAME, Enablement.Passive, 100, null, 0.1);
        final SampleBuffer samples = channel.getSampleBuffer();
        channel.start();

        // Need some delay to get new time stamps, otherwise
        // new values will be skipped since they hold the same time stamp
        Thread.sleep(10);
        pv.write(1.0);
        Thread.sleep(10);
        pv.write(2.0);
        Thread.sleep(10);
        pv.write(2.05);
        Thread.sleep(10);
        pv.write(2.5);

        // Allow monitors to arrive..
        Thread.sleep(5000);

        assertTrue(TestHelper.dump(samples) >= 4);

        channel.stop();
        PVPool.releasePV(pv);
    }
}
