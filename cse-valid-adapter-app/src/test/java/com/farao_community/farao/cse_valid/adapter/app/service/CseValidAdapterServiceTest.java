/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app.service;

import com.farao_community.farao.cse_valid.adapter.app.configuration.CseValidAdapterConfiguration;
import com.farao_community.farao.cse_valid.api.resource.CseValidRequest;
import com.farao_community.farao.cse_valid.api.resource.ProcessType;
import com.farao_community.farao.gridcapa.task_manager.api.TaskDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskStatus;
import com.farao_community.farao.gridcapa_cse_valid.starter.CseValidClient;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import util.TestData;

import static org.mockito.Mockito.*;

/**
 * @author Oualid Aloui {@literal <oualid.aloui at rte-france.com>}
 */
@SpringBootTest
class CseValidAdapterServiceTest {

    @MockBean
    private CseValidClient cseValidClient;

    @MockBean
    private CseValidAdapterConfiguration cseValidAdapterConfiguration;

    @MockBean
    private StreamBridge streamBridge;

    @MockBean
    private Logger businessLogger;

    @Autowired
    private CseValidAdapterService cseValidAdapterService;

    /* --------------- IMPORT CORNER --------------- */

    @Test
    void runAsyncImportIdccShouldCallSuccessfullyCseValidClientRun() {
        TaskDto taskDto = TestData.getImportTaskDto(TaskStatus.READY);
        CseValidRequest cseValidRequest = TestData.getImportCseValidRequest(ProcessType.IDCC);

        when(cseValidAdapterConfiguration.getTargetProcess()).thenReturn(ProcessType.IDCC);
        try (MockedStatic<CseValidRequest> mockStatic = mockStatic(CseValidRequest.class)) {
            mockStatic.when(() -> CseValidRequest.buildIdccValidRequest(any(), any(), any(), any(), any(), any(), any())).thenReturn(cseValidRequest);

            cseValidAdapterService.runAsync(taskDto);

            verify(streamBridge, never()).send(any(), any());
            verify(cseValidClient, timeout(1000).times(1)).run(cseValidRequest);
        }
    }

    @Test
    void runAsyncImportD2ccShouldCallSuccessfullyCseValidClientRun() {
        TaskDto taskDto = TestData.getImportTaskDto(TaskStatus.READY);
        CseValidRequest cseValidRequest = TestData.getImportCseValidRequest(ProcessType.D2CC);

        when(cseValidAdapterConfiguration.getTargetProcess()).thenReturn(ProcessType.D2CC);
        try (MockedStatic<CseValidRequest> mockStatic = mockStatic(CseValidRequest.class)) {
            mockStatic.when(() -> CseValidRequest.buildD2ccValidRequest(any(), any(), any(), any(), any(), any(), any())).thenReturn(cseValidRequest);

            cseValidAdapterService.runAsync(taskDto);

            verify(streamBridge, never()).send(any(), any());
            verify(cseValidClient, timeout(1000).times(1)).run(cseValidRequest);
        }
    }

    @Test
    void runAsyncImportShouldFailBecauseTtcAdjustmentFileNotFound() {
        TaskDto taskDto = TestData.getImportTaskDtoWithoutTtcFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    @Test
    void runAsyncImportShouldFailBecauseCgmFileNotFound() {
        TaskDto taskDto = TestData.getImportTaskDtoWithoutCgmFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    @Test
    void runAsyncImportcShouldFailBecauseGlskFileNotFound() {
        TaskDto taskDto = TestData.getImportTaskDtoWithoutGlskFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    /* --------------- EXPORT CORNER --------------- */

    @Test
    void runAsyncExportIdccShouldCallSuccessfullyCseValidClientRun() {
        TaskDto taskDto = TestData.getExportTaskDto(TaskStatus.READY);
        CseValidRequest cseValidRequest = TestData.getExportCseValidRequest(ProcessType.IDCC);

        when(cseValidAdapterConfiguration.getTargetProcess()).thenReturn(ProcessType.IDCC);
        try (MockedStatic<CseValidRequest> mockStatic = mockStatic(CseValidRequest.class)) {
            mockStatic.when(() -> CseValidRequest.buildIdccValidRequest(any(), any(), any(), any(), any(), any(), any())).thenReturn(cseValidRequest);

            cseValidAdapterService.runAsync(taskDto);

            verify(streamBridge, never()).send(any(), any());
            verify(cseValidClient, timeout(1000).times(1)).run(cseValidRequest);
        }
    }

    @Test
    void runAsyncExportD2ccShouldCallSuccessfullyCseValidClientRun() {
        TaskDto taskDto = TestData.getExportTaskDto(TaskStatus.READY);
        CseValidRequest cseValidRequest = TestData.getExportCseValidRequest(ProcessType.D2CC);

        when(cseValidAdapterConfiguration.getTargetProcess()).thenReturn(ProcessType.D2CC);
        try (MockedStatic<CseValidRequest> mockStatic = mockStatic(CseValidRequest.class)) {
            mockStatic.when(() -> CseValidRequest.buildD2ccValidRequest(any(), any(), any(), any(), any(), any(), any())).thenReturn(cseValidRequest);

            cseValidAdapterService.runAsync(taskDto);

            verify(streamBridge, never()).send(any(), any());
            verify(cseValidClient, timeout(1000).times(1)).run(cseValidRequest);
        }
    }

    @Test
    void runAsyncExportShouldFailBecauseTtcAdjustmentFileNotFound() {
        TaskDto taskDto = TestData.geExportTaskDtoWithoutTtcFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    @Test
    void runAsyncExportShouldFailBecauseCgmFileNotFound() {
        TaskDto taskDto = TestData.getExportTaskDtoWithoutCgmFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    @Test
    void runAsyncExportcShouldFailBecauseGlskFileNotFound() {
        TaskDto taskDto = TestData.getExportTaskDtoWithoutGlskFile(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }

    /* --------------- IMPORT AND EXPORT CORNER --------------- */

    @Test
    void runAsyncShouldFailBecauseCracFilesNotFound() {
        TaskDto taskDto = TestData.getTaskDtoWithoutCracFiles(TaskStatus.READY);
        cseValidAdapterService.runAsync(taskDto);
        verify(cseValidClient, never()).run(any());
        verify(streamBridge, times(1)).send(any(), any());
    }
}
