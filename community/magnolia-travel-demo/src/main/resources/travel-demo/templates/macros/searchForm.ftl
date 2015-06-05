[#-- Creates a search form. Only the action parameter is mandatory --]
[#macro searchForm action inputName="queryStr" placeholder="Search" buttonLabel="Submit"]

<form action="${action!}" class="navbar-form navbar-right" role="search">
  <div class="form-group">
    <input type="text" name="${inputName}" class="form-control" placeholder="${placeholder}">
  </div>
  <button type="submit" class="btn btn-default">${buttonLabel}</button>
</form>

[/#macro]
