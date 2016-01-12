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
package com.nec.strudel.workload.test;

import com.nec.strudel.workload.com.ProcessCommandConfig;
import com.nec.strudel.workload.job.Job;

public class ResourceNames {

	public static final ResourceFile<Job> JOB1 = Resources.of("job001", Job.class);

	public static final String JOB_SUITE1 = "jobsuite001";

	public static final ResourceFile<ProcessCommandConfig> COMMAND001 =
			Resources.of("command001", ProcessCommandConfig.class);
}