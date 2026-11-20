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

import de.aniondev.flowlane.common.properties.FlowableRestAppProperties;
import de.aniondev.flowlane.common.security.ApiHttpSecurityCustomizer;
import de.aniondev.flowlane.common.security.DefaultPrivileges;
import de.aniondev.flowlane.common.security.SecurityConstants;
import de.aniondev.flowlane.task.properties.FlowlaneTaskAppProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Based on http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#multiple-httpsecurity
 * 
 * @author Joram Barrez
 * @author Tijs Rademakers
 * @author Filip Hrisafov
 */
@Configuration(proxyBeanMethods = false)
public class TaskSecurityConfiguration {

    //
    // BASIC AUTH
    //

    @Configuration
    @Order(SecurityConstants.TASK_API_SECURITY_ORDER)
    public static class TaskApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected final FlowableRestAppProperties restAppProperties;
        protected final FlowlaneTaskAppProperties taskAppProperties;
        protected final ApiHttpSecurityCustomizer apiHttpSecurityCustomizer;

        public TaskApiWebSecurityConfigurationAdapter(FlowableRestAppProperties restAppProperties,
                FlowlaneTaskAppProperties taskAppProperties, ApiHttpSecurityCustomizer apiHttpSecurityCustomizer) {
            this.restAppProperties = restAppProperties;
            this.taskAppProperties = taskAppProperties;
            this.apiHttpSecurityCustomizer = apiHttpSecurityCustomizer;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();

            if (taskAppProperties.isRestEnabled()) {

                if (restAppProperties.isVerifyRestApiPrivilege()) {
                    http.antMatcher("/*-api/**").authorizeRequests().antMatchers("/*-api/**").hasAuthority(DefaultPrivileges.ACCESS_REST_API);
                } else {
                    http.antMatcher("/*-api/**").authorizeRequests().antMatchers("/*-api/**").authenticated();
                    
                }

                apiHttpSecurityCustomizer.customize(http);
                
            } else {
                http.antMatcher("/*-api/**").authorizeRequests().antMatchers("/*-api/**").denyAll();
                
            }
                   
        }
    }

}
