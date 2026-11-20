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
package de.aniondev.flowlane.modeler.conf;

import javax.servlet.MultipartConfigElement;

import de.aniondev.flowlane.modeler.properties.FlowlaneModelerAppProperties;
import de.aniondev.flowlane.modeler.service.FlowableModelQueryService;
import de.aniondev.flowlane.modeler.service.ModelImageService;
import de.aniondev.flowlane.modeler.service.ModelServiceImpl;
import de.aniondev.flowlane.modeler.serviceapi.ModelService;
import de.aniondev.flowlane.modeler.servlet.ApiDispatcherServletConfiguration;
import de.aniondev.flowlane.modeler.servlet.AppDispatcherServletConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FlowlaneModelerAppProperties.class)
@ComponentScan(basePackages = {
        "de.aniondev.flowlane.modeler.conf",
        "de.aniondev.flowlane.modeler.repository",
        "de.aniondev.flowlane.modeler.security",
        "de.aniondev.flowlane.common.repository",
        "de.aniondev.flowlane.common.tenant" })
public class ApplicationConfiguration {

    @Bean
    public ServletRegistrationBean modelerApiServlet(ApplicationContext applicationContext) {
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(applicationContext);
        dispatcherServletConfiguration.register(ApiDispatcherServletConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(dispatcherServletConfiguration);
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet, "/api/editor/*");
        registrationBean.setName("Flowlane Modeler App API Servlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> modelerAppServlet(ApplicationContext applicationContext, ObjectProvider<MultipartConfigElement> multipartConfig) {
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(applicationContext);
        dispatcherServletConfiguration.register(AppDispatcherServletConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(dispatcherServletConfiguration);
        ServletRegistrationBean<DispatcherServlet> registrationBean = new ServletRegistrationBean<>(servlet, "/modeler-app/*");
        registrationBean.setName("Flowlane Modeler App Servlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        multipartConfig.ifAvailable(registrationBean::setMultipartConfig);
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurer modelerApplicationWebMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addViewControllers(@NonNull ViewControllerRegistry registry) {

                if (!ClassUtils.isPresent("de.aniondev.flowlane.task.conf.ApplicationConfiguration", getClass().getClassLoader())) {
                    // If the task application is not present, then the root should be mapped to admin
                    registry.addViewController("/").setViewName("redirect:/modeler/");
                }
                registry.addViewController("/modeler").setViewName("redirect:/modeler/");
                registry.addViewController("/modeler/").setViewName("forward:/modeler/index.html");
            }
        };
    }


    // The services are shared between the api and app rest modules
    @Bean
    public ModelService modelerModelService() {
        return new ModelServiceImpl();
    }

    @Bean
    public ModelImageService modelerModelImageService() {
        return new ModelImageService();
    }

    @Bean
    public FlowableModelQueryService modelerModelQueryService() {
        return new FlowableModelQueryService();
    }

}
