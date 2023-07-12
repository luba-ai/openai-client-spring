package com.luba.gpt.parser.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleType extends BaseType {

    private String description;

    public SimpleType(String type, String description) {
        super(type);
        this.description = description;
    }
}
