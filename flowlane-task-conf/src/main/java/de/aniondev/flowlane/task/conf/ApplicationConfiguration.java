/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.aniondev.flowlane.task.conf;

import de.aniondev.flowlane.task.properties.FlowlaneTaskAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FlowlaneTaskAppProperties.class)
@ComponentScan(basePackages = {
        "de.aniondev.flowlane.task.conf",
        "de.aniondev.flowlane.task.service",
        "de.aniondev.flowlane.task.model.component",
        "de.aniondev.flowlane.common.repository" })
@EnableScheduling
@EnableAsync
public class ApplicationConfiguration {

    @Bean
    public TaskExecutorCustomizer flowableTaskTaskExecutorCustomizer() {
        return taskExecutor -> {
            // Not yet exposed via properties in Spring Boot
            taskExecutor.setAwaitTerminationSeconds(30);
            taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        };
    }
}
