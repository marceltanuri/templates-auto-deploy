package br.com.mtanuri.liferay.templates.auto.deploy.service;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PropsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import br.com.mtanuri.liferay.templates.auto.deploy.TemplatesAutoDeploy;
import br.com.mtanuri.liferay.templates.auto.deploy.model.ClassNames;
import br.com.mtanuri.liferay.templates.auto.deploy.model.ResourceClassNames;
import br.com.mtanuri.liferay.templates.auto.deploy.model.Template;

@Component(immediate = true, service = TemplateService.class)
public class TemplateService {

	private final static String deployTemplatesDirName = "/deployTemplates";

	@Reference
	DDMTemplateLocalService ddmTemplateLocalService;

	@Reference
	ClassNameLocalService classNameLocalService;

	@Reference
	ResourceLocalService resourceLocalService;

	@Reference
	GroupLocalService groupLocalService;

	private List<Template> getTemplates() throws FileNotFoundException {

		List<Template> templates = new ArrayList<Template>();
		String deployTemplatesDirPath = PropsUtil.get("liferay.home") + deployTemplatesDirName;

		File deployTemplatesDirFile = new File(deployTemplatesDirPath);

		// create needed dirs
		if (!deployTemplatesDirFile.exists()) {
			deployTemplatesDirFile.mkdir();
			// TODO if a new group is craeted is needed to create a dir for it
			List<Group> groups = groupLocalService.getGroups(-1, -1);
			for (Group group : groups) {
				File groupDir = new File(deployTemplatesDirPath + "/" + group.getGroupId());
				groupDir.mkdir();
				for (ResourceClassNames resourceClassNames : ResourceClassNames.values()) {
					File resourceClassNameDir = new File(
							deployTemplatesDirPath + "/" + group.getGroupId() + "/" + resourceClassNames.name());
					resourceClassNameDir.mkdir();
					for (ClassNames classNames : ClassNames.values()) {
						if (classNames.getResourceClassName().equals(resourceClassNames)) {
							File classNameDir = new File(deployTemplatesDirPath + "/" + group.getGroupId() + "/"
									+ resourceClassNames.name() + "/" + classNames.name());
							classNameDir.mkdir();
						}
					}
				}
			}
		}

		if (deployTemplatesDirFile.isDirectory()) {
			List<Group> groups = groupLocalService.getGroups(-1, -1);
			for (Group group : groups) {
				for (ResourceClassNames resourceClassNames : ResourceClassNames.values()) {
					for (ClassNames classNames : ClassNames.values()) {
						if (classNames.getResourceClassName().equals(resourceClassNames)) {
							File classNameDir = new File(deployTemplatesDirPath + "/" + group.getGroupId() + "/"
									+ resourceClassNames.name() + "/" + classNames.name());
							for (File file : classNameDir.listFiles()) {
								Template template = new Template(file.getName(),
										this.getTemplateSourceCode(new FileInputStream(file)),
										classNames.getClassNameId(), resourceClassNames.getResourceClassNameId(),
										group.getGroupId());
								templates.add(template);
								file.delete();
							}
						}
					}
				}
			}
		}
		return templates;
	}

	private String getTemplateSourceCode(InputStream in) {
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine()) {
			sb.append(scanner.nextLine() + "\n");
		}
		scanner.close();
		return sb.toString();
	}

	public void processTemplates() {
		try {
			List<Template> templatesToBeProcessed = this.getTemplates();

			for (Template templateToBeProcessed : templatesToBeProcessed) {
				DynamicQuery dynamicQuery = ddmTemplateLocalService.dynamicQuery();

				dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", templateToBeProcessed.getGroupId()));
				dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId", templateToBeProcessed.getClassNameId()));
				dynamicQuery.add(RestrictionsFactoryUtil.eq("resourceClassNameId",
						templateToBeProcessed.getResourceClassNameId()));

				// TODO filtering name only in en_US
				dynamicQuery.add(RestrictionsFactoryUtil.ilike("name",
						"%<Name language-id=\"en_US\">" + templateToBeProcessed.getName() + "</Name>%"));

				List<DDMTemplate> ddmTemplates = ddmTemplateLocalService.dynamicQuery(dynamicQuery);

				if (ddmTemplates != null && !ddmTemplates.isEmpty()) {
					DDMTemplate ddmTemplate = ddmTemplates.get(0);

					_log.info("updating template... '" + ddmTemplate.getName(Locale.US) + "' (templateKey:"
							+ ddmTemplate.getTemplateKey() + ")");
					ddmTemplate.setScript(templateToBeProcessed.getScript());
					ddmTemplateLocalService.updateDDMTemplate(ddmTemplate);
					_log.info(ddmTemplate.getName(Locale.US) + "' (templateKey:" + ddmTemplate.getTemplateKey()
							+ ") has been updated with success!");
				} else {
					_log.info("inserting new template... '" + templateToBeProcessed.getName());
					long userId = 20129l; // TODO use some default admin user from the database
					Map<Locale, String> nameMap = new HashMap<Locale, String>();
					nameMap.put(Locale.US, templateToBeProcessed.getName());
					Map<Locale, String> descriptionMap = null;
					DDMTemplate ddmTemplate = ddmTemplateLocalService.addTemplate(userId,
							templateToBeProcessed.getGroupId(), templateToBeProcessed.getClassNameId(), 0l,
							templateToBeProcessed.getResourceClassNameId(), nameMap, descriptionMap, "display", null,
							"ftl", templateToBeProcessed.getScript(), new ServiceContext());
					_log.info(ddmTemplate.getName(Locale.US) + "' (templateKey:" + ddmTemplate.getTemplateKey()
							+ ") has been inserted with success!");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(TemplatesAutoDeploy.class);
}
