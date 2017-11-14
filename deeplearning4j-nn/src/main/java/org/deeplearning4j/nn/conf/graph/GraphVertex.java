/*-
 *
 *  * Copyright 2016 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.nn.conf.graph;

import lombok.EqualsAndHashCode;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.inputs.InvalidInputTypeException;
import org.deeplearning4j.optimize.api.IterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.shade.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Collection;

/**
 * A GraphVertex is a vertex in the computation graph. It may contain Layer, or define some arbitrary forward/backward pass
 * behaviour based on the inputs
 *
 * @author Alex Black
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@EqualsAndHashCode
@Deprecated
public abstract class GraphVertex extends BaseGraphVertex implements Cloneable, Serializable {

    @Override
    public abstract GraphVertex clone();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    public abstract int numParams(boolean backprop);

    /**
     * @return The Smallest valid number of inputs to this vertex
     */
    public abstract int minInputs();

    /**
     * @return The largest valid number of inputs to this vertex
     */
    public abstract int maxInputs();

    /**
     * Create a {@link Layer} instance, for the given computation graph,
     * given the configuration instance.
     *
     * @param name             The name of the GraphVertex object
     * @param initializeParams If true: initialize the parameters. If false: make no change to the values in the paramsView array   @return The implementation GraphVertex object (i.e., implementation, no the configuration)
     */
    public abstract Layer instantiate(Collection<IterationListener> iterationListeners,
                                      String name, int layerIndex, int numInputs, INDArray layerParamsView,
                                      boolean initializeParams);

    /**
     * Determine the type of output for this GraphVertex, given the specified inputs. Given that a GraphVertex may do arbitrary
     * processing or modifications of the inputs, the output types can be quite different to the input type(s).<br>
     * This is generally used to determine when to add preprocessors, as well as the input sizes etc for layers
     *
     * @param layerIndex The index of the layer (if appropriate/necessary).
     * @return The type of output for this vertex
     * @throws InvalidInputTypeException If the input type is invalid for this type of GraphVertex
     */
    public abstract InputType[] getOutputType(int layerIndex, InputType... inputTypes);

}
