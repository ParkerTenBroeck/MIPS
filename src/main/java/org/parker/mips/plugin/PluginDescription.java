/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.plugin;

import java.io.InputStream;
import java.util.Map;
import org.parker.mips.plugin.SystemCall.SystemCall;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

/**
 *
 * @author parke
 */
public class PluginDescription {

    private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
        @Override
        protected Yaml initialValue() {
            return new Yaml(new SafeConstructor() {
                {
                    yamlConstructors.put(null, new AbstractConstruct() {
                        @Override
                        public Object construct(Node node) {
                            if (!node.getTag().startsWith("!@")) {
                                // Unknown tag - will fail
                                return SafeConstructor.undefinedConstructor.construct(node);
                            }
                            // Unknown awareness - provide a graceful substitution
//                            return new PluginAwareness() {
//                                @Override
//                                public String toString() {
//                                    return node.toString();
//                                }
//                            };
                            return null;
                        }
                    });
//                    for (PluginAwareness.Flags flag : PluginAwareness.Flags.values()) {
//                        yamlConstructors.put(new Tag("!@" + flag.name()), new AbstractConstruct() {
//                            @Override
//                            public PluginAwareness.Flags construct(final Node node) {
//                                return flag;
//                            }
//                        });
//                    }
                }
            });
        }
    };

    public final String NAME;
    public final String MAIN;
    public final String VERSION;
    public final String DESCRIPTION;
    public final SystemCall.SystemCallData[] SYSTEM_CALL_DATA;
    public final Map<String, Map<String, Object>> LAZY_SYSTEM_CALL_DATA;

    public PluginDescription(Map<String, Object> map) {
        this.NAME = map.get("name").toString();
        this.MAIN = map.get("main").toString();
        this.VERSION = map.get("version").toString();
        this.DESCRIPTION = map.get("description").toString();
        this.SYSTEM_CALL_DATA = null;
        this.LAZY_SYSTEM_CALL_DATA = (Map<String, Map<String, Object>>) map.get("system-calls");
    }

    public PluginDescription(InputStream input) {
        this((Map<String, Object>) (new Yaml().load(input)));
    }

    public Map<String, Object> saveMap() {
        return null;
    }
}
