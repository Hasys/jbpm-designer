/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.designer.repository.impl;

import org.jbpm.designer.repository.Asset;

public class BinaryAsset extends AbstractAsset<byte[]> {

    private byte[] assetContent;


    public byte[] getAssetContent() {
        return this.assetContent;
    }

    @Override
    public void setAssetContent(byte[] content) {
        this.assetContent = content;
    }

    @Override
    public boolean acceptBytes() {
        return true;
    }
}
