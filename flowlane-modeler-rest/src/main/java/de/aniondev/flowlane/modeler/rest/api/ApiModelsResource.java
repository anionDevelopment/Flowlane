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
package de.aniondev.flowlane.modeler.rest.api;

import javax.servlet.http.HttpServletRequest;

import de.aniondev.flowlane.common.model.ResultListDataRepresentation;
import de.aniondev.flowlane.common.security.SecurityUtils;
import de.aniondev.flowlane.common.service.exception.BadRequestException;
import de.aniondev.flowlane.modeler.domain.Model;
import de.aniondev.flowlane.modeler.model.ModelKeyRepresentation;
import de.aniondev.flowlane.modeler.model.ModelRepresentation;
import de.aniondev.flowlane.modeler.service.FlowableModelQueryService;
import de.aniondev.flowlane.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiModelsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiModelsResource.class);

    @Autowired
    protected FlowableModelQueryService modelQueryService;

    @Autowired
    protected ModelService modelService;

    @Autowired
    protected ObjectMapper objectMapper;

    @GetMapping(value = "/models", produces = "application/json")
    public ResultListDataRepresentation getModels(@RequestParam(required = false) String filter, @RequestParam(required = false) String sort, @RequestParam(required = false) Integer modelType,
            HttpServletRequest request) {

        return modelQueryService.getModels(filter, sort, modelType, request);
    }

    @PostMapping(value = "/import-process-model", produces = "application/json")
    public ModelRepresentation importProcessModel(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        return modelQueryService.importProcessModel(request, file);
    }

    @PostMapping(value = "/models", produces = "application/json")
    public ModelRepresentation createModel(@RequestBody ModelRepresentation modelRepresentation) {
        modelRepresentation.setKey(modelRepresentation.getKey().replaceAll(" ", ""));

        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, modelRepresentation.getModelType(), modelRepresentation.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Provided model key already exists: " + modelRepresentation.getKey());
        }

        String json = modelService.createModelJson(modelRepresentation);

        Model newModel = modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserId());
        return new ModelRepresentation(newModel);
    }

}
