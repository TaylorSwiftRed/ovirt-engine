/*
Copyright (c) 2016 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.ovirt.engine.api.v3.adapters;

import static org.ovirt.engine.api.v3.adapters.V3OutAdapters.adaptOut;

import org.ovirt.engine.api.model.SchedulingPolicy;
import org.ovirt.engine.api.v3.V3Adapter;
import org.ovirt.engine.api.v3.types.V3Properties;
import org.ovirt.engine.api.v3.types.V3SchedulingPolicy;

public class V3SchedulingPolicyOutAdapter implements V3Adapter<SchedulingPolicy, V3SchedulingPolicy> {
    @Override
    public V3SchedulingPolicy adapt(SchedulingPolicy from) {
        V3SchedulingPolicy to = new V3SchedulingPolicy();
        if (from.isSetLinks()) {
            to.getLinks().addAll(adaptOut(from.getLinks()));
        }
        if (from.isSetActions()) {
            to.setActions(adaptOut(from.getActions()));
        }
        if (from.isSetComment()) {
            to.setComment(from.getComment());
        }
        if (from.isSetDefaultPolicy()) {
            to.setDefaultPolicy(from.isDefaultPolicy());
        }
        if (from.isSetDescription()) {
            to.setDescription(from.getDescription());
        }
        if (from.isSetId()) {
            to.setId(from.getId());
        }
        if (from.isSetHref()) {
            to.setHref(from.getHref());
        }
        if (from.isSetLocked()) {
            to.setLocked(from.isLocked());
        }
        if (from.isSetName()) {
            to.setName(from.getName());
        }
        if (from.isSetProperties()) {
            to.setProperties(new V3Properties());
            to.getProperties().getProperties().addAll(adaptOut(from.getProperties().getProperties()));
        }
        return to;
    }
}
