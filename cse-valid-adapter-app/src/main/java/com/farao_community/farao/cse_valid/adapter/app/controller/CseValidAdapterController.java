/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app.controller;

import com.farao_community.farao.cse_valid.adapter.app.service.CseValidAdapterService;
import com.farao_community.farao.gridcapa.task_manager.api.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

/**
 * @author Oualid Aloui {@literal <oualid.aloui at rte-france.com>}
 */
@Controller
public class CseValidAdapterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CseValidAdapterController.class);
    private final CseValidAdapterService cseValidAdapterService;

    public CseValidAdapterController(CseValidAdapterService cseValidAdapterService) {
        this.cseValidAdapterService = cseValidAdapterService;
    }

    @Bean
    public Consumer<Flux<TaskDto>> consumeTask() {
        return f -> f
                .onErrorContinue((t, r) -> LOGGER.error(t.getMessage(), t))
                .subscribe(cseValidAdapterService::runAsync);
    }
}
