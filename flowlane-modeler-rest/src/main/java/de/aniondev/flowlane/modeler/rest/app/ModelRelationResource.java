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
package de.aniondev.flowlane.modeler.rest.app;

import java.util.List;

import de.aniondev.flowlane.common.service.exception.NotFoundException;
import de.aniondev.flowlane.modeler.domain.Model;
import de.aniondev.flowlane.modeler.domain.ModelInformation;
import de.aniondev.flowlane.modeler.service.ModelRelationService;
import de.aniondev.flowlane.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelRelationResource {

    @Autowired
    protected ModelService modelService;

    @Autowired
    protected ModelRelationService modelRelationService;

    @GetMapping(value = "/rest/models/{modelId}/parent-relations", produces = "application/json")
    public List<ModelInformation> getModelRelations(@PathVariable String modelId) {
        Model model = modelService.getModel(modelId);
        if (model == null) {
            throw new NotFoundException();
        }
        return modelRelationService.findParentModels(modelId);
    }

}
