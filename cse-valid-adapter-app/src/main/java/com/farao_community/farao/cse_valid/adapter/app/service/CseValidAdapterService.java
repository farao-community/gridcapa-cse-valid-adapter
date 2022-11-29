/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app.service;

import com.farao_community.farao.cse_valid.adapter.app.configuration.CseValidAdapterConfiguration;
import com.farao_community.farao.cse_valid.adapter.app.exception.CseValidAdapterServiceException;
import com.farao_community.farao.cse_valid.api.resource.CseValidFileResource;
import com.farao_community.farao.cse_valid.api.resource.CseValidRequest;
import com.farao_community.farao.gridcapa.task_manager.api.ProcessFileDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskStatus;
import com.farao_community.farao.gridcapa.task_manager.api.TaskStatusUpdate;
import com.farao_community.farao.gridcapa_cse_valid.starter.CseValidClient;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Oualid Aloui {@literal <oualid.aloui at rte-france.com>}
 */
@Service
public class CseValidAdapterService {

    private static final String TTC_ADJUSTMENT_FILE_TYPE = "TTC_ADJUSTMENT";
    private static final String CRAC_FILE_TYPE = "CRAC";
    private static final String CGM_FILE_TYPE = "CGM";
    private static final String GLSK_FILE_TYPE = "GLSK";
    private static final String TASK_STATUS_UPDATE = "task-status-update";

    private final CseValidClient cseValidClient;
    private final CseValidAdapterConfiguration cseValidAdapterConfiguration;
    private final StreamBridge streamBridge;
    private final Logger businessLogger;

    public CseValidAdapterService(CseValidClient cseValidClient,
                                  CseValidAdapterConfiguration cseValidAdapterConfiguration,
                                  StreamBridge streamBridge,
                                  Logger businessLogger) {
        this.cseValidClient = cseValidClient;
        this.cseValidAdapterConfiguration = cseValidAdapterConfiguration;
        this.streamBridge = streamBridge;
        this.businessLogger = businessLogger;
    }

    public void runAsync(TaskDto taskDto) {
        MDC.put("gridcapa-task-id", taskDto.getId().toString());
        try {
            businessLogger.info("Handling manual run request on TS {} ", taskDto.getTimestamp());
            CseValidRequest cseValidRequest = buildCseValidRequest(taskDto);
            CompletableFuture.runAsync(() -> cseValidClient.run(cseValidRequest));
        } catch (Exception e) {
            businessLogger.error(String.format("Unexpected error occurred during building the request, task %s will not be run. Reason: %s", taskDto.getId().toString(), e.getMessage()));
            streamBridge.send(TASK_STATUS_UPDATE, new TaskStatusUpdate(taskDto.getId(), TaskStatus.ERROR));
        }
    }

    private CseValidRequest buildCseValidRequest(TaskDto taskDto) {
        Map<String, CseValidFileResource> files = getCseValidFileResourcesFromTaskDto(taskDto);
        String id = taskDto.getId().toString();
        OffsetDateTime timestamp = taskDto.getTimestamp();
        CseValidFileResource ttcAdjustment = getCseValidFileResource(files, TTC_ADJUSTMENT_FILE_TYPE);
        CseValidFileResource crac = getCseValidFileResource(files, CRAC_FILE_TYPE);
        CseValidFileResource cgm = getCseValidFileResource(files, CGM_FILE_TYPE);
        CseValidFileResource glsk = getCseValidFileResource(files, GLSK_FILE_TYPE);
        switch (cseValidAdapterConfiguration.getTargetProcess()) {
            case IDCC:
                return CseValidRequest.buildIdccValidRequest(id, timestamp, ttcAdjustment, crac, cgm, glsk);
            case D2CC:
                return CseValidRequest.buildD2ccValidRequest(id, timestamp, ttcAdjustment, crac, cgm, glsk);
            default:
                throw new NotImplementedException("Unknown target process for CSE: " + cseValidAdapterConfiguration.getTargetProcess());
        }
    }

    private static Map<String, CseValidFileResource> getCseValidFileResourcesFromTaskDto(TaskDto taskDto) {
        return taskDto.getInputs().stream()
                .collect(Collectors.toMap(
                    ProcessFileDto::getFileType,
                    processFileDto -> new CseValidFileResource(processFileDto.getFilename(), processFileDto.getFileUrl())
                ));
    }

    private static CseValidFileResource getCseValidFileResource(Map<String, CseValidFileResource> files, String fileType) {
        return Optional.ofNullable(files.get(fileType))
                .orElseThrow(() -> new CseValidAdapterServiceException(fileType + " type not found"));
    }
}
