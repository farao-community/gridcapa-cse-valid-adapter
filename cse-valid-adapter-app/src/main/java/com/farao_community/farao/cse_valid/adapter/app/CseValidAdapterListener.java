/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app;

import com.farao_community.farao.cse_valid.api.resource.CseValidFileResource;
import com.farao_community.farao.cse_valid.api.resource.CseValidRequest;
import com.farao_community.farao.gridcapa.task_manager.api.ProcessFileDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskStatus;
import com.farao_community.farao.gridcapa_cse_valid.starter.CseValidClient;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author Theo Pascoli {@literal <theo.pascoli at rte-france.com>}
 */
@Component
public class CseValidAdapterListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CseValidAdapterListener.class);
    private final CseValidClient cseValidClient;
    private final CseValidAdapterConfiguration cseValidAdapterConfiguration;

    public CseValidAdapterListener(CseValidClient cseValidClient, CseValidAdapterConfiguration cseValidAdapterConfiguration) {
        this.cseValidClient = cseValidClient;
        this.cseValidAdapterConfiguration = cseValidAdapterConfiguration;
    }

    @Bean
    public Consumer<TaskDto> consumeTask() {
        return this::handleTask;
    }

    private void handleTask(TaskDto taskDto) {
        try {
            if (taskDto.getStatus() == TaskStatus.READY
                    || taskDto.getStatus() == TaskStatus.SUCCESS
                    || taskDto.getStatus() == TaskStatus.ERROR) {
                LOGGER.info("Handling manual run request on TS {} ", taskDto.getTimestamp());
                CseValidRequest cseValidRequest = buildCseValidRequest(taskDto);
                cseValidClient.run(cseValidRequest);
            } else {
                LOGGER.warn("Failed to handle manual run request on timestamp {} because it is not ready yet", taskDto.getTimestamp());
            }
        } catch (Exception e) {
            throw new CseValidAdapterException(String.format("Error during handling manual run request %s on TS ", taskDto.getTimestamp()), e);
        }
    }

    private CseValidRequest buildCseValidRequest(TaskDto taskDto) {
        switch (cseValidAdapterConfiguration.getTargetProcess()) {
            case "IDCC":
                return CseValidRequest.buildIdccValidRequest(taskDto.getId().toString(),
                        taskDto.getTimestamp(),
                        getFileFromTaskDto(taskDto, "TTC_ADJUSTMENT"),
                        getFileFromTaskDto(taskDto, "CRAC"),
                        getFileFromTaskDto(taskDto, "CGM"),
                        getFileFromTaskDto(taskDto, "GLSK"));
            case "D2CC":
                return CseValidRequest.buildD2ccValidRequest(taskDto.getId().toString(),
                        taskDto.getTimestamp(),
                        getFileFromTaskDto(taskDto, "TTC_ADJUSTMENT"),
                        getFileFromTaskDto(taskDto, "CRAC"),
                        getFileFromTaskDto(taskDto, "CGM"),
                        getFileFromTaskDto(taskDto, "GLSK"));
            default:
                throw new NotImplementedException(String.format("Unknown target process for CSE: %s", cseValidAdapterConfiguration.getTargetProcess()));

        }
    }

    private CseValidFileResource getFileFromTaskDto(TaskDto taskDto, String fileType) {
        for (ProcessFileDto processFileDto : taskDto.getProcessFiles()) {
            String processFileDtoFileType = processFileDto.getFileType();
            if (fileType.equals(processFileDtoFileType)) {
                return new CseValidFileResource(processFileDto.getFilename(), processFileDto.getFileUrl());
            }
        }
        return null;
    }
}
