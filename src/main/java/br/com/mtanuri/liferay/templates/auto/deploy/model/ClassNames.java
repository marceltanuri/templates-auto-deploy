package br.com.mtanuri.liferay.templates.auto.deploy.model;

public enum ClassNames {

	BlogsEntry(24901, "com.liferay.blogs.model.BlogsEntry", "PortletDisplayTemplate"),
	AssetEntry(20015, "com.liferay.asset.kernel.model.AssetEntry", "PortletDisplayTemplate"),
	DDMStructure(24809, "com.liferay.dynamic.data.mapping.model.DDMStructure", "JournalArticle"),
	BreadcrumbEntry(25459, "com.liferay.portal.kernel.servlet.taglib.ui.BreadcrumbEntry", "PortletDisplayTemplate"),
	AssetCategory(20013, "com.liferay.asset.kernel.model.AssetCategory", "PortletDisplayTemplate"),
	LanguageEntry(25473, "com.liferay.portal.kernel.servlet.taglib.ui.LanguageEntry", "PortletDisplayTemplate"),
	FileEntry(25469, "com.liferay.portal.kernel.repository.model.FileEntry", "PortletDisplayTemplate"),
	NavItem(20138, "com.liferay.portal.kernel.theme.NavItem", "PortletDisplayTemplate"),
	RSSFeed(25407, "com.liferay.rss.web.internal.util.RSSFeed", "PortletDisplayTemplate"),
	LayoutSet(20044, "com.liferay.portal.kernel.model.LayoutSet", "PortletDisplayTemplate"),
	AssetTag(20017, "com.liferay.asset.kernel.model.AssetTag", "PortletDisplayTemplate"),
	WikiPage(21902, "com.liferay.wiki.model.WikiPage", "PortletDisplayTemplate");

	private long classNameId;
	private String value;
	private String resourceClassName;

	private ClassNames(long classNameId, String value, String resourceClassName) {
		this.classNameId = classNameId;
		this.value = value;
		this.resourceClassName = resourceClassName;
	}

	public long getClassNameId() {
		return classNameId;
	}

	public String getValue() {
		return value;
	}

	public Enum<ResourceClassNames> getResourceClassName() {
		return ResourceClassNames.valueOf(resourceClassName);
	}

}