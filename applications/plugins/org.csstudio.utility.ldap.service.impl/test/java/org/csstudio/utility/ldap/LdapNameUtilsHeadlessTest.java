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
 *
 * $Id$
 */
package org.csstudio.utility.ldap;

import static org.csstudio.utility.ldap.LdapNameUtilsUnitTest.COUNTRY_FIELD_NAME;
import static org.csstudio.utility.ldap.LdapNameUtilsUnitTest.COUNTRY_FIELD_VALUE;
import static org.csstudio.utility.ldap.LdapNameUtilsUnitTest.ECON_FIELD_VALUE;
import static org.csstudio.utility.ldap.LdapNameUtilsUnitTest.EFAN_FIELD_VALUE;
import static org.csstudio.utility.ldap.LdapNameUtilsUnitTest.O_FIELD_VALUE;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.COMPONENT;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.FACILITY;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.IOC;
import static org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsConfiguration.UNIT;
import static org.csstudio.utility.ldap.treeconfiguration.LdapFieldsAndAttributes.ORGANIZATION_FIELD_NAME;
import static org.csstudio.utility.ldap.utils.LdapUtils.createLdapName;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import junit.framework.Assert;

import org.csstudio.utility.ldap.treeconfiguration.LdapEpicsControlsFieldsAndAttributes;
import org.csstudio.utility.ldap.utils.LdapNameUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for LdapNameUtils that requires plugins.
 *
 * @author bknerr
 * @author $Author$
 * @version $Revision$
 * @since 21.07.2010
 */
public class LdapNameUtilsHeadlessTest {

    private static SearchResult RESULT;
    private static LdapName QUERY;

    @BeforeClass
    public static void setUp() {

        RESULT = new SearchResult("econ=berndTest",
                                   null,
                                   null);

        QUERY = createLdapName(IOC.getNodeTypeName(), ECON_FIELD_VALUE,
                                COMPONENT.getNodeTypeName(), LdapEpicsControlsFieldsAndAttributes.ECOM_EPICS_IOC_FIELD_VALUE,
                                FACILITY.getNodeTypeName(), EFAN_FIELD_VALUE,
                                UNIT.getNodeTypeName(), UNIT.getRootTypeValue(),
                                ORGANIZATION_FIELD_NAME, O_FIELD_VALUE,
                                COUNTRY_FIELD_NAME,COUNTRY_FIELD_VALUE);

        RESULT.setNameInNamespace(QUERY.toString());
    }

    @Test
    public void testLdapNameParsing() {
        try {
            LdapNameUtils.parseSearchResult(RESULT);
        } catch (final NamingException e) {
            Assert.fail();
        }
    }
}
