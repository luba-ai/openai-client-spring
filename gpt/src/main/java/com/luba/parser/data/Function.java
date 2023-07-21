package com.luba.parser.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function {

    private String name;
    private String description;
    private BaseType parameters;
}
