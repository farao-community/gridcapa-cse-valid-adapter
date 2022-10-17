/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.cse_valid.adapter.app;

import com.farao_community.farao.cse_valid.api.exception.CseValidInternalException;
import com.farao_community.farao.cse_valid.api.resource.CseValidRequest;
import com.farao_community.farao.gridcapa.task_manager.api.*;
import com.farao_community.farao.gridcapa_cse_valid.starter.CseValidClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Theo Pascoli {@literal <theo.pascoli at rte-france.com>}
 */
@SpringBootTest
class CseValidAdapterListenerTest {

    @Autowired
    CseValidAdapterListener cseValidAdapterListener;

    @Captor
    ArgumentCaptor<CseValidRequest> argumentCaptor;

    @MockBean
    CseValidClient cseValidClient;

    private String ttcAdjFileType;
    private String ttcAdjFileName;
    private String ttcAdjFileUrl;
    private String cracFileType;
    private String cracFileName;
    private String cracFileUrl;
    private String cgmFileType;
    private String cgmFileName;
    private String cgmFileUrl;
    private String glskFileType;
    private String glskFileName;
    private String glskFileUrl;

    @BeforeEach
    void setUp() {
        ttcAdjFileType = "TTC_ADJUSTMENT";
        ttcAdjFileName = "ttc_adj.txt";
        ttcAdjFileUrl = "file://ttcadj.txt";
        cracFileType = "CRAC";
        cracFileName = "crac.txt";
        cracFileUrl = "file://crac.txt";
        cgmFileType = "CGM";
        cgmFileName = "cgm.uct";
        cgmFileUrl = "file://cgm.uct";
        glskFileType = "GLSK";
        glskFileName = "glsk.xml";
        glskFileUrl = "file://glsk.xml";
    }

    @Test
    void testGetManualCseValidRequest() {
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.READY);
        CseValidRequest cseValidRequest = cseValidAdapterListener.buildCseValidRequest(taskDto);
        assertEquals(taskDto.getId().toString(), cseValidRequest.getId());
        assertEquals(cgmFileName, cseValidRequest.getCgm().getFilename());
        assertEquals(cgmFileUrl, cseValidRequest.getCgm().getUrl());
    }

    @Test
    void testGetCseValidRequestWithIncorrectFiles() {
        String wrongCracFileType = "Wrong-Crac";
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(ttcAdjFileType, ProcessFileStatus.VALIDATED, ttcAdjFileName, timestamp, ttcAdjFileUrl));
        inputs.add(new ProcessFileDto(cracFileType, ProcessFileStatus.VALIDATED, cracFileName, timestamp, cracFileUrl));
        inputs.add(new ProcessFileDto(cgmFileType, ProcessFileStatus.VALIDATED, cgmFileName, timestamp, cgmFileUrl));
        inputs.add(new ProcessFileDto(wrongCracFileType, ProcessFileStatus.VALIDATED, glskFileName, timestamp, glskFileUrl));
        List<ProcessEventDto> processEvents = new ArrayList<>();
        TaskDto taskDto = new TaskDto(id, timestamp, TaskStatus.READY, null, inputs, null, processEvents);
        assertThrows(IllegalArgumentException.class, () -> cseValidAdapterListener.buildCseValidRequest(taskDto));
    }

    @Test
    void consumeReadyTask() {
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.READY);
        cseValidAdapterListener.consumeTask().accept(taskDto);
        Mockito.verify(cseValidClient).run(argumentCaptor.capture());
        CseValidRequest cseValidRequest = argumentCaptor.getValue();
        assertNotNull(cseValidRequest);
    }

    @Test
    void consumeSuccessTask() {
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.SUCCESS);
        cseValidAdapterListener.consumeTask().accept(taskDto);
        Mockito.verify(cseValidClient).run(argumentCaptor.capture());
        CseValidRequest cseValidRequest = argumentCaptor.getValue();
        assertNotNull(cseValidRequest);
    }

    @Test
    void consumeErrorTask() {
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.ERROR);
        cseValidAdapterListener.consumeTask().accept(taskDto);
        Mockito.verify(cseValidClient).run(argumentCaptor.capture());
        CseValidRequest cseValidRequest = argumentCaptor.getValue();
        assertNotNull(cseValidRequest);
    }

    @Test
    void consumeCreatedTask() {
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.CREATED);
        cseValidAdapterListener.consumeTask().accept(taskDto);
        Mockito.verify(cseValidClient, Mockito.never()).run(argumentCaptor.capture());
    }

    @Test
    void consumeTaskThrowingError() {
        Mockito.doThrow(new CseValidInternalException("message")).when(cseValidClient).run(Mockito.any());
        TaskDto taskDto = createTaskDtoWithStatus(TaskStatus.ERROR);
        Consumer<TaskDto> taskDtoConsumer = cseValidAdapterListener.consumeTask();
        assertThrows(CseValidAdapterException.class, () -> taskDtoConsumer.accept(taskDto));
    }

    public TaskDto createTaskDtoWithStatus(TaskStatus status) {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(ttcAdjFileType, ProcessFileStatus.VALIDATED, ttcAdjFileName, timestamp, ttcAdjFileUrl));
        inputs.add(new ProcessFileDto(cracFileType, ProcessFileStatus.VALIDATED, cracFileName, timestamp, cracFileUrl));
        inputs.add(new ProcessFileDto(cgmFileType, ProcessFileStatus.VALIDATED, cgmFileName, timestamp, cgmFileUrl));
        inputs.add(new ProcessFileDto(glskFileType, ProcessFileStatus.VALIDATED, glskFileName, timestamp, glskFileUrl));
        List<ProcessEventDto> processEvents = new ArrayList<>();
        return new TaskDto(id, timestamp, status, null, inputs, null, processEvents);
    }
}
