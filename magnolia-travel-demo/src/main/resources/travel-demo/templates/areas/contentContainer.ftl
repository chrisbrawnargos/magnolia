[#-------------- ASSIGNMENTS --------------]
[#assign cssClass="col-lg-12"]
[#if def.parameters?? && def.parameters.cssClass??]
    [#assign cssClass=def.parameters.cssClass]
[/#if]

[#-------------- RENDERING --------------]
<div class="${cssClass}">
[#list components as component ]
    [@cms.component content=component /]
[/#list]
</div>