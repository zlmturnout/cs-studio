/*
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
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
package org.csstudio.archive.common.service.util;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.csstudio.archive.common.service.channel.ArchiveChannelId;
import org.csstudio.archive.common.service.channel.ArchiveLimitsChannel;
import org.csstudio.archive.common.service.channel.IArchiveChannel;
import org.csstudio.archive.common.service.channelgroup.ArchiveChannelGroupId;
import org.csstudio.archive.common.service.controlsystem.IArchiveControlSystem;
import org.csstudio.domain.desy.time.TimeInstant;
import org.csstudio.domain.desy.typesupport.TypeSupportException;

/**
 * Common type conversions for {@link Number} subtypes.
 *
 * @author bknerr
 * @since 10.12.2010
 * @param <N> the number subtype
 */
public abstract class AbstractNumberArchiveTypeConversionSupport<N extends Number & Comparable<? super N> & Serializable >
                      extends ArchiveTypeConversionSupport<N> {
    /**
     * Constructor.
     */
    AbstractNumberArchiveTypeConversionSupport(@Nonnull final Class<N> type) {
        super(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public String convertToArchiveString(@Nonnull final N value) throws TypeSupportException {
        return value.toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public Boolean isOptimizableByAveraging() {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    // CHECKSTYLE OFF : ParameterNumber
    protected IArchiveChannel createChannel(@Nonnull final ArchiveChannelId id,
                                            @Nonnull final String name,
                                            @Nullable final Class<N> datatype,
                                            @Nonnull final ArchiveChannelGroupId grpId,
                                            @Nonnull final TimeInstant time,
                                            @Nonnull final IArchiveControlSystem cs,
                                            final boolean enabled,
                                            @Nonnull final N low,
                                            @Nonnull final N high) {
        // CHECKSTYLE ON : ParameterNumber
        return new ArchiveLimitsChannel<N>(id, name, datatype, grpId, time, cs, enabled, low, high);
    }
}
