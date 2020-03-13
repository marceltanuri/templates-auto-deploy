# templates-auto-deploy
A Liferay 7.2 Service for auto deploying Liferay templates (widgets-templates, webContent-templates, structures [in the future])

How it works:
1) Install the service
2) If you have a widgetTemplate with the name myBlogADT, so the only thing you need to do is to copy your myBlogADT.ftl file to your new directory {liferay.home}/deployTemplates

The deployTemplates dir has the following sub-dirs

- /deployTemplates

-- /{groupId}

--- /{resourceClassName} for example: PortletDisplayTemplate

---- /{className} for example: BlogsEntry

3) Your file will be read and your ADT will be updated.

Obs: This is a initial idea with only some basic features:
* Auto deploy of existing widgetTemplates
* Auto deploy of existing webContentTemplates
* Auto deploy of new widgetTemplates
* Auto deploy of new webContentTemplates

In the feature:
* Auto deploy of ddmStructures
