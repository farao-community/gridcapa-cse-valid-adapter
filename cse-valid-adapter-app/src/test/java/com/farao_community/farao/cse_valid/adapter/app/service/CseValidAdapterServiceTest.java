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
import com.farao_community.farao.minio_adapter.starter.MinioAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import util.TestData;

import static org.mockito.Mockito.*;
import static util.TestData.*;

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

    @MockBean
    private MinioAdapter minioAdapter;

    @Autowired
    private CseValidAdapterService cseValidAdapterService;

    @BeforeEach
    void setUp() {
        Mockito.when(minioAdapter.generatePreSignedUrlFromFullMinioPath(TTC_ADJUSTMENT_FILE_PATH, 1)).thenReturn(TTC_ADJUSTMENT_FILE_URL);
        Mockito.when(minioAdapter.generatePreSignedUrlFromFullMinioPath(IMPORT_CRAC_FILE_PATH, 1)).thenReturn(IMPORT_CRAC_FILE_URL);
        Mockito.when(minioAdapter.generatePreSignedUrlFromFullMinioPath(EXPORT_CRAC_FILE_PATH, 1)).thenReturn(EXPORT_CRAC_FILE_URL);
        Mockito.when(minioAdapter.generatePreSignedUrlFromFullMinioPath(CGM_FILE_PATH, 1)).thenReturn(CGM_FILE_URL);
        Mockito.when(minioAdapter.generatePreSignedUrlFromFullMinioPath(GLSK_FILE_PATH, 1)).thenReturn(GLSK_FILE_URL);
    }
    /* --------------- FULL IMPORT --------------- */

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
