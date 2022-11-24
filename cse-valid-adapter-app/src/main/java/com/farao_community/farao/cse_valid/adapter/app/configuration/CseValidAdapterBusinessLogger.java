/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oualid Aloui {@literal <oualid.aloui at rte-france.com>}
 */
@Configuration
public class CseValidAdapterBusinessLogger {

    @Bean
    public Logger getBusinessLogger() {
        return LoggerFactory.getLogger("CSE_VALID_ADAPTER_BUSINESS_LOGGER");
    }
}
