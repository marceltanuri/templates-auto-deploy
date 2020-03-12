package br.com.mtanuri.liferay.templates.auto.deploy.service;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import br.com.mtanuri.liferay.templates.auto.deploy.TemplatesAutoDeploy;
import br.com.mtanuri.liferay.templates.auto.deploy.model.Template;

@Component(immediate = true, service = TemplateService.class)
public class TemplateService {

	private final static String path = "/deployTemplates";

	@Reference
	DDMTemplateLocalService ddmTemplateLocalService;

	private List<Template> getTemplates() throws FileNotFoundException {

		List<Template> templates = new ArrayList<Template>();

		File deployTemplatesDir = new File(PropsUtil.get("liferay.home") + path);

		if (!deployTemplatesDir.exists()) {
			deployTemplatesDir.mkdir();
		}

		if (deployTemplatesDir.isDirectory()) {
			for (File file : deployTemplatesDir.listFiles()) {
				Template template = new Template(file.getName(), this.getTemplateSourceCode(new FileInputStream(file)));
				templates.add(template);
				file.delete();
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

				// TODO use classNameId as filter
				// dynamicQuery.add(RestrictionsFactoryUtil.eq("CLASSNAMEID", ""));

				// TODO filtering name only in en_US
				dynamicQuery.add(RestrictionsFactoryUtil.ilike("name",
						"%<Name language-id=\"en_US\">" + templateToBeProcessed.getName() + "</Name>%"));

				List<DDMTemplate> ddmTemplates = ddmTemplateLocalService.dynamicQuery(dynamicQuery);

				// TODO As we filter templates only by name, we are still needing to get the
				// first result.
				if (ddmTemplates != null && !ddmTemplates.isEmpty()) {
					DDMTemplate ddmTemplate = ddmTemplates.get(0);

					_log.info("updating template... '" + ddmTemplate.getName(Locale.US) + "' (templateKey:"
							+ ddmTemplate.getTemplateKey() + ")");

					ddmTemplate.setScript(templateToBeProcessed.getScript());
					ddmTemplateLocalService.updateDDMTemplate(ddmTemplate);
				} else {
					// TODO logic to add a new template when it doesn't exist on database, will be
					// needed a way to identify the classnameId (AssetEntry, BlogEntry, etc). To
					// have specifcs folders (AssetEntry, BlogEntry, etc) under deployTemplate could
					// be a good idea.
					// templateService.addDDMTemplate(newDDMTemplate);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(TemplatesAutoDeploy.class);
}
