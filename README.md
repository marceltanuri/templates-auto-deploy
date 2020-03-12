# templates-auto-deploy
A Liferay 7.2 Service for auto deploying Liferay templates (widgets-templates, webContent-templates, structures [in the future])

Obs: This is a initial idea with the basics features:
* Auto deploy of widgetTemplates
* Auto deploy of webContentTemplates
* It is considered that templates have a unique name. In case of double results only one will be processed.
* It is only working for templates that already exist in the database

In the feature:
* To work for templates that already don't exist in the database (add new template)
* Identify the template classnameId (AssetEntry, BlogEntry, etc)
