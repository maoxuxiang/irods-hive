package org.irods.jargon.hive.external.utils.template;

import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to manipulate SPARQL templates for this library. These are just text
 * files loaded from resources that have parameters to plug in.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class SPARQLTemplateUtils {

	public static final Logger log = LoggerFactory
			.getLogger(SPARQLTemplateUtils.class);

	/**
	 * Given a SPARQL template and corresponding name/value pairs that represent
	 * values to substitute in the template, derive the final query data
	 * 
	 * @param templatePath
	 *            <code>String</code> with a template path under resources
	 * @param replacementValues
	 *            <code>Map<String,String></code> that represents the name/value
	 *            pairs to substitute into the template (denoted as ${name}
	 *            fields)
	 * @return <code>String</code> with the sparql data, with place holder
	 *         values replaced
	 * @throws HiveTemplateException
	 */
	public static String getSPARQLTemplateAndSubstituteValues(
			final String templatePath,
			final Map<String, String> replacementValues)
			throws HiveTemplateException {

		log.info("getSPARQLTemplateAndSubstituteValues(final String templatePath,final Map<String, String> replacementValues)");

		if (templatePath == null || templatePath.isEmpty()) {
			throw new IllegalArgumentException("null or empty templatePath");
		}

		if (replacementValues == null) {
			throw new IllegalArgumentException("null replacementValues");
		}

		log.info("templatePath:{}", templatePath);
		log.info("replacementValues:{}", replacementValues);

		String rawSparql = getRawSPARQLTemplate(templatePath);
		StrSubstitutor sub = new StrSubstitutor(replacementValues);
		String resolvedString = sub.replace(rawSparql);
		log.info("resolvedSparql:{}", resolvedString);
		return resolvedString;

	}

	/**
	 * Given the path in the resources, load the given template as a String
	 * 
	 * @param templatePath
	 *            <code>String</code> with a template path under resources
	 * @return <code>String</code> with the template contents
	 * @throws HiveTemplateException
	 *             if the template is not found or another error occurs
	 */
	public static String getRawSPARQLTemplate(final String templatePath)
			throws HiveTemplateException {

		log.info("getRawSPARQLTemplate(final String templatePath)");

		if (templatePath == null || templatePath.isEmpty()) {
			throw new IllegalArgumentException("null or empty templatePath");
		}

		log.info("templatePath:{}", templatePath);

		try {
			return LocalFileUtils
					.getClasspathResourceFileAsString(templatePath);
		} catch (JargonException e) {
			log.error("exception loading template from resource file", e);
			throw new HiveTemplateException(e);
		}

	}

}
