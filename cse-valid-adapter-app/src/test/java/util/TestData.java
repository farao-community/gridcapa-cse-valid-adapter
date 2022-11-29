/*
 * Copyright (c) 2022, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package util;

import com.farao_community.farao.cse_valid.api.resource.CseValidFileResource;
import com.farao_community.farao.cse_valid.api.resource.CseValidRequest;
import com.farao_community.farao.cse_valid.api.resource.ProcessType;
import com.farao_community.farao.gridcapa.task_manager.api.ProcessFileDto;
import com.farao_community.farao.gridcapa.task_manager.api.ProcessFileStatus;
import com.farao_community.farao.gridcapa.task_manager.api.TaskDto;
import com.farao_community.farao.gridcapa.task_manager.api.TaskStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class TestData {

    private static final String TTC_ADJUSTMENT_FILE_TYPE = "TTC_ADJUSTMENT";
    private static final String TTC_ADJUSTMENT_FILE_NAME = "ttc_adj.txt";
    private static final String TTC_ADJUSTMENT_FILE_URL = "file://ttcadj.txt";

    private static final String CRAC_FILE_TYPE = "CRAC";
    private static final String CRAC_FILE_NAME = "crac.txt";
    private static final String CRAC_FILE_URL = "file://crac.txt";

    private static final String CGM_FILE_TYPE = "CGM";
    private static final String CGM_FILE_NAME = "cgm.uct";
    private static final String CGM_FILE_URL = "file://cgm.uct";

    private static final String GLSK_FILE_TYPE = "GLSK";
    private static final String GLSK_FILE_NAME = "glsk.xml";
    private static final String GLSK_FILE_URL = "file://glsk.xml";

    private static final UUID TASK_ID = UUID.randomUUID();

    private TestData() {
    }

    public static CseValidRequest getCseValidRequest(ProcessType processType) {
        String id = TASK_ID.toString();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        CseValidFileResource ttcAdjustment = new CseValidFileResource(TTC_ADJUSTMENT_FILE_NAME, TTC_ADJUSTMENT_FILE_URL);
        CseValidFileResource crac = new CseValidFileResource(CRAC_FILE_NAME, CRAC_FILE_URL);
        CseValidFileResource cgm = new CseValidFileResource(CGM_FILE_NAME, CGM_FILE_URL);
        CseValidFileResource glsk = new CseValidFileResource(GLSK_FILE_NAME, GLSK_FILE_URL);
        return new CseValidRequest(id, processType, timestamp, ttcAdjustment, crac, cgm, glsk, timestamp);
    }

    public static TaskDto getTaskDto(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp, TTC_ADJUSTMENT_FILE_URL));
        inputs.add(new ProcessFileDto(CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, CRAC_FILE_NAME, timestamp, CRAC_FILE_URL));
        inputs.add(new ProcessFileDto(CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp, CGM_FILE_URL));
        inputs.add(new ProcessFileDto(GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp, GLSK_FILE_URL));
        return new TaskDto(id, timestamp, status, Collections.emptyList(), inputs, Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getTaskDtoWithoutTtcFile(TaskStatus status) {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, CRAC_FILE_NAME, timestamp, CRAC_FILE_URL));
        inputs.add(new ProcessFileDto(CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp, CGM_FILE_URL));
        inputs.add(new ProcessFileDto(GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp, GLSK_FILE_URL));
        return new TaskDto(id, timestamp, status, Collections.emptyList(), inputs, Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getTaskDtoWithoutCracFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp, TTC_ADJUSTMENT_FILE_URL));
        inputs.add(new ProcessFileDto(CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp, CGM_FILE_URL));
        inputs.add(new ProcessFileDto(GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp, GLSK_FILE_URL));
        return new TaskDto(id, timestamp, status, Collections.emptyList(), inputs, Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getTaskDtoWithoutCgmFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp, TTC_ADJUSTMENT_FILE_URL));
        inputs.add(new ProcessFileDto(CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, CRAC_FILE_NAME, timestamp, CRAC_FILE_URL));
        inputs.add(new ProcessFileDto(GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp, GLSK_FILE_URL));
        return new TaskDto(id, timestamp, status, Collections.emptyList(), inputs, Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getTaskDtoWithoutGlskFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp, TTC_ADJUSTMENT_FILE_URL));
        inputs.add(new ProcessFileDto(CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, CRAC_FILE_NAME, timestamp, CRAC_FILE_URL));
        inputs.add(new ProcessFileDto(CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp, CGM_FILE_URL));
        return new TaskDto(id, timestamp, status, Collections.emptyList(), inputs, Collections.emptyList(), Collections.emptyList());
    }
}
