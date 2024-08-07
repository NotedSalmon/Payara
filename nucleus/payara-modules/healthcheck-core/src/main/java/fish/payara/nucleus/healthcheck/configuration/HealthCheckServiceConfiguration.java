/*
 *
 * Copyright (c) 2016-2021 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/main/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package fish.payara.nucleus.healthcheck.configuration;

import java.beans.PropertyVetoException;
import java.util.List;

import jakarta.validation.constraints.Min;

import org.glassfish.api.admin.config.ConfigExtension;
import org.jvnet.hk2.config.Attribute;
import org.jvnet.hk2.config.Configured;
import org.jvnet.hk2.config.DuckTyped;
import org.jvnet.hk2.config.Element;

/**
 * @author mertcaliskan
 *
 */
@Configured
public interface HealthCheckServiceConfiguration extends ConfigExtension {

    @Attribute(defaultValue="false",dataType=Boolean.class)
    String getEnabled();
    void enabled(String value) throws PropertyVetoException;

    @Attribute(defaultValue = "false", dataType = Boolean.class)
    String getHistoricalTraceEnabled();
    void setHistoricalTraceEnabled(String value) throws PropertyVetoException;

    @Attribute(defaultValue = "20", dataType = Integer.class)
    @Min(value = 0)
    String getHistoricalTraceStoreSize();
    void setHistoricalTraceStoreSize(String value) throws PropertyVetoException;

    @Attribute
    String getHistoricalTraceStoreTimeout();
    void setHistoricalTraceStoreTimeout(String value) throws PropertyVetoException;

    @Element("*")
    List<Checker> getCheckerList();

    @DuckTyped
    <T extends Checker> T getCheckerByType(Class<T> type);

    @Element("notifier")
    List<String> getNotifierList();

    class Duck {
        public static <T extends Checker> T getCheckerByType(HealthCheckServiceConfiguration config, Class<T> type) {
            for (Checker checker : config.getCheckerList()) {
                try {
                    return type.cast(checker);
                } catch (Exception e) {
                    // ignore, not the right type.
                }
            }
            return null;
        }
    }
}