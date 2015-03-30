[#-- Shared setup for all components. --]
[#assign divIDPrefix = def.parameters.divIDPrefix!]
[#assign divClass = def.parameters.divClass!""]

[#assign divID = ""]

[#if divIDPrefix?has_content]
    [#assign divID = ' id="${divIDPrefix}-${content.@id}"']
[/#if]
