/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app.configuration;

import com.farao_community.farao.cse_valid.api.resource.ProcessType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oualid Aloui {@literal <oualid.aloui at rte-france.com>}
 */
@Configuration
public class CseValidAdapterConfiguration {

    @Value("${cse-valid-adapter.target-process}")
    private String targetProcess;

    public ProcessType getTargetProcess() {
        return ProcessType.valueOf(targetProcess);
    }
}
