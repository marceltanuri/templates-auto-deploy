# templates-auto-deploy
A Liferay 7.2 Service for auto deploying Liferay templates (widgets-templates, webContent-templates, structures [in the future])

How it works:
1) Install the service
2) If you have a widgetTemplate with the name myBlogADT, so the only thing you need to do is to copy your myBlogADT.ftl file to your new directory {liferay.home}/deployTemplates
3) Your file will be read and your ADT will be updated.

Obs: This is a initial idea with only some basic features:
* Auto deploy of widgetTemplates
* Auto deploy of webContentTemplates
* It is considered that templates have a unique name. In case of double results only one will be processed.
* It is only working for templates that already exist in the database

In the feature:
* Auto deploy of ddmStructures
* To work for templates that don't exist yet in the database (add new template)
* Identify the template classnameId (AssetEntry, BlogEntry, etc)
