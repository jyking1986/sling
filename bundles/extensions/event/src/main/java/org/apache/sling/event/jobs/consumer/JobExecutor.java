/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.event.jobs.consumer;

import org.apache.sling.event.jobs.Job;

import aQute.bnd.annotation.ConsumerType;

/**
 * A job executor consumes a job.
 *
 * A job executor registers itself with the {@link #PROPERTY_TOPICS} service registration
 * property. The value of this property defines which topics an executor is able to process.
 * Each string value of this property is either a job topic or a topic category ending
 * with "/*" which means all topics in this category.
 * For example, the value "org/apache/sling/jobs/*" matches the topics
 * "org/apache/sling/jobs/a" and "org/apache/sling/jobs/b" but neither
 * "org/apache/sling/jobs" nor "org/apache/sling/jobs/subcategory/a"
 *
 * If there is more than one job executor or consumer registered for a job topic,
 * the selection is as follows:
 * - If there is a single consumer registering for the exact topic, this one is used
 * - If there is more than a single consumer registering for the exact topic, the one
 *   with the highest service ranking is used. If the ranking is equal, the one with
 *   the lowest service ID is used.
 * - If there is a single consumer registered for the category, it is used
 * - If there is more than a single consumer registered for the category, the service
 *   with the highest service ranking is used. If the ranking is equal, the one with
 *   the lowest service ID is used.
 *
 * If the executor decides to process the job asynchronously, the processing must finish
 * within the current lifetime of the job executor. If the executor (or the instance
 * of the executor) dies, the job processing will mark this processing as failed and
 * reschedule.
 *
 * @since 1.1
 */
@ConsumerType
public interface JobExecutor {

    /**
     * Service registration property defining the jobs this executor is able to process.
     * The value is either a string or an array of strings.
     */
    String PROPERTY_TOPICS = "job.topics";

    /**
     * Execute the job.
     *
     * If the job has been processed successfully, {@link JobExecutionContext#SUCCEEDED()} should be returned.
     * If the job has not been processed completely, but might be rescheduled {@link JobExecutionContext#FAILED()}
     * should be returned.
     * If the job processing failed and should not be rescheduled, {@link JobExecutionContext#CANCELLED()} should
     * be returned.
     *
     * If the executor decides to process the job asynchronously it should return <code>null</code>
     * and notify the job manager by using the {@link JobExecutionContext#asyncProcessingFinished(JobExecutionResult)}
     * method.
     *
     * If the processing fails with throwing an exception/throwable, the process will not be rescheduled
     * and treated like the method would have returned {@link JobExecutionContext#CANCELLED()}.
     *
     * Instead of the constants from the JobExecutionContext class, this method can return a custom instance containing
     * additional information using the builder pattern available from {@link JobExecutionContext#result(String)}.
     *
     * @param job The job
     * @param context The execution context.
     * @return The job execution result
     */
    JobExecutionResult process(Job job, JobExecutionContext context);
}
