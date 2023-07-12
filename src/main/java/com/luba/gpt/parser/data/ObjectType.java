package com.luba.gpt.parser.data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ObjectType extends BaseType {

    private String type = "object";
    private Map<String, BaseType> properties;
    private Set<String> required;
}
