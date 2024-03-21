/*
 * Copyright (c) 2023, RTE (http://www.rte-france.com)
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
    public static final String TTC_ADJUSTMENT_FILE_URL = "file://TTC_ADJUSTMENT/ttcadj.txt";
    public static final String TTC_ADJUSTMENT_FILE_PATH = "/TTC_ADJUSTMENT";

    private static final String IMPORT_CRAC_FILE_TYPE = "IMPORT_CRAC";
    private static final String IMPORT_CRAC_FILE_NAME = "importCrac.txt";
    public static final String IMPORT_CRAC_FILE_URL = "file://IMPORT_CRAC/importCrac.txt";
    public static final String IMPORT_CRAC_FILE_PATH = "/IMPORT_CRAC";

    private static final String EXPORT_CRAC_FILE_TYPE = "EXPORT_CRAC";
    private static final String EXPORT_CRAC_FILE_NAME = "exportCrac.txt";
    public static final String EXPORT_CRAC_FILE_URL = "file://EXPORT_CRAC/exportCrac.txt";
    public static final String EXPORT_CRAC_FILE_PATH = "/EXPORT_CRAC";

    private static final String CGM_FILE_TYPE = "CGM";
    private static final String CGM_FILE_NAME = "cgm.uct";
    public static final String CGM_FILE_URL = "file://CGM/cgm.uct";
    public static final String CGM_FILE_PATH = "/CGM";

    private static final String GLSK_FILE_TYPE = "GLSK";
    private static final String GLSK_FILE_NAME = "glsk.xml";
    public static final String GLSK_FILE_URL = "file://GLSK/glsk.xml";
    public static final String GLSK_FILE_PATH = "/GLSK";

    private static final UUID TASK_ID = UUID.randomUUID();

    private TestData() {
    }

    /* --------------- IMPORT CORNER --------------- */

    public static CseValidRequest getImportCseValidRequest(ProcessType processType) {
        String id = TASK_ID.toString();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        CseValidFileResource ttcAdjustment = new CseValidFileResource(TTC_ADJUSTMENT_FILE_NAME, TTC_ADJUSTMENT_FILE_URL);
        CseValidFileResource importCrac = new CseValidFileResource(IMPORT_CRAC_FILE_TYPE, IMPORT_CRAC_FILE_URL);
        CseValidFileResource cgm = new CseValidFileResource(CGM_FILE_NAME, CGM_FILE_URL);
        CseValidFileResource glsk = new CseValidFileResource(GLSK_FILE_NAME, GLSK_FILE_URL);
        return new CseValidRequest(id, processType, timestamp, ttcAdjustment, importCrac, null, cgm, glsk, timestamp);
    }

    public static TaskDto getImportTaskDto(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(IMPORT_CRAC_FILE_PATH, IMPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, IMPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getImportTaskDtoWithoutTtcFile(TaskStatus status) {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(IMPORT_CRAC_FILE_PATH, IMPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, IMPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getImportTaskDtoWithoutCgmFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(IMPORT_CRAC_FILE_PATH, IMPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, IMPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getImportTaskDtoWithoutGlskFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(IMPORT_CRAC_FILE_PATH, IMPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, IMPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /* --------------- EXPORT CORNER --------------- */

    public static CseValidRequest getExportCseValidRequest(ProcessType processType) {
        String id = TASK_ID.toString();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        CseValidFileResource ttcAdjustment = new CseValidFileResource(TTC_ADJUSTMENT_FILE_NAME, TTC_ADJUSTMENT_FILE_URL);
        CseValidFileResource exportCrac = new CseValidFileResource(EXPORT_CRAC_FILE_NAME, EXPORT_CRAC_FILE_URL);
        CseValidFileResource cgm = new CseValidFileResource(CGM_FILE_NAME, CGM_FILE_URL);
        CseValidFileResource glsk = new CseValidFileResource(GLSK_FILE_NAME, GLSK_FILE_URL);
        return new CseValidRequest(id, processType, timestamp, ttcAdjustment, null, exportCrac, cgm, glsk, timestamp);
    }

    public static TaskDto getExportTaskDto(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(EXPORT_CRAC_FILE_PATH, EXPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, EXPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto geExportTaskDtoWithoutTtcFile(TaskStatus status) {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(EXPORT_CRAC_FILE_PATH, EXPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, EXPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getExportTaskDtoWithoutCgmFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(EXPORT_CRAC_FILE_PATH, EXPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, EXPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public static TaskDto getExportTaskDtoWithoutGlskFile(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(EXPORT_CRAC_FILE_PATH, EXPORT_CRAC_FILE_TYPE, ProcessFileStatus.VALIDATED, EXPORT_CRAC_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /* --------------- IMPORT AND EXPORT CORNER --------------- */

    public static TaskDto getTaskDtoWithoutCracFiles(TaskStatus status) {
        UUID id = TASK_ID;
        OffsetDateTime timestamp = OffsetDateTime.parse("2022-05-03T14:30Z");
        List<ProcessFileDto> inputs = new ArrayList<>();
        inputs.add(new ProcessFileDto(TTC_ADJUSTMENT_FILE_PATH, TTC_ADJUSTMENT_FILE_TYPE, ProcessFileStatus.VALIDATED, TTC_ADJUSTMENT_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(CGM_FILE_PATH, CGM_FILE_TYPE, ProcessFileStatus.VALIDATED, CGM_FILE_NAME, timestamp));
        inputs.add(new ProcessFileDto(GLSK_FILE_PATH, GLSK_FILE_TYPE, ProcessFileStatus.VALIDATED, GLSK_FILE_NAME, timestamp));
        return new TaskDto(id, timestamp, status, inputs, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }
}
