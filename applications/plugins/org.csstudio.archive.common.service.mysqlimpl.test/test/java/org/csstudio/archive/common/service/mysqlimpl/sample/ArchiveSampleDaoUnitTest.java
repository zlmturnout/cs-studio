/*
 * Copyright (c) 2011 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
package org.csstudio.archive.common.service.mysqlimpl.sample;

import static org.csstudio.archive.common.service.mysqlimpl.sample.TestSampleProvider.CHANNEL_ID_3RD;
import static org.csstudio.archive.common.service.mysqlimpl.sample.TestSampleProvider.START;

import java.util.Collection;

import junit.framework.Assert;

import org.csstudio.archive.common.service.channel.IArchiveChannel;
import org.csstudio.archive.common.service.mysqlimpl.channel.ArchiveChannelDaoImpl;
import org.csstudio.archive.common.service.mysqlimpl.channel.IArchiveChannelDao;
import org.csstudio.archive.common.service.mysqlimpl.dao.AbstractDaoTestSetup;
import org.csstudio.archive.common.service.mysqlimpl.dao.ArchiveDaoException;
import org.csstudio.archive.common.service.mysqlimpl.requesttypes.DesyArchiveRequestType;
import org.csstudio.archive.common.service.sample.IArchiveSample;
import org.csstudio.domain.desy.system.ISystemVariable;
import org.csstudio.domain.desy.time.TimeInstant.TimeInstantBuilder;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Integration test for {@link ArchiveSampleDaoImpl} with rollbacks.
 *
 * @author bknerr
 * @since 27.07.2011
 */
public class ArchiveSampleDaoUnitTest extends AbstractDaoTestSetup {
    private static IArchiveSampleDao SAMPLE_DAO;
    private static IArchiveChannelDao CHANNEL_DAO;

    @BeforeClass
    public static void setupDao() {
        CHANNEL_DAO = new ArchiveChannelDaoImpl(HANDLER, PERSIST_MGR);
        SAMPLE_DAO = new ArchiveSampleDaoImpl(HANDLER, PERSIST_MGR, CHANNEL_DAO);
    }

    @Test
    public void retrieveSamples() throws ArchiveDaoException {
        final IArchiveChannel channel = CHANNEL_DAO.retrieveChannelById(CHANNEL_ID_3RD);
        final IArchiveSample<Object,ISystemVariable<Object>> sample =
            SAMPLE_DAO.retrieveLatestSampleBeforeTime(channel, START);
        Assert.assertNotNull(sample);
        Assert.assertEquals(Byte.valueOf((byte) 26), sample.getValue());


        final Collection<IArchiveSample<Object,ISystemVariable<Object>>>
            samples = SAMPLE_DAO.retrieveSamples(DesyArchiveRequestType.RAW, channel, TimeInstantBuilder.fromNanos(1L), START);
        Assert.assertNotNull(samples);
        Assert.assertEquals(1, samples.size());
        Assert.assertEquals(Byte.valueOf((byte) 26), samples.iterator().next().getValue());
    }
}
