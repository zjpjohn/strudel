/*******************************************************************************
 * Copyright 2015, 2016 Junichi Tatemura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.nec.strudel.workload.server;

import java.util.Set;

import javax.json.JsonObject;

import com.nec.strudel.workload.job.WorkRequest;

public interface WorkerService {

    WorkStatus init(WorkRequest work);

    WorkStatus start(String workId);

    WorkStatus stop(String workId);

    WorkStatus terminate(String workId) throws InterruptedException;

    WorkStatus getStatus(String workId);

    WorkStatus operate(String workId, String name, JsonObject data);

    JsonObject getReport(String workId);

    Set<String> works();
}
