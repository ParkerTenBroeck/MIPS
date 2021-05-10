/*
 *    Copyright 2021 ParkerTenBroeck
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.parker.assembleride.preferences;

import java.util.*;

public class WeakMultiMap<E, K> extends WeakHashMap<E, Set<K>> {


    public void putS(E key, K value) {
        Set<K> list = super.get(key);
        if(list == null){
            list = new HashSet<>();
            list.add(value);
            super.put(key, list);
        }else{
            list.add(value);
        }
    }

    public void removeS(E key, K value){
        Set<K> list = super.get(key);
        if(list != null){
            list.remove(value);
        }
    }

    public void removeAll(E key) {
        super.remove(key);
    }
}
